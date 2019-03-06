package gtu.spring.mvc.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cub.inv.query.ui.dto.web.fund.FundQryRoiWebResponseDto;
import cub.inv.query.ui.dto.web.util.JsonUtil_forWeb;

public class PaginationInfo implements Serializable {

    public static final int PAGE_SIZE_DEFAULT = 10;
    public static final String EMPTY_JSON = null;

    private static final Logger logger = LoggerFactory.getLogger(PaginationInfo.class);
    private static final long serialVersionUID = -713225282325263703L;
    private int pageNo;
    private int pageSize;
    private int totalCount;

    public PaginationInfo() {
        this.totalCount = 0;
        this.pageNo = 1;
        this.pageSize = PAGE_SIZE_DEFAULT;
    }

    public PaginationInfo(int pageNo, int pageSize, int totalCount) {
        this.totalCount = totalCount;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}