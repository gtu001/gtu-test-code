package gtu.spring.mvc.common;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "loginDomain", "roiEntity" })
@RequestMapping("/fund")
public class SpringMvcController {
    
    @Value("${app.login.domain}")
    private String loginDomain;

    @ModelAttribute("loginDomain")
    public String initLoginDomain() {
        logger.debug("in initLoginDomain, loginDomain = {}", loginDomain);

        return loginDomain;
    }

        // html 換頁
    @RequestMapping(value = "/qryFundCustomApplyDetail", method = { RequestMethod.GET, RequestMethod.POST })
    public String qryCustomApplyDetail(
            @ModelAttribute(value = "fundCustomApplyDetailWebRequestDto") FundCustomApplyDetailWebRequestDto webRequestDto,
            @RequestParam(name = "lastestQueryJSON") String lastestQueryJSON,
            Model model) {

            // do your stuff here...

        // set ui model
        model.addAttribute("entity", webResponseDto);
        return "fund/fundCustomApplyDetail";
    }
    
    // 客戶申請書查詢 畫面
    @RequestMapping(value = "/qryFundCustomApplyDetail", method = { RequestMethod.GET, RequestMethod.POST })
    public String qryCustomApplyDetail(
            @ModelAttribute(value = "fundCustomApplyDetailWebRequestDto") FundCustomApplyDetailWebRequestDto webRequestDto,
            @RequestParam(name = "lastestQueryJSON", required = false) String lastestQueryJSON,
            @RequestParam(name = "lastestQueryURL", required = false) String lastestQueryURL,
            Model model) {
        logger.info("form {} ", lastestQueryJSON);
        // set ui model
        model.addAttribute("entity", webResponseDto);
        model.addAttribute("lastestQueryJSON", lastestQueryJSON);
        model.addAttribute("lastestQueryURL", lastestQueryURL);
        return "fund/fundCustomApplyDetail";
    }


    @RequestMapping(value = "/qryForignEtfDtl", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<FundForignEtfDtlWebResponseDto> qryForignEtfDtl(
            @ModelAttribute(value = "fundForignEtfDtlWebRequestDto") FundForignEtfDtlWebRequestDto webRequestDto,
            ModelMap modelMap) {
        logger.debug("in qryForignEtfDtl, webRequestDto = {}", ReflectionToStringBuilder.toString(webRequestDto));
        if (webRequestDto != null) {

            // set ui model
            return new ResponseEntity<FundForignEtfDtlWebResponseDto>(webResponseDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/qryRoiCurrencySummary", method = { RequestMethod.POST })
    public String qryRoiCurrencySummary(ModelMap modelMap) {
        logger.debug("roiEntity = {}", modelMap.get("roiEntity"));

        return "fund/fundQryRoiCurrencySummary";
    }


    @RequestMapping(value = "/qryTrustAcct/{trustAcct}/{shoreTypeReq}", method = { RequestMethod.GET })
    public String qryTrustAcct(@PathVariable String trustAcct, @PathVariable String shoreTypeReq, Model model) {
        FundQryTrustAcctWebResponseDto webResponseDto = this.queryQryTrustAcctDataByRest(trustAcct, shoreTypeReq);
        // set ui model
        model.addAttribute("entity", webResponseDto);

        return "fund/fundQryTrustAcct";
    }
}