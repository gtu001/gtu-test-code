import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gtu.json.JsonUtil_forWeb;

/**
 * 套件網址 : http://pagination.js.org/
 */
public class PaginationController {

    private static final Logger logger = Logger.getLogger(PaginationController.class);

    @RequestMapping(value = "/qryRoi", method = { RequestMethod.GET, RequestMethod.POST })
    public String qryRoi(@ModelAttribute FundQryRoiWebRequestDto webRequestDto, Model model) {
        logger.debug("in qryRoi, webRequestDto = {}", webRequestDto);

        if (webRequestDto.getProductTypeReq() != null) {
            // 加入分頁處理----------------------------------------------↓↓↓↓↓↓
            Pair<FundQryRoiWebResponseDto, PaginationBean> rtnPage = fundQryRoiWebRequestDtoPageDataHolder.getPageContent(webRequestDto,
                    new Function<FundQryRoiWebRequestDto, FundQryRoiWebResponseDto>() {
                        @Override
                        public FundQryRoiWebResponseDto apply(FundQryRoiWebRequestDto t) {
                            // 處理webRequestDto
                            RestRequestDto restRequestDto = fundQryRoiProcessor.generateRestRequestDto(webRequestDto);

                            // 發送rest request
                            FundQryRoiResponseDto restResponseDto = (FundQryRoiResponseDto) invfQueryService.queryFundDataByRest(restRequestDto, FundQryRoiResponseDto.class);

                            // 處理 rest response
                            FundQryRoiWebResponseDto webResponseDto = (FundQryRoiWebResponseDto) fundQryRoiProcessor.generateWebResponseDto(restResponseDto);

                            return webResponseDto;
                        }
                    });
            // 加入分頁處理----------------------------------------------↑↑↑↑↑↑
            // set ui model

            FundQryRoiWebResponseDto webResponseDto = rtnPage.getLeft();

            // 設定總比數
            PaginationInfo pageInfo = rtnPage.getRight();
            webRequestDto.getFunQryRoiPageInfo().setTotalCount(pageInfo.getTotalCount());

            model.addAttribute("roiEntity", webResponseDto);
            model.addAttribute("paginationAreaJSON", webRequestDto.getFunQryRoiPageInfo().getPaginationJSON());
        } else {
            webRequestDto.initParams();

            model.addAttribute("roiEntity", new FundQryRoiWebResponseDto());
            model.addAttribute("paginationAreaJSON", webRequestDto.getFunQryRoiPageInfo().getPaginationJSON());
        }
        return FundViewEnum.QRY_ROI.getValue();
    }
}