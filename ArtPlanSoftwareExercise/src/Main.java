import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        long time2 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            getReverseString(s);
        }
        System.out.println(getReverseString(s));
        System.out.println(System.currentTimeMillis() - time2);
    }

    public static String getReverseString(String str) {
        return new StringBuffer(str).reverse().toString();
    }
}
