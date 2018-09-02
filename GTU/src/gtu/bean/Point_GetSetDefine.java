package gtu.bean;
import java.beans.ConstructorProperties;

public class Point_GetSetDefine {
    private double x, y;

    public Point_GetSetDefine() {
    }

    @ConstructorProperties({ "x", "y" })
    public Point_GetSetDefine(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
