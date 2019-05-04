
// Java program to implement solution of producer 
// consumer problem. 
import java.util.LinkedList;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

//import jdk.internal.jline.internal.InputStreamReader;

import java.util.Random;

public class ServerSide {

	public static int clsize, prosize, buff;
	public static String password;

	// Receives user input for number of clients and servers as well as size of buffer
	// 		also gets the password from the user to be verified by the client(s)
	public static void main(String[] args) throws Exception {

		// Object of a class that has both produce()
		// and consume() methods
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

		// creates a socket using port 59898 (which is an open, general purpose port,
		// mainly TCP use though)
		try (var listener = new ServerSocket(59898)) {
			System.out.println("The server is running...");

			// creates a thread pool which is the limit of the number of servers allowed,
			// created by user "prosize"
			var pool = Executors.newFixedThreadPool(prosize);
			while (true) {

				// calls the Server class and starts listening on port 59898
				// essentially starts the server
				pool.execute(new Server(listener.accept()));
			}
		}
	}

	private static class Server implements Runnable {
		private Socket socket;

		// contructor for socket, which defines the global
		// socket as the local one to be used here
		Server(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			System.out.println("Connected: " + socket);
			int guess = 0; 
			try{
			// always while true so server will continue to run when a client accesses it;
			// so the server can spawn threads continuously when a client is created
			while (true) {
				synchronized (this) {
					try{
						// bounded buffer; the server can only handle the "buff" size of actions
						// ???? 

						// receives input from client
						DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
						var out = new PrintWriter(socket.getOutputStream(), true);
						while (!in.equals(null)) {

							System.out.println("------ in.toString is: " + in.toString());
							// insert Server action to client here
							if ( in.equals(password) ) {
								out.println("ACCESS GRANTED");
							} else {
								// guesses.add(guess++);
								out.println("ACCESS DENIED. Try again..");
							}
							// }
							// notifies the other threads that they can continue
							notify();
							// sleep for 5 seconds
							Thread.sleep(500);
						}
					} catch (Exception e) {
						System.out.println("Error:" + socket);
					} finally {
						try {
							socket.close();
						} catch (IOException e) {
						}
						System.out.println("Closed: " + socket);

					}
				}
				}
			} catch (Exception e) {
				System.out.println("Error:" + socket);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println("Closed: " + socket);

			}
		}
	}
}
