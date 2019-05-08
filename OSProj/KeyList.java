import java.util.ArrayList;

public class KeyList {
    static ArrayList<String> list = new ArrayList<>();
    public static int clsize, prosize, buff = 0;
    public static String password;


    public static void setList() {
        
        list.add("A1");
        list.add("B2");
        list.add("C3");
        list.add("D4");
    }

    public static String getKey() {

        String key = list.get(list.size()-1);
        list.remove(list.size()-1); 
        return key; 
    }

    public static void removeKey(){

    }

    public static String getPassword(){

        return password;
    }

    public static void setPassword(String pword){
        
        password = pword; 
    }

    public static void setCLsize(int size){
        clsize = size; 
    } 

    public static int getCLsize(){
        return clsize;
    }
    
    public static void setSsize(int size){
        prosize = size; 
    }
    
    public static int getSsize(){
        return prosize;
    }

    public static void setBuffer(int size){
        buff = size; 
    }

    public static int getBuffer(){
        return buff; 
    }
    
}