/*
 * Copyright (c) 2007. 財團法人資訊工業策進會. All right reserved.
 */
package gtu.itext.iisi.table;

import gtu.itext.iisi.table.GroupingInfo.GroupLevel;
import gtu.itext.iisi.table.func.GroupingFunctionProcessor;

import java.util.BitSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * normal Table 統計列處理器
 *
 * @author 920111 
 *         在 2008/3/20 建立
 */
public class GroupingRowProcessor {

    /**
     * 統計列處理過程資訊. 
     */
    public class ProcessInfo {

        GroupingFunctionProcessor[] funcProcessors;

        /**
         * 
         */
        public ProcessInfo(ColumnMetadata[] metadatas) {

            this.funcProcessors = new GroupingFunctionProcessor[metadatas.length];
            for (int j = 0; j < metadatas.length; j++) {
                final ColumnMetadata metadata = metadatas[j];
                if (metadata.getGroupFunction() != null) {
                    this.funcProcessors[j] = metadata.getGroupFunction().create();
                }
            }

        }

        /**
         * 分組資訊，如「專案名稱」.
         * 前次處理值. 
         */
        private Object previousValue = null;

        /**
         * 分組資訊，如「專案名稱」.
         * 目前處理值. 
         */
        private Object nowValue = null;

        /** 此分組適用範圍最初列號. */
        int firstRow;

        /** 此分組適用範圍最後列號. */
        int lastRow;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
        }

        /**
         * @return 傳回 firstRow。
         */
        public int getFirstRow() {
            return this.firstRow;
        }

        /**
         * @return 傳回 lastRow。
         */
        public int getLastRow() {
            return this.lastRow;
        }

        /**
         * @return 傳回 previousValue。
         */
        public Object getPreviousValue() {
            return this.previousValue;
        }

        /**
         * 清空現有資訊.
         * @param nowData 現有資料列資料值, 若為總計列, 則輸入為 null
         */
        void clear(Object[] nowData) {
            this.firstRow = 0;
            this.lastRow = 0;
            this.previousValue = null;

            if (nowData != null) {
                for (int i = 0; i < this.funcProcessors.length; i++) {
                    if (this.funcProcessors[i] != null) {
                        this.funcProcessors[i].clear();
                        this.funcProcessors[i].addItem(nowData[i]);
                    }
                }
            } else {
                for (int i = 0; i < this.funcProcessors.length; i++) {
                    if (this.funcProcessors[i] != null) {
                        this.funcProcessors[i].clear();
                    }
                }

            }

        }

        /**
         * @param j
         * @return
         */
        public GroupingFunctionProcessor getFuncProcessor(int j) {
            if (j < 0 || j >= this.funcProcessors.length) {
                return null;
            }
            return this.funcProcessors[j];
        }
    }

    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    private AbstractNormalTableTransfer tableTransfer;

    /** 分組描述資訊. */
    private GroupingInfo groupingInfo;

    /** 前次處理時資料列相關內容. */
    private ProcessInfo[] processInfos;

    /** 對應欄位代碼 */
    private int[] columnMappingIndex;

    /** 對應欄位代碼 */
    private BitSet columnMappingSet = new BitSet();

    //====
    //== [instance variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop 
    //================================================
    //== [Constructors] Block Start (含init method)
    //====

    /**
     * @param groupInfo
     */
    public GroupingRowProcessor(AbstractNormalTableTransfer tableTransfer, ColumnMetadata[] metadatas, GroupingInfo groupingInfo) {
        super();
        // 1. 相依資訊設定
        this.tableTransfer = tableTransfer; //	| 相關表格轉換器.
        this.groupingInfo = groupingInfo; //	| 相關分組資訊.

        // 2. 決定 groupInfo 所使用的 column 在 metadatas 中的對應索引.
        if (groupingInfo != null) {
            final GroupLevel[] groups = this.groupingInfo.getGroups(); // 	| a.實際統計層級內容. 
            this.processInfos = new ProcessInfo[groups.length]; //			| b.建立"總計"層級，所使用的「統計列處理過程資訊」.
            this.processInfos[0] = new ProcessInfo(metadatas); //			|
            this.processInfos[0].previousValue = ""; //						|
            this.columnMappingIndex = new int[groups.length]; //			| c.對應 group 層級所參考的 Column Index.
            for (int i = 0; i < groups.length; i++) { //					|	=>逐一找出並記錄在metadatas中的對應索引，
                GroupingInfo.GroupLevel g = groups[i]; //					|
                final int theIndex = ArrayUtils.indexOf(metadatas, g.dataColumn);
                this.columnMappingIndex[i] = theIndex;
                if (theIndex >= 0) {
                    this.columnMappingSet.set(theIndex);
                }
            }
        }
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====
    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====

    /** 指定欄號是否用於進行分組設定. */
    public boolean isGroupingColumn(int index) {
        return this.columnMappingSet.get(index);
    }

    //####################################################################
    //## [Method] sub-block : XXXX(可依功能區分) 
    //####################################################################

    /**
     * 處理第一列號設定.
     * 
     * @param metadatas
     * @param nowData
     */
    public void doFirstRow() {
        if (ArrayUtils.getLength(this.processInfos) > 0) {
            this.processInfos[0].firstRow = this.tableTransfer.getLastRowNum() + 1;
        }
    }

    /**
     * 處理最末列時，列號的更新.
     * 
     * @param metadatas
     * @param nowData
     */
    public void doFinalRow(ColumnMetadata[] metadatas, Object[] nowData) {
        if (this.groupingInfo == null) { // 決定是否輸出中間統計資料.
            return;
        }
        final GroupLevel[] groups = this.groupingInfo.getGroups();
        for (int i = groups.length - 1; i >= 0; i--) {
            final int theIndex = this.columnMappingIndex[i];
            if (theIndex >= 0) {
                final ProcessInfo processInfo = this.processInfos[i];
                processInfo.lastRow = this.tableTransfer.getLastRowNum();
            }
        }
    }

    /**
     * 決定輸出統計資料列層級.
     * 
     * Ex: group By  P_CODE  //1
     *               C_CODE  //2
     *               U_CODE  //3
     * 
     * 回傳  3 時，應輸出 U_CODE小計
     * 回傳  2 時，應輸出 U_CODE小計、C_CODE小計
     * 回傳  1 時，應輸出 U_CODE小計、C_CODE小計、P_CODE小計
     * 回傳 -1 時，不做統計列處理
     * 
     * 回傳 0 ，不應發生
     * 
     * @param metadatas
     * @param nowData
     * @return 1~n
     */
    public int decideGroupLv(ColumnMetadata[] metadatas, Object[] nowData) {
        //
        // 0. 前置判斷及資料準備.
        //
        if (this.groupingInfo == null) { // 決定是否輸出中間統計資料.
            return -1;
        }
        final GroupLevel[] groups = this.groupingInfo.getGroups();
        int maxLv = -1;
        //System.out.println(StringUtils.join(nowData, ","));
        //
        // 1. 依序檢查分組原則，是否對應值已於上次不同(表示應輸出統計列)
        //
        for (int i = groups.length - 1; i > 0; i--) {
            final int theIndex = this.columnMappingIndex[i];
            if (theIndex >= 0) { // 指定分組標的，有對應 ColumnMetadata 欄位，才進入處理.
                // final ColumnMetadata nowCM = metadatas[theIndex];

                final Object nowValue = nowData[theIndex];
                ProcessInfo processInfo = this.processInfos[i];

                if (processInfo != null) {

                    // 1-1. 一律重設現有最未列位置.
                    //    	因為此時 this.tableTransfer 尚未新增列，
                    //		所以目前 this.tableTransfer.getLastRowNum() 指向的是上一列的列號.
                    processInfo.nowValue = nowValue; // 現有資料
                    processInfo.lastRow = this.tableTransfer.getLastRowNum();
                    // 1-2. 當分組標的對應值不同時. 
                    //		設定應處理分組資訊輸出的最大層級.
                    // 		1-2-1. 暫不異動 processInfo 資訊， 
                    // 		1-2-2. 等輸出統計列完成後，再另外呼叫 clear()
                    if (!ObjectUtils.equals(nowValue, processInfo.previousValue)) {
                        maxLv = i;
                    }
                } else {
                    // 首次處理此分類群組資訊，
                    // 因為實際資料列仍未輸出，所以getLastRowNum()值為上一行的index
                    // 但起始資料應為本列, 故加1.
                    //		a. 記錄起始結束列.
                    //		b. 記錄比較基準值.
                    //System.out.println("現在首次處理.....  : " + nowValue + " at 列:" + this.tableTransfer.getLastRowNum());
                    processInfo = new ProcessInfo(metadatas);
                    processInfo.firstRow = this.tableTransfer.getLastRowNum() + 1;
                    processInfo.lastRow = this.tableTransfer.getLastRowNum() + 1;
                    processInfo.previousValue = nowValue;
                    this.processInfos[i] = processInfo;
                }
                //
                //System.out.println( this.tableTransfer.getLastRowNum() );
                // 
                if (maxLv == -1 || maxLv > i) { // 本列資料對於 groupLV[i], 應列入加總
                    // 處理實際公式計算. 
                    for (int j = 0; j < metadatas.length; j++) {
                        final GroupingFunctionProcessor funcProcessor = processInfo.getFuncProcessor(j);
                        if (funcProcessor != null) {
                            funcProcessor.addItem(nowData[j]);
                        }
                    }
                }

            }
        }
        // 2. 總計數據處理 ( 處理實際公式計算 ).
        {
            ProcessInfo processInfo = this.processInfos[0];
            for (int j = 0; j < metadatas.length; j++) {
                final GroupingFunctionProcessor funcProcessor = processInfo.getFuncProcessor(j);
                if (funcProcessor != null) {
                    funcProcessor.addItem(nowData[j]);
                }
            }
        }

        return maxLv;
    }

    /**
     * 
     */
    public ProcessInfo getProcessInfo(int i) {
        if (i >= 0 && i < this.processInfos.length) {
            return this.processInfos[i];
        }
        return null;
    }

    /**
     * 
     * @param nowData 現有資料列資料值, 若為總計列, 則輸入為 null
     */
    public void clearProcessInfo(int i, Object[] nowData) {
        if (i >= 0 && i < this.processInfos.length) {
            //System.out.println("清理: " + this.processInfos[i]);
            ProcessInfo pi = this.processInfos[i];
            if (pi != null) {
                pi.clear(nowData); // 先清除 (含內部統計值計算器), 再設定相關新值.
                pi.previousValue = pi.nowValue; // previousValue、pi.nowValue 皆為比較基準，如單位名稱等等...
                pi.firstRow = this.tableTransfer.getLastRowNum() + 1; // 已輸出統計列完成. 設定後續統計列起始位置. 
                pi.lastRow = this.tableTransfer.getLastRowNum() + 1;
            }
        }
    }

    //====
    //== [Method] Block Stop 
    //================================================
    //== [Main Method] Block Start
    //====
    //public static void main(String[] args) {
    //  
    //}
    //====
    //== [Main Method] Block Stop 
    //================================================

}
