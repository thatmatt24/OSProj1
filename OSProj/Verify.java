import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Verify extends RuntimeException implements Runnable {
    private Socket socket; 

    // Establish threads for producing and consuming
    // producer thread
    Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                produce(KeyList.getKey());
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
                consume(KeyList.getKey());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    Verify(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        int value = 0;

        System.out.println("Connected: " + socket);

        while (true) {
            synchronized (this) {

                try {
                    
                    var in = new Scanner(socket.getInputStream());
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String input = bufferedReader.readLine();

                    do {
                        printWriter.println("Enter password: ");
                        input = bufferedReader.readLine();

                        if (input.equals(KeyList.getPassword())) {

                            printWriter.println("Access Granted. Your Key is: " + KeyList.getKey());
                            t1.start();

                            t1.join();

                        } else {

                            printWriter.println("Access Denied");
                        }

                    } while (!input.equals("done"));

                    if (input.equals("done")) {

                        System.out.println(KeyList.getKey() + "key returned.");

                        t2.start();

                        t2.join();
                    }

                    printWriter.close();
                    // serverSocket.close();

                } catch (Exception e) {

                    System.out.println("Error:" + socket);
                    e.printStackTrace();

                } finally {

                    // socket.close();
                    System.out.println("Closed: " + socket);

                }
            }

        }
    }

    public void produce(String key) throws InterruptedException {

        // key = this.key;

        while (true) {

            synchronized (this) {
                try {
                    while (KeyList.list.size() == KeyList.getBuffer()) {
                        wait();
                    }
                    // to insert the jobs in the list
                    // fixme: may have to pass in which key is being
                    KeyList.getKey();

                    // notifies the consumer thread that
                    // now it can start consuming
                    notify();

                    // makes the working of program easier
                    // to understand
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }

    }

    public String consume(String key) throws InterruptedException {

        while (true) {

            synchronized (this) {
                try {
                    while (KeyList.list.size() == KeyList.getBuffer()) {
                        wait();
                    }
                    // to insert the jobs in the list
                    // fixme: may have to pass in which key is being
                    KeyList.removeKey();

                    // notifies the consumer thread that
                    // now it can start consuming
                    notify();

                    // makes the working of program easier
                    // to understand
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

            }
        }

    }
}
