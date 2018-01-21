package gtu.db.oracle;

import java.util.List;

/**
 * 實現oracle的分頁查詢
 * 
 * @author gtu001
 * @param <T>
 */
public abstract class PageQueryAct<T> {
    final int pageNo;// 第幾頁
    final int pageSize;// 一頁幾筆
    final String sql;// 需要打包的sql
    final long totalCount;// 總比數

    public PageQueryAct(int pageNo, int pageSize, long totalCount, String sql) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sql = sql;
        this.totalCount = totalCount;
    }

    public abstract List<T> doQuery(String wrapSql);

    public List<T> getPageResult() {
        int fromIndex = (pageNo - 1) * pageSize;
        StringBuilder pageSb = new StringBuilder();
        if (pageSize != -1) {
            pageSb.append(String.format("select * from (select row_.*, ROWNUM rownum_ from (%s) row_ )", sql));
            if (fromIndex <= 0) {
                pageSb.append(String.format(" where rownum_ <= %s", pageSize));
            } else {
                pageSb.append(String.format(" where rownum_ > %s", fromIndex));
                pageSb.append(String.format(" and rownum_ <= %s", (fromIndex + pageSize)));
            }
        } else {
            pageSb.append(sql);
        }
        List<T> queryList = doQuery(pageSb.toString());
        return queryList;
    }
    
    public static void main(String[] args){
        System.out.println(getPageSQL("select * from dual", 3, 20));
    }
    
    /**
     * 另一個範例 ##這個範例比較優##
     * @param sql  要包得sql
     * @param pageNo  第幾頁
     * @param recordCountPerPage  一頁幾筆
     * @return
     */
    public static String getPageSQL(String sql, int pageNo, int recordCountPerPage) {
        return "select * from ("//
                + " select rownum as my_rownum, table_a.* from (" + sql + ") table_a "//
                + " where rownum<= " + String.valueOf(pageNo * recordCountPerPage) + " ) where my_rownum > "//
                + String.valueOf((pageNo - 1) * recordCountPerPage);//
    }
}