package gtu.itext.iisi.data;

import gtu.itext.iisi.table.ColumnMetadata;

public class BeanDataGetter implements DataGetter {

	private final static BeanDataGetter instance = new BeanDataGetter();

	private BeanDataGetter() {
	}

	static public BeanDataGetter getInstance() {
		return BeanDataGetter.instance;
	};

	@Override
    public boolean validateDataType(Object data) {
		return true;
	};

	@Override
    public boolean getRowData(Object bean, ColumnMetadata[] colsMeta, Object[] nowData) {
		int blankCount = 0;
		int colsNumber = colsMeta.length;
		for (int i = 0; i < colsNumber; i++) {

			final Object value = colsMeta[i].getField().eval(bean);
			nowData[i] = value;
			if (nowData[i] == null) {
				blankCount++;
			}

		}
		return (blankCount != colsNumber);
	}
}
