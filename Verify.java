

public class Verify implements Runnable {

    Verify(Server socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        int value = 0;

        System.out.println("Connected: " + socket);

        // Establish threads for producing and consuming
        // producer thread() -> {
		    try {
		        produce(key);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}   }
        });

        // consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        while (true) {
            synchronized (this) {
                // producer thread waits while list
                // is full
                while (list.size() == Globals.buff)
                    wait();

                try {
                    Socket socket = serverSocket.accept();
                    var in = new Scanner(socket.getInputStream());
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String key = list.getFirst();
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

    public void produce(String key) throws InterruptedException {

        // key = this.key;

        while (true) {

            synchronized (this) {

                while (list.size() == capacity) {
                    wait();
                }
                // to insert the jobs in the list
                // fixme: may have to pass in which key is being
                Globals.list.add(key);

                // notifies the consumer thread that
                // now it can start consuming
                notify();

                // makes the working of program easier
                // to understand
                Thread.sleep(1000);

            }
        }
    }

    public String consume() throws InterruptedException {

        while (true) {

            synchronized (this) {

                while (list.size() == capacity) {
                    wait();
                }
                // to insert the jobs in the list
                // fixme: may have to pass in which key is being
                Globals.list.remove(key);

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


