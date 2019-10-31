import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PeopleServer {
	private static final int PORT =5677;
	private static final int BUFSIZE = 1024;

	private DatagramSocket serverSock;
	private PeopleInfo pInfo;

	public PeopleServer() {
		try { // try to create a socket for the server
			serverSock = new DatagramSocket(PORT);
		} catch (SocketException se) {
			System.out.println(se);
			System.exit(1);
		}
		waitForPackets();
	}

	private void waitForPackets() {
		DatagramPacket receivePacket;
		byte data[];
		pInfo = new PeopleInfo();
		data = new byte[BUFSIZE];

		receivePacket = new DatagramPacket(data, data.length);
		while (true) {
			System.out.println("Waiting for a packet...");
			try {
				serverSock.receive(receivePacket);
				UDPServerThread thread = new UDPServerThread(data, serverSock, receivePacket);
				thread.start();
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
		
	}

	public class UDPServerThread extends Thread {
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] data = null;

		public UDPServerThread(byte[] data, DatagramSocket socket, DatagramPacket packet) {
			this.socket = socket;
			this.packet = packet;
			this.data = data;
		}

		private void processClient(DatagramPacket receivePacket) {
			InetAddress clientAddr = receivePacket.getAddress();
			int clientPort = receivePacket.getPort();
			String clientMesg = new String(receivePacket.getData(), 0, receivePacket.getLength());
			System.out.println("Client packet from " + clientAddr + ", " + clientPort);
			System.out.println("Client mesg: " + clientMesg);
			doRequest(clientAddr, clientPort, clientMesg);
		}

		private void doRequest(InetAddress clientAddr, int clientPort, String clientMesg) {
			if (clientMesg.trim().toLowerCase().equals("get")) {
				System.out.println("Processing 'get'");
				sendMessage(clientAddr, clientPort, pInfo.toString());
			} else if ((clientMesg.length() >= 5) && // "Info "
					(clientMesg.substring(0, 4).toLowerCase().equals("info"))) {
				System.out.println("Processing 'info'");
				pInfo.addPeople(clientMesg.substring(4)); // cut the score keyword
			} 
			else if(clientMesg.substring(0, 4).toLowerCase().equals("find")) {
				System.out.println("Processing 'find'");
				int fid = Integer.parseInt(clientMesg.substring(4));
				sendMessage(clientAddr, clientPort,pInfo.findNameById(fid) );
			}
			else
				System.out.println("Ignoring input line");

		}

		private void sendMessage(InetAddress clientAddr, int clientPort, String mesg) {
			byte mesgData[] = mesg.getBytes(); // convert message to byte[] form
			try {
				DatagramPacket sendPacket = new DatagramPacket(mesgData, mesgData.length, clientAddr, clientPort);
				socket.send(sendPacket);
			} catch (IOException ioe) {
				System.out.println(ioe);
			}

		}

		public void run() {
			processClient(packet);
		}
	}

	public static void main(String args[]) {
		new PeopleServer();
	}
}
