import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.Socket;


public class DaytimeClient {
	public static void main(String[] args) throws Exception {
        Socket client = new Socket("localhost", 6790);
        System.out.print("Get DayTime : ");
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println(br.readLine());
        client.close();
    }
}
