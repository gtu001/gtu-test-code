package gtu.bean;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.beans.SimpleBeanInfo;

public class ChartBeanBeaInfo extends SimpleBeanInfo {

    static class ChartBean {
        private int titlePosition;
        private boolean inverse;

        public int getTitlePosition() {
            return titlePosition;
        }

        public void setTitlePosition(int titlePosition) {
            this.titlePosition = titlePosition;
        }

        public boolean isInverse() {
            return inverse;
        }

        public void setInverse(boolean inverse) {
            this.inverse = inverse;
        }
    }

    static class TitlePositionEditor extends PropertyEditorSupport {
        private String[] options = { "Left", "Center", "Right" };

        public String[] getTags() {
            return options;
        }

        public String getAsText() {
            int value = (Integer) getValue();
            return options[value];
        }

        public void setAsText(String s) {
            for (int ii = 0; ii < options.length; ii++) {
                if (options[ii].equals(s)) {
                    setValue(ii);
                    return;
                }
            }
        }
    }

    static class InverseEditor extends PropertyEditorSupport {
        private String[] options = { "TRUE", "FALSE" };

        public String[] getTags() {
            return options;
        }

        public String getAsText() {
            boolean value = (Boolean) getValue();
            if (value) {
                return options[0];
            }
            return options[1];
        }

        public void setAsText(String s) {
            if (s.equalsIgnoreCase(options[0])) {
                setValue(true);
            } else {
                setValue(false);
            }
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor titlePositionDesc = new PropertyDescriptor("titlePosition", ChartBean.class);
            titlePositionDesc.setPropertyEditorClass(TitlePositionEditor.class);
            PropertyDescriptor inverseDesc = new PropertyDescriptor("titlePosition", ChartBean.class);
            inverseDesc.setPropertyEditorClass(InverseEditor.class);
            return new PropertyDescriptor[] { titlePositionDesc, inverseDesc };
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
