import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class RandnumClient {
	public static void main(String[] args) throws Exception {
        Socket client = new Socket("localhost", 6791);
        System.out.print("Get server product random number: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println(br.readLine());
        client.close();
    }
}
