package gtu.sort;
public class CompareTwoValue {

    public static <T> boolean eq(T s1, T s2) {
        return s1 != null ? s1.equals(s2) : s2 == null;
    }
}
