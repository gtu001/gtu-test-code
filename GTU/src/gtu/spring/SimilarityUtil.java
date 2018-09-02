package gtu.spring;

/**
 * 编辑距离的两字符串相似度
 * 
 * Levenshtein distance最先是由俄国科学家Vladimir
 * Levenshtein在1965年发明，用他的名字命名。不会拼读，可以叫它edit distance（编辑距离）。
 * 原理很简单，就是返回将第一个字符串转换(删除、插入、替换)成第二个字符串的编辑次数。次数越少，意味着字符串相似度越高
 * 
 * Levenshtein distance可以用来：
 * 
 * Spell checking(拼写检查) Speech recognition(语句识别) DNA analysis(DNA分析) Plagiarism
 * detection(抄袭检测) LD用m*n的矩阵存储距离值。算法大概过程：
 * 
 * @author jianpo.mo
 */
public class SimilarityUtil {

    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }

    public static int ld(String str1, String str2) {
        int d[][]; // 矩阵
        int n = str1.length();
        int m = str2.length();
        int i; // 遍历str1的
        int j; // 遍历str2的
        char ch1; // str1的
        char ch2; // str2的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) { // 遍历str1
            ch1 = str1.charAt(i - 1);
            // 去匹配str2
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    public static double sim(String str1, String str2) {
        int ld = ld(str1, str2);
        return 1 - (double) ld / Math.max(str1.length(), str2.length());
    }

    public static void main(String[] args) {
        String str1 = "chenlb.blogjava.net";
        String str2 = "chenlb.javaeye.com";
        System.out.println("ld=" + ld(str1, str2));
        System.out.println("sim=" + sim(str1, str2));
        
//        完全一樣
//        ld=0
//        sim=1.0
    }
}