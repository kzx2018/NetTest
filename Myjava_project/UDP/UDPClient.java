import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient extends JFrame implements ActionListener {
	private static final int SERVER_PORT = 5677; // server details
	private static final String SERVER_HOST = "localhost";

	private static final int BUFSIZE = 1024; // max size of a message

	private DatagramSocket sock;
	private InetAddress serverAddr;

	private JTextArea jtaMesgs,jtaName;
	private JTextField jtfName, jtfId,jtfFindNameById;
	private JButton jbGetInfo,jbFindName;

	public UDPClient() {
		super("People Infomation UDP Client");

		initializeGUI();

		try { // try to create the client's socket
			sock = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		try { // try to turn the server's name into an internet address
			serverAddr = InetAddress.getByName(SERVER_HOST);
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			System.exit(1);
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 550);
		setResizable(false); // fixed size display
		setVisible(true);

		waitForPackets();
	} // end of ScoreUDPClient();

	private void initializeGUI()
	// text area in center, and controls in south
	{
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		jtaMesgs = new JTextArea(7, 7);
		jtaMesgs.setEditable(false);
		JScrollPane jsp = new JScrollPane(jtaMesgs);
		c.add(jsp, "Center");
		
		
		JLabel jlId = new JLabel("Id: ");
		jtfId = new JTextField(5);
		jtfId.addActionListener(this);

		JLabel jlName = new JLabel("Name: ");
		jtfName = new JTextField(10);
		jtfName.addActionListener(this);
		// pressing enter triggers sending of name/score

		jbGetInfo = new JButton("Get Info");
		jbGetInfo.addActionListener(this);
		
		JLabel jlFindNameById = new JLabel("FindNameById: ");
		jtfFindNameById = new JTextField(5);
		
		jbFindName = new JButton("Find Name");
		jbFindName.addActionListener(this);
		
		JLabel jlName1 = new JLabel("Get Name: ");
		jtaName = new JTextArea(1,10);
		jtaName.setEditable(false);
		
		
		JPanel p1 = new JPanel(new FlowLayout());
		p1.add(jlId);
		p1.add(jtfId);
		p1.add(jlName);
		p1.add(jtfName);

		JPanel p2 = new JPanel(new FlowLayout());
		p2.add(jbGetInfo);

		JPanel p3 = new JPanel(new FlowLayout());
		p3.add(jlFindNameById);
		p3.add(jtfFindNameById);
		p3.add(jbFindName);
	
		JPanel p4 = new JPanel(new FlowLayout());
		p4.add(jlName1);
		p4.add(jtaName);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(p1);
		p.add(p2);
		p.add(p3);
		p.add(p4);
		c.add(p, "South");

	} // end of initializeGUI()

	// --------------------------------------------------------------
	// client processing of user commands; done by GUI thread

	public void actionPerformed(ActionEvent e)
	/*
	 * Either a name/score is to be sent or the "Get Scores" button has been
	 * pressed.
	 * 
	 * Only output messages are sent to the server from here, no input is received.
	 * Server responses are dealt with by the application thread in
	 * waitForPackets().
	 */
	{
		if (e.getSource() == jbGetInfo) {
			sendMessage(serverAddr, SERVER_PORT, "get");
			jtaMesgs.append("Sent a get command\n");
		} else if (e.getSource() == jtfName)
			sendInfo();
		else if(e.getSource()==jbFindName) {
			
			String findId = jtfFindNameById.getText().trim();
			if(findId.equals("")) {
				JOptionPane.showMessageDialog(null, "No Id pressed", "Find name Error", JOptionPane.ERROR_MESSAGE);
			}
			sendMessage(serverAddr, SERVER_PORT, "find"+findId);
			jtaMesgs.append("Sent a find command\n");
		}
	} // end of actionPerformed()

	private void sendInfo()
	/*
	 * Check if the user has supplied a name and score, then send
	 * "score name & score &" to server Note: we should check that score is an
	 * integer, but we don't.
	 */
	{
		String Id = jtfId.getText().trim();
		String name = jtfName.getText().trim();

		// System.out.println("'" + name + "' '" + score + "'");

		if ((name.equals("")) && (Id.equals("")))
			JOptionPane.showMessageDialog(null, "No Id and name entered", "Send Info Error",
					JOptionPane.ERROR_MESSAGE);
		else if (name.equals(""))
			JOptionPane.showMessageDialog(null, "No name entered", "Send Info Error", JOptionPane.ERROR_MESSAGE);
		else if (Id.equals(""))
			JOptionPane.showMessageDialog(null, "No Id entered", "Send Info Error", JOptionPane.ERROR_MESSAGE);
		else {
			sendMessage(serverAddr, SERVER_PORT, "Info " + Id + " " + name);
			jtaMesgs.append("Sent " + Id + " " + name + "\n");
		}
	} // end of sendScore()

	private void sendMessage(InetAddress serverAddr, int serverPort, String mesg)
	// send message to the socket at the specified address and port
	{
		byte mesgData[] = mesg.getBytes(); // convert message to byte[] form
		try {
			DatagramPacket sendPacket = new DatagramPacket(mesgData, mesgData.length, serverAddr, serverPort);
			sock.send(sendPacket);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	} // end of sendMessage()

	// ------------------------------------------------------------
	// processing of server responses; done by the application thread
	// very similar to the server processing of client messages

	private void waitForPackets()
	/*
	 * Repeatedly receive a packet, process it. No messages are sent to the server
	 * from here. Output is left to the GUI thread.
	 */
	{
		DatagramPacket receivePacket;
		byte data[];

		try {
			while (true) {
				// set up an empty packet
				data = new byte[BUFSIZE];
				receivePacket = new DatagramPacket(data, data.length);

				System.out.println("Waiting for a packet...");
				sock.receive(receivePacket);

				processServer(receivePacket);
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	} // end of waitForPackets()

	private void processServer(DatagramPacket receivePacket)
	// extract server details from the received packet
	{
		InetAddress serverAddr = receivePacket.getAddress();
		int serverPort = receivePacket.getPort();
		String serverMesg = new String(receivePacket.getData(), 0, receivePacket.getLength());

		System.out.println("Server packet from " + serverAddr + ", " + serverPort);
		System.out.println("Server mesg: " + serverMesg);

		showResponse(serverMesg);
	} // end of processServer()

	private void showResponse(String mesg)
	/*
	 * The only server response is to a client's get command. The response should be
	 * "HIGH$$ n1 & s1 & .... nN & sN & "
	 */
	{
		if ((mesg.length() >= 6) && // "INFO&\n "
				(mesg.substring(0, 6).equals("INFO&\n ")))
			showInfo(mesg.substring(6).trim());
		// remove HIGH$$ keyword and surrounding spaces
		else if((mesg.length() >= 6) &&(mesg.substring(0, 6).equals("NAME&&")))
		{
			jtaName.setText("");
			jtaName.append(mesg.substring(6).trim());
		}
		else // should not happen
			jtaMesgs.append(mesg + "\n");
	} // end of showResponse()

	private void showInfo(String mesg)
	// Parse the high scores and display in a nice way
	{
		StringTokenizer st = new StringTokenizer(mesg, " ");
		String name;
		int i, id;
		i = 1;
		try {
			while (st.hasMoreTokens()) {
				id = Integer.parseInt(st.nextToken().trim());
				name = st.nextToken().trim();
				jtaMesgs.append("" + i + ". " + id + " : " + name + "\n");
				i++;
			}
			jtaMesgs.append("\n");
		} catch (Exception e) {
			jtaMesgs.append("Problem parsing People Infomation\n");
			System.out.println("Parsing error with People Infomation: \n" + e);
		}
	} // end of showHigh()

	// ------------------------------------

	public static void main(String args[]) {
		new UDPClient();
	}

} // end of ScoreUDPClient class
