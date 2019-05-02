// Java program to implement solution of producer 
// consumer problem. 
import java.util.LinkedList;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.Random;

public class ServerSide {

	public static int clsize, prosize, buff;

	public static void main(String[] args) throws Exception {
		// Object of a class that has both produce()
		// and consume() methods
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter number of clients: ");
		clsize = scanner.nextInt();
		System.out.println();

		System.out.print("Enter number of servers (Maximum of 9): ");
		prosize = scanner.nextInt();
		System.out.println();
		if (prosize > 9) {
			System.out.println("Maximum number of servers exceeded!\nSetting number " + "of servers to 9...\n");
			prosize = 9;
		}

		// int[] sock_list = new int[prosize];
		// int[] port_list = { 1337, 1859, 3000, 3002, 3030, 3128, 3306, 3333, 3621 };

		// for (int i = 0; i < sock_list.length; i++) {
		// 	sock_list[i] = port_list[i];
		// }

		System.out.print("Enter size of buffer: ");
		buff = scanner.nextInt();
		System.out.println();

		try (var listener = new ServerSocket(59898)) {
			System.out.println("The server is running...");
			var pool = Executors.newFixedThreadPool(prosize);
			while (true) {
				pool.execute(new Server(listener.accept()));
			}
		}
	}

	private static class Server implements Runnable {
		private Socket socket;

		Server(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("Connected: " + socket);

			while (true) {
				synchronized (this) {

					try {
						// receives input from client
						var in = new Scanner(socket.getInputStream());
						var out = new PrintWriter(socket.getOutputStream(), true);
						while (in.hasNextLine()) {
							// insert Server action to client here
							out.println(in.nextLine().toUpperCase());
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
	}

}