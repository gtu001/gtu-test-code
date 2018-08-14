package com.gtu.example.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.example.common.JSONUtil;
import com.gtu.example.common.JqBootgridHandler.JqBootgridColumnDef;
import com.gtu.example.common.JqBootgridHandler.JqBootgridModelHandler;
import com.gtu.example.common.JqBootgridHandler.JqBootgridRequestInterpreter;
import com.gtu.example.springdata.vo.ProductInfo;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/jq_bootgrid_test/")
public class JqBootgridTestController {

    private static final Logger log = LoggerFactory.getLogger(JqBootgridTestController.class);

    @Autowired
    private ConfigurableApplicationContext ctx;

    @GetMapping(value = "/jq_bootgrid")
    public ModelAndView test_table_001() {
        ModelAndView model = new ModelAndView();
        model.setViewName("jq_bootgrid");
        return model;
    }

    @GetMapping(value = "/dashboard/template")
    public String getDashboardMaterialedView() {
        log.info("#- getDashboardMaterialedView start");
        try {
            String jsonPath = "com/gtu/example/springdata/vo/template_001.json";
            URL url = this.getClass().getClassLoader().getResource(jsonPath);
            String jsonStr = IOUtils.toString(url);

            ObjectMapper mapper = new ObjectMapper();
            ProductInfo productInfo = mapper.readValue(jsonStr, ProductInfo.class);
            log.info("" + ReflectionToStringBuilder.toString(productInfo, ToStringStyle.MULTI_LINE_STYLE));

            List<Pair<String, String>> columns = productInfo.getWorkUnits().stream().map(workUnit -> Pair.of(workUnit.getWorkUnit(), workUnit.getName())).collect(Collectors.toList());
            List<JqBootgridColumnDef> defLst = JqBootgridModelHandler.getColumnDef(columns);

            return JSONArray.fromObject(defLst).toString();
        } catch (Exception e) {
            return JSONUtil.getThrowable(e).toString();
        }
    }

    @PostMapping(value = "/get_model")
    public String getModel(HttpServletRequest request) {
        log.info("#- getModel start");
        try {
            JqBootgridRequestInterpreter interpreter = new JqBootgridRequestInterpreter(request);
            Map<String, String> searchPhrase = interpreter.getSearchPhrase();
            int rowCount = interpreter.getRowCount();
            String current = interpreter.getCurrent();
            Pair<String, String> sort = interpreter.getSort();

            // JqBootgridModelHandler jqBootgridModel = new
            // JqBootgridModelHandler(lst, TestBean.class, null);
            // return jqBootgridModel.getModel().toString();
            return "";
        } catch (Exception ex) {
            return JSONUtil.getThrowable(ex).toString();
        } finally {
            log.info("#- getModel end");
        }
    }
}
