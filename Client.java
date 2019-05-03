import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        try (var socket = new Socket(args[0], 59898)) {
            System.out.print("Enter password: ");
            Scanner scanner = new Scanner(System.in);
            BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
                System.out.println(in.readLine());
            // }
        }
    }
}