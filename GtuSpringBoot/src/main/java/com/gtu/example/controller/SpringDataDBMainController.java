package com.gtu.example.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.reflect.FieldUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gtu.example.common.JSONUtil;
import com.gtu.example.common.JqGridHandler.EntityPrimaryKeySetter;
import com.gtu.example.common.JqGridHandler.JqReader;
import com.gtu.example.common.JqGridHandler.SimpleJdGridCreater;
import com.gtu.example.common.PackageReflectionUtil;
import com.gtu.example.common.RepositoryReflectionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/springdata-dbMain/")
public class SpringDataDBMainController {

    private static final Logger log = LoggerFactory.getLogger(SpringDataDBMainController.class);

    @Autowired
    private ConfigurableApplicationContext ctx;

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "/tables")
    public String tables() {
        List<String> tabLst = new ArrayList<String>();
        tabLst.add("Address");
        tabLst.add("Employee");
        tabLst.add("WorkItem");
        return JSONArray.fromObject(tabLst).toString();
    }

    @GetMapping(value = "/dbMain")
    public ModelAndView dbMain() {
        ModelAndView model = new ModelAndView();
        model.setViewName("db_process");
        return model;
    }

    private Set<Class<?>> getAllTables() {
        Set<Class<?>> all = new LinkedHashSet<>();
        Set<Class<?>> s1 = new Reflections("com.gtu.example.springdata.entity").getSubTypesOf(Object.class);
        log.info("getAllTables material {}", s1.size());

        all.addAll(s1);
        log.info("getAllTables = {}", all.size());
        return all;
    }

    @GetMapping(value = "/table-get-columns")
    public String tableColumns(@RequestParam(name = "table") String table) {
        Optional<List<String>> obj = getAllTables().stream()//
                .filter(c -> c.getSimpleName().equalsIgnoreCase(table))//
                .findFirst().map(c -> {
                    List<String> lst1 = Stream.of(c.getDeclaredFields())//
                            .map(Field::getName)//
                            .collect(Collectors.toList());

                    if (c.getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
                        List<String> lst2 = Stream.of(c.getSuperclass().getDeclaredFields()).map(Field::getName)//
                                .collect(Collectors.toList());
                        List<String> allLst = new ArrayList<>();

                        allLst.addAll(lst2);
                        allLst.addAll(lst1);
                        return allLst;
                    }

                    return lst1;
                });
        return JSONArray.fromObject(obj.get()).toString();
    }

    @GetMapping(value = "/table-mapping")
    public String tableMapping(@RequestParam(name = "table") String table) throws ClassNotFoundException {
        Map<Class<Object>, Class[]> repositoryEntityMap = getAllRepostiriesMap();
        
        Class<?> entityClz = Class.forName(String.format("com.gtu.example.springdata.entity.%s", table));
        Class<?>[] clz = repositoryEntityMap.get(entityClz);

        Class repository = clz[0];
        Class entityKey = clz[1];

        JSONObject obj = new JSONObject();

        obj.put("repository", repository.getName());
        obj.put("entityClz", entityClz.getName());
        obj.put("entityKey", entityKey.getName());

        log.info("return : {}", obj);
        return obj.toString();
    }
    
    private Map<Class<Object>, Class[]> getAllRepostiriesMap() {
        Map<Class<Object>, Class[]> all = new LinkedHashMap<>();
        Map<Class<Object>, Class[]> m1 = RepositoryReflectionUtil.getRepositoryEntityMap("com.gtu.example.springdata.dao_1");
        log.info("getAllRepostiriesMap material {}", m1.size());
        all.putAll(m1);
        log.info("getAllRepostiriesMap = {}", all.size());
        return all;
    }

    @PostMapping(value = "/db_simple_query/{type}")
    public String queryDBAction(@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("# queryDBAction start ..");
            OperateDefine define = this.getOperateDefine(request);

            SimpleJdGridCreater handler = new SimpleJdGridCreater(define.entityClz);
            if ("colModel".equals(type)) {
                return handler.getColModel().toString();
            } else {
                List<?> lst = (List<?>) define.repository.findAll();
                log.info("size = {}", lst.size());

                JqReader reader = handler.getSimpleJqReader(lst, null, new String[] { define.entityId }, null, 1, lst.size());
                return handler.toJSONArray(reader).toString();
            }
        } catch (Exception ex) {
            log.error("queryDBAction ERR : " + ex.getMessage(), ex);
            return JSONUtil.getThrowable(ex).toString();
        }
    }

    private class OperateDefine<T, ID> {
        Class<T> repositoryClz;
        CrudRepository<T, ID> repository;
        Class<T> entityClz;
        Class<ID> entityKey;
        String entityId;
        T entity;
    }

    private void debugParameter(HttpServletRequest request) {
        log.info("debugParameter start ---");
        for (Enumeration<String> enu = request.getParameterNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = request.getParameter(key);
            log.info(" - param : {} - {}", key, value);
        }
        log.info("debugParameter end   ---");
    }

    private OperateDefine getOperateDefine(HttpServletRequest request) throws ClassNotFoundException {
        this.debugParameter(request);

        OperateDefine define = new OperateDefine();
        define.repositoryClz = Class.forName(request.getParameter("repository"));
        define.entityClz = Class.forName(request.getParameter("entityClz"));
        define.entityKey = Class.forName(request.getParameter("entityKey"));
        define.entityId = RepositoryReflectionUtil.getEntityId(define.entityClz);

        log.info("getOperateDefine Result = {}", ReflectionToStringBuilder.toString(define, ToStringStyle.MULTI_LINE_STYLE));

        define.repository = (CrudRepository) ctx.getBean(define.repositoryClz);
        define.entity = PackageReflectionUtil.newInstanceDefault(define.entityClz, false);
        if (define.entity == null) {
            throw new RuntimeException("entity is null !!");
        }

        for (Enumeration<String> enu = request.getParameterNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = request.getParameter(key);
            log.info("mapping to column : {} - {}", key, value);
            try {
                define.entity.getClass().getDeclaredField(key);
                FieldUtils.writeDeclaredField(define.entity, key, value, true);
            } catch (Exception ex) {
                log.error("operateDBAction ERR : " + key + " -> " + ex.getCause());
            }
        }

        log.info("# entity = {}", ReflectionToStringBuilder.toString(define.entity));
        return define;
    }

    @PostMapping(value = "/db_save")
    public String saveDBAction(HttpServletRequest request, HttpServletResponse response) {
        try {
            OperateDefine define = this.getOperateDefine(request);

            Object resultObj = define.repository.save(define.entity);

            JSONObject json = JSONUtil.getSuccess(null);
            if (resultObj != null) {
                json.put("entity", resultObj);
            }

            return json.toString();
        } catch (Exception ex) {
            log.error("operateDBAction ERR : " + ex.getMessage(), ex);
            // return JSONUtil.getThrowable(ex).toString();
            return JSONUtil.getThrowableRoot(ex).toString();
        }
    }

    @PostMapping(value = "/db_delete")
    public String deleteDBAction(HttpServletRequest request, HttpServletResponse response) {
        try {
            OperateDefine define = this.getOperateDefine(request);

            String id = request.getParameter("id");

            EntityPrimaryKeySetter entitySetter = new EntityPrimaryKeySetter(define.entity, id);

            define.repository.delete(entitySetter.apply());

            JSONObject json = JSONUtil.getSuccess(null);

            return json.toString();
        } catch (Exception ex) {
            log.error("operateDBAction ERR : " + ex.getMessage(), ex);
            // return JSONUtil.getThrowable(ex).toString();
            return JSONUtil.getThrowableRoot(ex).toString();
        }
    }
}
