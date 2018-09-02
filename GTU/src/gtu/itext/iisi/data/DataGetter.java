package gtu.itext.iisi.data;

import gtu.itext.iisi.table.ColumnMetadata;

public interface DataGetter {

	public boolean validateDataType(Object data);

	/**
	 * 取得單列中的所有指定資料內容.
	 * 
	 * @param data
	 *            資料列.
	 * @param colsMeta
	 *            資料欄描述資訊.
	 * @param nowData
	 *            取得的資料內容將會放於此陣列中傳回.
	 * @return true, 資料非空, false, 此列中不包含任何資料或是資料格式錯誤.
	 */
	public boolean getRowData(Object bean, ColumnMetadata[] colsMeta, Object[] nowData);
}
