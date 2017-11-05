/**
 * 
 */
package cs.tcd.ie;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input
 *
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";
	public static int calls = 0;
	public static boolean resend = false;
	int oddOrEven = 0;

	String str;
	Terminal terminal;
	InetSocketAddress dstAddress;
	ArrayList<Integer> list;
	ArrayList<String> ACKslist = new ArrayList<String>();
	String[] ackList;
	int[] list1;

	/**
	 * Constructor
	 * 
	 * Attempts to create socket at given port and create an InetSocketAddress
	 * for the destinations
	 */
	Client(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal = terminal;
			dstAddress = new InetSocketAddress(dstHost, dstPort);
			socket = new DatagramSocket(srcPort);
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		calls++;
		resend = false;
		StringContent content = new StringContent(packet);
		int seq = content.toString().charAt(3);

		String ack = content.toString();
		storeAcknowledgment(ack);
		checkTypeOfAcknowledgment(packet);

		// ! might need it later!
		// StringContent content = new StringContent(packet);
		// int seq = content.toString().charAt(3);

		terminal.println(content.toString());

		// // resend missing packet
		// if (list.contains(0)) {
		// int index = list.lastIndexOf(0);
		// System.out.println("LastIndexOf : " + index);
		// resendMissingPacket(index);
		// }

		// might need it later
		// if ( !ACKslist.contains("NAK") || list.size() % 2 ==0 || calls % 2 ==
		// 0)

		if (calls % 2 == oddOrEven) {
			// if (!list.contains(0) && list.size() % 2 == 0)
			// resend missing packet
			if (list.contains(0)) {
				resend = true;
				oddOrEven = (oddOrEven + 1) % 2;
				int index = list.lastIndexOf(0);
				System.out.println("LastIndexOf : " + index);
				resendMissingPacket(index);
			}
			if (resend == false)
				this.notifyAll();
		}
	}

	public void checkTypeOfAcknowledgment(DatagramPacket packet) {
		StringContent co = new StringContent(packet);
		int n = co.toString().charAt(3);
		n = n - '0';
		System.out.println("n" + n);
		if (new StringContent(packet).toString().charAt(0) == 'N') {
			// list.add(0);
			list.set(n, 0);
		} else {
			// list.add(1);
			list.set(n, 1);

		}

		System.out.println(list.toString());
	}

	public void storeAcknowledgment(String ackString) {
		int index = ackString.charAt(3);
		index = index - '0';
		String ind = String.valueOf(index);
		String ind1 = "" + index;
		String nak = "NAK" + ind;
		String ack = "ACK" + ind;
		// if (ACKslist.isEmpty()) {
		// ACKslist.add(ackString);
		// // System.out.println("st : " + ackString);
		// } else {
		// if (ACKslist.contains("ACK" + ind)) {
		// int index1 = ACKslist.indexOf("ACK" + index);
		// ACKslist.set(index, "NAK" + index1);
		// // terminal.println("It comes here bitch");
		//
		// } else if (ACKslist.contains(v)) {
		// int index1 = ACKslist.indexOf("NAK" + index);
		// ACKslist.set(index, "ACK" + index1);
		// terminal.println("It comes here bitch");
		//
		// } else {
		// ACKslist.add(ackString);
		// //terminal.println("It comes here bitch");
		//
		// }
		//
		// }
		//
		// terminal.println("Values of arraylist of acknelowdgemnts");
		// for(int i=0; i<ACKslist.size(); i++){
		// terminal.println(ACKslist.get(i));
		// }

		if (ackList[index] != null) {
			System.out.println("index" + index);
			System.out.println("str" + ind1);
			char c = ackList[index].charAt(3);
			String s = Character.toString(c);
			if (s.equals(ind)) {
				// if (ackList[index].endsWith(ind1)) {
				ackList[index] = ackString;
				System.out.println("It comes here");
			}
		} else {
			ackList[index] = ackString;
		}

	}

	public void updateCheckTypeOfAcknowledgment(int index) {

	}

	public void resendMissingPacket(int index) {
		byte[] data = new byte[2];
		data[0] = (byte) str.charAt(index);
		data[1] = (byte) index;
		DatagramPacket packet = new DatagramPacket(data, data.length,
				dstAddress);
		try {
			socket.send(packet);
			terminal.println("Resending packet : " + new StringContent(packet)
					+ index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception {
		byte[] data = null;
		DatagramPacket packet = null;
		data = new byte[2];
		str = (terminal.readString("String to send: "));
		ackList = new String[str.length()];
		list1 = new int[str.length()];
		list = new ArrayList<Integer>(str.length());
		for (int i = 0; i < str.length(); i++) {
			list.add(-1);
		}

		for (int i = 0; i < str.length(); i = i + 2) {
			for (int j = 0, m = i; j < 2; j++) {
				if (m < str.length()) {
					data[0] = (byte) str.charAt(m);
					data[1] = (byte) m;
					packet = new DatagramPacket(data, data.length, dstAddress);
					terminal.println("Sending packet..."
							+ new StringContent(packet) + data[1]);

					socket.send(packet);
					// terminal.println("Packet sent");
					m++;
				} else {
					break;
				}
			}
			this.wait();
		}
		// test result of acksList
//		terminal.println("ACKsList values : ");
//		for (int i = 0; i < ACKslist.size(); i++) {
//			terminal.println(ACKslist.get(i));
//		}
		
		//printAllRcieveidPackets();
		terminal.println();
		return;
	}
	
	public void printAllRcieveidPackets(){
		terminal.println("packets recieved in order");
		System.out.println();		
		for(int i=0; i<list.size(); i++){
			if(list.get(i) == 1){
				String s = Character.toString( str.charAt(i));
				terminal.print(s);
			}else{
				
			}
		}
	}

	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Client");
			(new Client(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT,
					DEFAULT_SRC_PORT)).start();
			terminal.println("Program completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}