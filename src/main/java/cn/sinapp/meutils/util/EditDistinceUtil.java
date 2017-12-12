/**
 * 
 */
package cn.sinapp.meutils.util;

/**
 * @author zhangdong zhangdong147896325@163.com
 * 
 *         2012-8-29 下午2:06:51
 */
public class EditDistinceUtil {

    private static int Minimum(int a, int b, int c) {
        int mi;
        mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;
    }

    public static int getEditDistance(String s, String t) {
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char sI; // ith character of s
        char tJ; // jth character of t
        int cost; // cost
        // Step 1
        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];

        // Step 2

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3

        for (i = 1; i <= n; i++) {
            sI = s.charAt(i - 1);
            // Step 4
            for (j = 1; j <= m; j++) {
                tJ = t.charAt(j - 1);
                // Step 5
                if (sI == tJ) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                // Step 6
                d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }
        // Step 7
        return d[n][m];
    }

    public static int getEditDistance(String s, String t, int sLength, int tLength) {
        int[][] d = new int[sLength + 1][tLength + 1];
        int i, j, cost;
        char sI, tJ;
        for (i = 0; i <= sLength; i++) {
            d[i][0] = i;
        }
        for (j = 0; j <= tLength; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= sLength; i++) {
            sI = s.charAt(i - 1);
            for (j = 1; j <= tLength; j++) {
                tJ = t.charAt(j - 1);
                if (sI == tJ) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }
        return d[sLength][tLength];
    }

}
