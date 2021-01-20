package cn.kaer.gocbluetooth;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        String s = "06 12 90 02 4F 3C 93 C9 A6";
        String[] s1 = s.trim().split(" ");
        printF(s1.length+"------");
        for (String temp : s1) {

         printF(temp);
        }

    }

    public static int hexString2Int(String hexString) {
        if (hexString.contains(" ")) {
            String[] s1 = hexString.split(" ");

            for (String temp : s1) {
                int i = hexString2Int(temp);
               return 0;
            }
        } else {
            return Integer.parseInt(hexString, 16);
        }
return 0;
    }

    private void printF(String s) {
        System.out.println(s);
    }

}