package gtu.spring.rest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class SpringRestTest001 {

    /**
     * 查詢 ETF 詳細資訊頁面
     */
    @RequestMapping(value = "/qryForignEtfDtl", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<FundForignEtfDtlWebResponseDto> qryForignEtfDtl(@ModelAttribute(value = "fundForignEtfDtlWebRequestDto") FundForignEtfDtlWebRequestDto webRequestDto, ModelMap modelMap) {
        logger.debug("in qryForignEtfDtl, webRequestDto = {}", ReflectionToStringBuilder.toString(webRequestDto));
        if (webRequestDto != null) {

            // set ui model
            return new ResponseEntity<FundForignEtfDtlWebResponseDto>(webResponseDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
