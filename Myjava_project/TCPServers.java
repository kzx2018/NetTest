
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TCPServers {

	private static final int ECHO_SERVER_PORT = 6789;
	private static final int DAYTIME_SERBER_PORT = 6790;
	private static final int RANDOMNUM_SERBER_PORT = 6791;

	public static void main(String[] args) {
		if(args[0].equals("6791")){
		try (ServerSocket server = new ServerSocket(RANDOMNUM_SERBER_PORT)) {
			System.out.println("RANDNUM is booting...");
			while (true) {
				Socket client = server.accept();
				new Thread(new ClientHandler3(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		if(args[0].equals("6790")){
		try (ServerSocket server = new ServerSocket(DAYTIME_SERBER_PORT)) {
			System.out.println("DAYTIME is booting...");
			while (true) {
				Socket client = server.accept();
				new Thread(new ClientHandler2(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		if(args[0].equals("6789")){
		try (ServerSocket server = new ServerSocket(ECHO_SERVER_PORT)) {
			System.out.println("ECHO is booting...");
			while (true) {
				Socket client = server.accept();
				new Thread(new ClientHandler1(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
