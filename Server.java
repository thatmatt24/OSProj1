import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.io.*;

public class Server implements Runnable {
    
    public static int clsize, prosize, buff;
    public static String password;

    public static void main(String[] args) {

        final SC sc = new SC();
        
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter number of clients: ");
		clsize = scanner.nextInt();
		System.out.println();

		System.out.print("Enter number of servers: ");
		prosize = scanner.nextInt();
		System.out.println();

		System.out.print("Enter size of buffer: ");
		buff = scanner.nextInt();
		System.out.println();

		System.out.print("Enter password: ");
		password = scanner.next();
        System.out.println();
        
        
    
        // creates a socket using port 3030 (which is an open, general purpose port,
        // mainly TCP use though)
        ServerSocket listener = new ServerSocket(3030);
        System.out.println("The server is running...");

        
        // creates a thread pool which is the limit of the number of servers allowed,
		// created by user "prosize"
        var pool = Executors.newFixedThreadPool(prosize);
        
		while (true) {

			// calls the Server class and starts listening on port 59898
			// essentially starts the server
            pool.execute(new Verify(listener.accept()));

            listener.close();
                
            }
    }
}

public class Verify implements Runnable {
    private Socket socket;

    Verify(Serve socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        final SC sc = new SC();
        int value = 0;
        String key = ""; //FIXME: need to access linkedlist from this class

        System.out.println("Connected: " + socket);

        // Establish threads for producing and consuming
        //  producer thread
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sc.produce(key);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sc.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        while (true) {
            synchronized (this) {
                // producer thread waits while list
                // is full
                while (list.size() == capacity)
                    wait();

                try {
                    Socket socket = serverSocket.accept();
                    var in = new Scanner(socket.getInputStream());
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // while(){
                    // bounded buffer - # of clients checking the password
                    // keystore - so # of available 'keys'
                    // }
                    String input = bufferedReader.readLine();

                    do {
                        printWriter.println("Enter password: ");
                        input = bufferedReader.readLine();

                        if (input.equals(password)) {

                            printWriter.println("Access Granted");
                            printWriter.println("Key: " + key); 
                            t1.start();

                            t1.join();

                        } else {

                            printWriter.println("Access Denied");
                        }

                    } while (!input.equals("done"));

                    if (input.equals("done")) {

                        System.out.println(key + "key returned.");

                        t2.start();

                        t2.join();
                    }

                    printWriter.close();
                    serverSocket.close();

                } catch (Exception e) {

                    System.out.println("Error:" + socket);
                    e.printStackTrace();

                } finally {

                    socket.close();
                    System.out.println("Closed: " + socket);

                }
            }

        }
    }
}


public static class SC {

        LinkedList<String> list = new LinkedList<>();
        int capacity = 2;

        SC() {
            list.add("A1");
            list.add("B2");
            list.add("C3");
            list.add("D4");
        }

    public void produce(String key) throws InterruptedException {

        //key = this.key; 

        while (true) {

            synchronized (this) {

                while (list.size() == capacity) {
                    wait();
                }
                // to insert the jobs in the list
                // fixme: may have to pass in which key is being 
                list.add(key);

                // notifies the consumer thread that
                // now it can start consuming
                notify();

                // makes the working of program easier
                // to understand
                Thread.sleep(1000);

            }
        }
    }

    public String consume() throws InterruptedException{
        
        while (true) {

            synchronized (this) {

                while (list.size() == capacity) {
                    wait();
                }
                // to insert the jobs in the list
                // fixme: may have to pass in which key is being 
                list.remove(key);

                // notifies the consumer thread that
                // now it can start consuming
                notify();

                // makes the working of program easier
                // to understand
                Thread.sleep(1000);

            }
        }
    }

}

