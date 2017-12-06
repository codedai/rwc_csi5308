/**
 * Created by Wange on 2017/12/2.
 */
public class Algorithm {


    // |VISION|

    public static String rightMove(String str, int num) {
        str = reChange(str);
        String first = str.substring(0, num);
        String right = str.substring(num);
        first = reChange(first);
        right = reChange(right);
        str = first + right;
        return str;
    }

    public static String leftMove(String str, int num) {
        String first = str.substring(0, num);
        String last = str.substring(num);
        first = reChange(first);
        last = reChange(last);
        String re = first + last;
        re = reChange(re);
        return re;

    }

    private static String reChange(String str) {
        // TODO Auto-generated method stub
        char[] ch = str.toCharArray();
        char temp;
        for (int i = 0; i < ch.length / 2; i++) {
            temp = ch[i];
            ch[i] = ch[ch.length - i - 1];
            ch[ch.length - i - 1] = temp;
        }
        return String.valueOf(ch);
    }

    public static String generateRepeatingString(char c, Integer n) {
        StringBuilder b = new StringBuilder();
        for (Integer x = 0; x <n; x++)
            b.append(c);
        return b.toString();
    }

    /*
    * --------------------------------------------------------------------------------
    * Set up phase
    * --------------------------------------------------------------------------------
    */




}
