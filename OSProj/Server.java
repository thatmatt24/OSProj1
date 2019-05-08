/**
 * This program mimics a three-way hanshake with key generation. 
 * Server program opens communication over the 3030 port. 
 *  KeyList is used to track "global" variables for the keys, which are given to the client. 
 *  Server calls Verify() after connection is established. 
 *  Verify() will check the client's password against the user supplied password,
 *      and will generate a key if the correct password is supplied. 
 *
 * @author Matt McMahon, Adonis Mimunovic
 * @version 2.0 
 * 
*/
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.Executors;


public class Server {

    public static void main(String[] args) throws Exception {
        
        int clsize = KeyList.clsize; 
        int prosize = KeyList.getSsize(); 
        int buff = KeyList.buff;
        String password = KeyList.getPassword();
        KeyList.setList();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of clients: ");
        clsize = scanner.nextInt();
        KeyList.setCLsize(clsize);
        System.out.println();

        System.out.print("Enter number of servers: ");
        prosize = scanner.nextInt();
        KeyList.setSsize(prosize); 
        System.out.println();

        System.out.print("Enter size of buffer: ");
        buff = scanner.nextInt();
        KeyList.setBuffer(buff); 
        System.out.println();

        System.out.print("Enter password: ");
        password = scanner.next();
        KeyList.setPassword(password); 
        System.out.println();

        // creates a socket using port 3030 (which is an open, general purpose port,
        // mainly TCP use though)
        try (var listener = new ServerSocket(3030)) {
            System.out.println("The server is running...");

            // creates a thread pool which is the limit of the number of servers allowed,
            // created by user "prosize"
            var pool = Executors.newFixedThreadPool(KeyList.getSsize());
            while (true) {
                pool.execute(new Verify(listener.accept()));
            }
        }
    }
}
