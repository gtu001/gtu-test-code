package gtu.itext.iisi.data;

public class CellConst implements CellDataSource {

    static class Counter {
        final int start;

        int c;

        /**
         * @param c
         */
        private Counter(int start) {
            super();
            this.start = start;
            this.c = start;
        }

        void add() {
            this.c++;
        }

        @Override
        public String toString() {
            return String.valueOf(this.c);
        }

        /**
         * 
         */
        public void reset() {
            this.c = this.start;
        }
    }

    protected Object defaultValue;

    /**
     * Null Value
     */
    public CellConst() {

    }

    /**
     * User-Defined
     * 
     * @param defaultValue
     */
    public CellConst(Object defaultValue) {
        super();
        this.defaultValue = defaultValue;
    }

    /**
     * 建立流水號欄位.
     */
    final static public CellConst newCounter(int startNo) {
        CellConst c = new CellConst(new Counter(startNo - 1)) {

            /**
             * @see tw.gov.moi.ae.report.itext2.data.CellConst#reset()
             */
            @Override
            public void reset() {
                ((Counter) this.defaultValue).reset();
            }

            @Override
            public Object eval(Object dataObj) {
                ((Counter) this.defaultValue).add();
                return super.eval(dataObj);
            }
        };
        return c;
    }

    @Override
    public void reset() {
    };

    @Override
    public Object eval(Object dataObj) {
        return this.defaultValue;
    }
}
