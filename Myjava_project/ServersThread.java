import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class ServersThread implements Runnable{

	private ServerSocket serverSocket;
	private int port;
	String[] str = new String[]{"Echo","DayTime","Randomnumber"};
	
	public ServersThread(int port){
		this.port = port;
	}
	public static void main(String[] args) {
		for(int i=6789;i<=6791;i++){
			new Thread(new ServersThread(i)).start();
		}
	}

	@Override
 	public void run() {
		
			try {
			serverSocket = new ServerSocket(port);
			System.out.println(str[port-6789]+" is booting...");
			while (true) {
				Socket client = serverSocket.accept();
				if(port==6789)
				new Thread(new ClientHandler1(client)).start();
				if(port==6790)
				new Thread(new ClientHandler2(client)).start();
				if(port==6791)
				new Thread(new ClientHandler3(client)).start();
				
			}
			} catch (IOException e) {
				e.printStackTrace();
			}


		
	}


	private static class ClientHandler1 implements Runnable {
		private Socket client;

		public ClientHandler1(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
					PrintWriter pw = new PrintWriter(client.getOutputStream())) {
				String msg = br.readLine();
				System.out.println("Received " + client.getInetAddress() + " send:   " + msg);
				pw.println(msg);
				pw.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class ClientHandler2 implements Runnable {

		private Socket client;

		public ClientHandler2(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
					PrintWriter pw = new PrintWriter(client.getOutputStream())) {
				SimpleDateFormat sd = new SimpleDateFormat("dd MMM yy hh:mm:ss zzz");
				String time = sd.format(new Date());
				System.out.println("Received " + client.getInetAddress() + " send daytime request ");
				pw.println(time);
				pw.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class ClientHandler3 implements Runnable {
		private Socket client;

		public ClientHandler3(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
					PrintWriter pw = new PrintWriter(client.getOutputStream())) {
				Random rand = new Random();
				int num = rand.nextInt(10) + 1;
				System.out.println("Received " + client.getInetAddress() + " send random number request ");
				pw.println(String.valueOf(num));
				pw.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
