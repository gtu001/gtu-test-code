package gtu.google;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkState;

public class PreconditionsTest {

    public static void main(String[] args) {
        checkArgument(true, "false 會顯示 java.lang.IllegalArgumentException");
        checkState(true, "false 會顯示 java.lang.IllegalStateException");
        checkArgument(true, "這事第一個%s,這事第二個%s,其它會一起列在後面", "aaa", "bbb", "ccc", "ddd");
        checkNotNull("not null", "null會顯示錯誤");
        checkElementIndex(0, 1);// 第一個<第二個
        checkPositionIndex(1, 1);// 第一個<=第二個
        System.out.println("done...");
    }
}
