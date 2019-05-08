import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        try (var socket = new Socket(args[0], 3030)) {
            System.out.print("Hit Enter.");

            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            while (scanner.hasNextLine()) {
                
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());

            }
        }
    }
}