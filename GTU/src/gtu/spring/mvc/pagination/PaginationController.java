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
            PaginationBean pageInfo = rtnPage.getRight();
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

    public static class PaginationBean implements Serializable {

        private static final int PAGE_SIZE_DEFAULT = 10;

        private static final long serialVersionUID = -713225282325263703L;
        private String pageNo;// 第幾頁
        private String pageSize;// 一頁幾筆
        private int totalCount;// 總比數

        private int getSize(List<?> lst) {
            if (lst == null || lst.isEmpty()) {
                return 0;
            }
            return lst.size();
        }

        public PaginationBean() {
            this.totalCount = 0;
            this.pageNo = "1";
            this.pageSize = String.valueOf(PaginationBean.PAGE_SIZE_DEFAULT);
        }

        public String getPaginationJSON() {
            Map<String, Object> pagObj = new HashMap<String, Object>();
            List<String> pageLst = new ArrayList<String>();
            int tmpTotalCount = this.totalCount;
            for (int ii = 0; ii < tmpTotalCount; ii++) {
                pageLst.add(String.valueOf(ii + 1));
            }
            pagObj.put("dataSource", pageLst);
            pagObj.put("pageNumber", pageNo);
            pagObj.put("pageSize", pageSize);
            pagObj.put("pageRange", 3);
            pagObj.put("showPrevious", true);
            pagObj.put("showNext", true);
            pagObj.put("showPageNumbers", true);
            pagObj.put("showNavigator", true);
            pagObj.put("showGoInput", true);
            pagObj.put("showGoButton", true);
            pagObj.put("showFirstOnEllipsisShow", true);
            pagObj.put("showLastOnEllipsisShow", true);
            pagObj.put("autoHidePrevious", false);
            pagObj.put("autoHideNext", false);
            pagObj.put("prevText", "上一頁");
            pagObj.put("nextText", "下一頁");
            pagObj.put("ellipsisText", "...");
            pagObj.put("goButtonText", "前往");
            return JsonUtil_forWeb.toJsonStr(pagObj);
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        @Override
        public String toString() {
            return "FunQryRoiPageInfo [pageNo=" + pageNo + ", pageSize=" + pageSize + ", totalCount=" + totalCount + ", maxCount=" + maxCount + "]";
        }
    }
}