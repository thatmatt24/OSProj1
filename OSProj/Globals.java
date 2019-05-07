import java.util.LinkedList;

public class Globals  {
    
    Globals() {
        list.add("A1");
        list.add("B2");
        list.add("C3");
        list.add("D4");
    }

    public static int clsize, prosize, buff;
    public static String password;
    static LinkedList<String> list = new LinkedList<>();
    static String key = list.getFirst();

}