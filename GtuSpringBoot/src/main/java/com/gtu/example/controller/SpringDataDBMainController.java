package com.gtu.example.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gtu.example.common.JSONUtil;
import com.gtu.example.common.PackageReflectionUtil;
import com.gtu.example.common.RepositoryReflectionUtil;
import com.gtu.example.springdata.entity.Address;
import com.gtu.example.springdata.entity.Employee;
import com.gtu.example.springdata.entity.WorkItem;

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

    @GetMapping(value = "/table-get-columns")
    public String tableColumns(@RequestParam(name = "table") String table) {
        // 不word
        Reflections reflections = new Reflections("com.gtu.example.springdata.entity");
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

        allClasses.add(WorkItem.class);
        allClasses.add(Employee.class);
        allClasses.add(Address.class);

        log.info("<<" + allClasses);

        Optional<List<String>> obj = allClasses.stream()//
                .filter(c -> c.getSimpleName().equalsIgnoreCase(table))//
                .findFirst().map(c -> {
                    return Stream.of(c.getDeclaredFields())//
                            .map(Field::getName)//
                            .collect(Collectors.toList());
                });
        String rtnStr = JSONArray.fromObject(obj.get()).toString();
        log.info("return : {}", rtnStr);
        return rtnStr;
    }

    @GetMapping(value = "/table-get-methodMapping")
    public String tableMethodMapping(@RequestParam(name = "table") String table, //
            @RequestParam(name = "operate") String operate) throws ClassNotFoundException {

        Map<Class<Object>, Class[]> repositoryEntityMap = RepositoryReflectionUtil.getRepositoryEntityMap("com.gtu.example.springdata.dao_1");
        Class<?> entityClz = Class.forName(String.format("com.gtu.example.springdata.entity.%s", table));
        Class<?>[] clz = repositoryEntityMap.get(entityClz);

        Class repository = clz[0];
        Class entityKey = clz[1];

        JSONObject obj = new JSONObject();

        String method = "";
        switch (operate) {
        case "saveOrUpdate":
            method = "save";
        case "update":
            method = "save";
            break;
        case "query":
            method = "TODO";
            break;
        case "delete":
            method = "delete";
            break;
        }

        obj.put("repository", repository.getName());
        obj.put("entityClz", entityClz.getName());
        obj.put("entityKey", entityKey.getName());
        obj.put("method", method);

        log.info("return : {}", obj);
        return obj.toString();
    }

    public static class JsonReader {
        String root = "ttt";
        int page = 1;
        int total = 1;
        List<Object> records = new ArrayList<>();
        boolean repeatitems = true;
        String cell = "cell";
        String id = "id";
        String userdata = "userdata";
        SubGrid subgrid = new SubGrid();

        JsonReader() {
            records.add(new Employee("1", "2", "3"));
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Object> getRecords() {
            return records;
        }

        public void setRecords(List<Object> records) {
            this.records = records;
        }

        public boolean isRepeatitems() {
            return repeatitems;
        }

        public void setRepeatitems(boolean repeatitems) {
            this.repeatitems = repeatitems;
        }

        public String getCell() {
            return cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserdata() {
            return userdata;
        }

        public void setUserdata(String userdata) {
            this.userdata = userdata;
        }

        public SubGrid getSubgrid() {
            return subgrid;
        }

        public void setSubgrid(SubGrid subgrid) {
            this.subgrid = subgrid;
        }
    }

    public static class SubGrid {
        String root;
        boolean repeatitems;
        String cell;

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public boolean isRepeatitems() {
            return repeatitems;
        }

        public void setRepeatitems(boolean repeatitems) {
            this.repeatitems = repeatitems;
        }

        public String getCell() {
            return cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }
    }

    @PostMapping(value = "/db_simple_query")
    public String queryDBAction() {
        return JSONObject.fromObject(new JsonReader()).toString();
    }

    @PostMapping(value = "/db_operate")
    public String operateDBAction(HttpServletRequest request, HttpServletResponse response) {
        try {
            Class repositoryClz = Class.forName(request.getParameter("repository"));
            Class entityClz = Class.forName(request.getParameter("entityClz"));
            Class entityKey = Class.forName(request.getParameter("entityKey"));
            String method = request.getParameter("method");

            CrudRepository repository = (CrudRepository) ctx.getBean(repositoryClz);
            Object entity = PackageReflectionUtil.newInstanceDefault(entityClz, false);
            if (entity == null) {
                throw new RuntimeException("entity is null !!");
            }

            for (Enumeration<String> enu = request.getParameterNames(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                String value = request.getParameter(key);
                log.info("mapping to column : {} - {}", key, value);
                try {
                    entity.getClass().getDeclaredField(key);
                    FieldUtils.writeDeclaredField(entity, key, value, true);
                } catch (Exception ex) {
                    log.error("operateDBAction ERR : " + key + " -> " + ex.getCause());
                }
            }

            log.info("# entity = {}", ReflectionToStringBuilder.toString(entity));

            Object resultObj = null;
            if ("save".equals(method)) {
                log.info("# save !!");
                resultObj = repository.save(entity);
            }

            if (resultObj != null) {
                JSONObject json = JSONUtil.getSuccess(null);
                json.put("entity", resultObj);
                return json.toString();
            } else {
                return "done..";
            }
        } catch (Exception ex) {
            log.error("operateDBAction ERR : " + ex.getMessage(), ex);
            return JSONUtil.getThrowable(ex).toString();
        }
    }
}
