import java.util.LinkedList;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.Executors;


public class Serve {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of clients: ");
        Globals.clsize = scanner.nextInt();
        System.out.println();

        System.out.print("Enter number of servers: ");
        Globals.prosize = scanner.nextInt();
        System.out.println();

        System.out.print("Enter size of buffer: ");
        Globals.buff = scanner.nextInt();
        System.out.println();

        System.out.print("Enter password: ");
        Globals.password = scanner.next();
        System.out.println();

        // creates a socket using port 3030 (which is an open, general purpose port,
        // mainly TCP use though)
        try (var listener = new ServerSocket(3030)) {
            System.out.println("The server is running...");

            // creates a thread pool which is the limit of the number of servers allowed,
            // created by user "prosize"
            var pool = Executors.newFixedThreadPool(Globals.prosize);
            while (true) {
                pool.execute(new Verify(listener.accept()));
            }
        }
        
    }
}

