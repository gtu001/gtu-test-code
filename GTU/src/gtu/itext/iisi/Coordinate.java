package gtu.itext.iisi;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import com.lowagie.text.Rectangle;

public abstract class Coordinate {

    /** 轉換長度 由自訂單位 到 iText 中的 pixel. */
    public abstract float trans(float length);

    /** 反轉長度 由iText 中的 pixel 到 自訂單位. */
    public abstract float reverseTrans(float length);

    /** 轉換長度(高). */
    public float transHeight(float length) {
        return this.trans(length);
    }

    /** 轉換長度(寬). */
    public float transWidth(float length) {
        return this.trans(length);
    }

    /** 轉換座標系統. */
    public Float transPosition(DocumentUtils docUtils, Rectangle pageSize, float x, float y) {
        return transPosition(pageSize, x, y);
    }

    /** 轉換座標系統. */
    public Point2D.Float transPosition(Rectangle pageSize, float x, float y) {
        return transPosition(pageSize.getWidth(), pageSize.getHeight(), x, y);
    }

    /** 轉換座標系統. */
    protected abstract Point2D.Float transPosition(float w, float h, float x, float y);

    /**
     * 轉換 左上角 為準之座標系統(單位CM) 至 iText內定之左下角為準的座標系統.
     */
    public final static Coordinate TOP_LEFT_CM = new Coordinate() {
        @Override
        public Point2D.Float transPosition(float w, float h, float x, float y) {
            float ny = h - (y * 28.34f);
            return new Point2D.Float(x * 28.34f, ny);
        }

        @Override
        public float trans(float length) {
            return length * 28.34f;
        }

        @Override
        public float reverseTrans(float length) {
            return length / 28.34f;
        }
    };

    /**
     * 轉換(單位CM) 至 iText內定之左下角為準的座標系統.
     */
    public final static Coordinate CM = new Coordinate() {
        @Override
        public Point2D.Float transPosition(float w, float h, float x, float y) {
            return new Point2D.Float(x * 28.34f, y * 28.34f);
        }

        @Override
        public float trans(float length) {
            return length * 28.34f;
        }

        @Override
        public float reverseTrans(float length) {
            return length / 28.34f;
        }
    };

    /**
     * iText內定之左下角為準的座標系統.
     */
    public final static Coordinate Default = new Coordinate() {
        @Override
        public Point2D.Float transPosition(float w, float h, float x, float y) {
            return new Point2D.Float(x, y);
        }

        @Override
        public float trans(float length) {
            return length;
        }

        @Override
        public float reverseTrans(float length) {
            return length;
        }
    };

    /**
     * COGNOS 座標系 頁面在第四象限.
     */
    public final static Coordinate COGNOS = new Coordinate() {

        @Override
        public float trans(float length) {
            return length;
        }

        @Override
        public float reverseTrans(float length) {
            return length;
        }

        @Override
        protected Float transPosition(float w, float h, float x, float y) {
            return new Point2D.Float(x, y - h);
        }
    };

    /**
     * COGNOS 座標系 頁面在第四象限.
     */
    public final static Coordinate COGNOS_90 = new Coordinate() {

        @Override
        public float trans(float length) {
            return length;
        }

        @Override
        public float reverseTrans(float length) {
            return length;
        }

        @Override
        protected Float transPosition(float w, float h, float x, float y) {
            // 轉 90 度
            return new Point2D.Float(x, y - w);
        }
    };

}
