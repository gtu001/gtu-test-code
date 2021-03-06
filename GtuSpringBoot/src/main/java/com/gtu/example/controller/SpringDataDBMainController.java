package com.gtu.example.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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

import com.gtu.example.common.DBRelationHelper;
import com.gtu.example.common.JSONUtil;
import com.gtu.example.common.JqGridHandler;
import com.gtu.example.common.JqGridHandler.EntityPrimaryKeySetter;
import com.gtu.example.common.JqGridHandler.JqReader;
import com.gtu.example.common.JqGridHandler.SimpleJdGridCreater;
import com.gtu.example.common.JqGridHandler.SubgridHandler;
import com.gtu.example.common.PackageReflectionUtil;
import com.gtu.example.common.RelationEntityRowHandler;
import com.gtu.example.common.RepositoryReflectionUtil;
import com.gtu.example.springdata.entity.Address;
import com.gtu.example.springdata.entity.Car;
import com.gtu.example.springdata.entity.Employee;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/springdata-dbMain/")
public class SpringDataDBMainController {

    private static final Logger log = LoggerFactory.getLogger(SpringDataDBMainController.class);

    @GetMapping(value = "/test001")
    @Transactional
    public String test_____001() {
        Employee employee = new Employee();
        List<Car> cars = new ArrayList<>();
        Car car = new Car();
        car.setBrand("1111");
        car.setEmployee(employee);
        cars.add(car);
        employee.setCars(cars);

        employee.setDescription("3333");
        employee.setFirstName("fffff");
        employee.setLastName("llll");

        Address addr = new Address();
        addr.setAddressId("addr");
        addr.setCity("city");
        addr.setRoad("road");
        // employee.setAddress(addr);

        entityManager.persist(employee);
        return "done...";
    }

    @Autowired
    private ConfigurableApplicationContext ctx;
    @Autowired
    // @Qualifier("serversEntityManager")
    private EntityManager entityManager;

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "/tables")
    public String tables() {
        Set<String> tabLst = DBRelationHelper.getAllTables_str();
        return JSONArray.fromObject(tabLst).toString();
    }

    @GetMapping(value = "/dbMain")
    public ModelAndView dbMain() {
        ModelAndView model = new ModelAndView();
        model.setViewName("db_process");
        return model;
    }

    @GetMapping(value = "/table-get-columns")
    public String tableColumns(@RequestParam(name = "table") String table) {
        Optional<List<String>> obj = DBRelationHelper.getAllTables().stream()//
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
        Map<Class<Object>, Class[]> repositoryEntityMap = DBRelationHelper.getAllRepostiriesMap();

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

    @PostMapping(value = "/db_simple_query/{type}")
    public String queryDBAction(@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("# queryDBAction start ..");
            OperateDefine define = this.getOperateDefine(request);

            SimpleJdGridCreater handler = new SimpleJdGridCreater(define.entityClz, false);
            if ("colModel".equals(type)) {
                return handler.getColModel().toString();
            } else {
                List<?> lst = (List<?>) define.repository.findAll();
                log.info("size = {}", lst.size());

                handler.getColModel();
                JqReader reader = handler.getSimpleJqReader(lst, null, 1, lst.size());
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
                JqGridHandler.setFieldToEntity(define.entityClz, define.entity, key, value);
            } catch (Exception ex) {
                if (define.entity.getClass().getSuperclass().isAnnotationPresent(MappedSuperclass.class)) {
                    Class<?> parentClz = define.entity.getClass().getSuperclass();
                    try {
                        JqGridHandler.setFieldToEntity(parentClz, define.entity, key, value);
                    } catch (Exception ex2) {
                        log.error("operateDBAction ERR : " + key + " -> " + ex2.getCause());
                    }
                } else {
                    log.error("operateDBAction ERR : " + key + " -> " + ex.getCause());
                }
            }
        }

        log.info("# entity = {}", ReflectionToStringBuilder.toString(define.entity));
        return define;
    }

    @PostMapping(value = "/db_save")
    public String saveDBAction(HttpServletRequest request, HttpServletResponse response) {
        log.info("## saveDBAction ..start");
        try {
            OperateDefine define = this.getOperateDefine(request);

            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            // 處理客製關聯塞直
            DBRelationHelper relationHelper = new DBRelationHelper(define.entity, ctx);
            relationHelper.relationProcess();
            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑

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
        } finally {
            log.info("## saveDBAction ..end");
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

    @PostMapping(value = "/query_relation")
    public String queryRelation(HttpServletRequest request, HttpServletResponse response) {
        try {
            String rowId = request.getParameter("rowId");
            String[] pks = null;
            if (StringUtils.isNotBlank(rowId)) {
                pks = rowId.split("^", -1);
            }

            OperateDefine define = this.getOperateDefine(request);
            Object entity = RepositoryReflectionUtil.findById(define.repositoryClz, define.repository, pks[0]);

            JSONObject json = JSONUtil.getSuccess(null);
            JSONArray arry = new JSONArray();

            for (Field f : entity.getClass().getDeclaredFields()) {
                if (SimpleJdGridCreater.isRelationField(f)) {
                    SubgridHandler subgrid = new SubgridHandler(entity, f.getName(), entityManager);
                    arry.add(subgrid);
                }
            }
            json.put("subgrid", arry);
            return json.toString();
        } catch (Exception ex) {
            log.error("operateDBAction ERR : " + ex.getMessage(), ex);
            // return JSONUtil.getThrowable(ex).toString();
            return JSONUtil.getThrowableRoot(ex).toString();
        }
    }

    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        for (Enumeration<?> enu = request.getParameterNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    @PostMapping(value = "/relation_detail_process")
    @Transactional
    public String relationDetailProcess(HttpServletRequest request, HttpServletResponse response) {
        log.info("[WARN] relationDetailProcess start");
        try {
            String rowId = request.getParameter("rowId");
            String detailId = request.getParameter("id");
            String fieldName = request.getParameter("fieldName");
            String oper = request.getParameter("oper");
            String[] pks = null;
            if (StringUtils.isNotBlank(rowId)) {
                pks = rowId.split("^", -1);
            }

            OperateDefine define = this.getOperateDefine(request);
            Object entity = RepositoryReflectionUtil.findById(define.repositoryClz, define.repository, pks[0]);

            Map<String, Object> valueMap = getParameterMap(request);
            RelationEntityRowHandler relationHandler = new RelationEntityRowHandler(define.entityClz, entity, fieldName);

            switch (oper) {
            case "add":
                relationHandler.insert(valueMap);
                break;
            case "edit":
                relationHandler.update(valueMap, detailId);
                break;
            case "del":
                relationHandler.delete(detailId);
                break;
            default:
                throw new Exception("未定義 oper : " + oper);
                // break;
            }

            Object resultObj = define.repository.save(entity);
            // entityManager.persist(entity);

            JSONObject json = JSONUtil.getSuccess("ok");
            // if (resultObj != null) {
            // json.put("entity", resultObj);
            // }
            return json.toString();
        } catch (Exception ex) {
            log.error("operateDBAction ERR : " + ex.getMessage(), ex);
            // return JSONUtil.getThrowable(ex).toString();
            return JSONUtil.getThrowableRoot(ex).toString();
        } finally {
            log.info("[WARN] relationDetailProcess end");
        }
    }
}
