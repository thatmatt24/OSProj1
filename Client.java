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
        try (var socket = new Socket(args[0], 59898)) {
            System.out.print("Enter password: ");
            String clientinput = scanner.nextLine();
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            while (!clientinput.isBlank()) {
                out.println(clientinput);
                System.out.println(in.next());
            }
        }
    }
}