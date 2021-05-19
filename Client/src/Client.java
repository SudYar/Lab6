import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket clientSocket= null;

        try {
            clientSocket = new Socket("localhost", 7345);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
