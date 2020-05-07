import java.util.ArrayList;
import java.util.Collections;

public class test {
    public static void main(String args[])
    {
        ArrayList<String> A = new ArrayList<String>();
        A.add("BYE");
        A.add("OKAY");
        A.add("TIC");
        A.add("RANDOM");
        String c = A.get(0);
        A.remove(0);
        A.add(c);
        Collections.sort(A);
        System.out.println(A);
    }
}