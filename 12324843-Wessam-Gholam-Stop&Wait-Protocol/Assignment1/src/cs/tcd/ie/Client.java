/**
 * 
 */
package cs.tcd.ie;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input
 *
 */
public class Client extends Node{
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";
    Timer timer = new Timer();

	
	//public ArrayList acks;
	
	Terminal terminal;
	InetSocketAddress dstAddress;
	int status = 1; // 1 to send, 0 no send
	DatagramPacket packet = null;
	int counter =0;
	


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
		counter++;
		status = 1;
		StringContent content = new StringContent(packet);
		terminal.println(content.toString());
		this.notify();
	}
	

	/**
	 * Sender Method
	 * 
	 * 
	 */
	
	class clock extends TimerTask{

		@Override
		public void run() {
			status = 1;

			terminal.println("Time is up");
			DatagramPacket p = getPacket();
			//imp//StringContent content= new StringContent(p);
			
			
			//char[] arr = (content.toString()).toCharArray();
			//char[] arr2 = Arrays.copyOf(arr, 10); //10 the the length of the new array

			//arr2[2] = 'R';
			//String newPacket = new String(arr2);
			//p = (new StringContent(newPacket)).toDatagramPacket();

			//DatagramPacket p;
			//byte[] data1 = null;
			//data1 = new byte[2];
			//char c = 'R';
			//char q = '6';

			//data1[0] = (byte) c;
			//data1[1] = (byte) q;

			//p = new DatagramPacket(data1, data1.length, dstAddress);
			StringContent content= new StringContent(p);
			

			try {
				p.setSocketAddress(dstAddress);
				socket.send(p);
				//this.notifyAll();
				terminal.println("\n Re-sending: " + content);
				if(counter < content.toString().length()){
					timer.schedule(new clock(),1000);
				}


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public synchronized void start() throws Exception{

		byte[] data = null;
		char sendingPrefix = '0';
		 packet = null;
		String str = terminal.readString("String to send: ");
		for (int i = 0; i < str.length(); i++) {
			data = new byte[2];
			char c = str.charAt(i);
			data[0] = (byte) c;
			data[1] = (byte) sendingPrefix;
			terminal.println("Sending packet..." );
			packet = new DatagramPacket(data, data.length, dstAddress);
			terminal.println("\njust sent: " + str.charAt(i) + sendingPrefix);
				//socket.setSoTimeout(500);
			getPacket();
		    clock b= new clock();

				socket.send(packet);
				timer.schedule(new clock(), 1000);
				//socket.send(packet);

				status = 0; 
				while(status == 0){
					this.wait();
				}
				//int t = 4;
				
				//while(5 > t){
					//this.wait();
				//}
				
				//this.wait();
				
			 //catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			//}
			//status = 0;
			//try {
				//while (status == 0) {
					// this.wait();
					//socket.setSoTimeout(1000);
					 //Thread.sleep((long) 100.0);
				//}

				
				  if(sendingPrefix == '0'){ sendingPrefix ='1'; }else{
				  sendingPrefix = '0'; }
			
			}
		timer.cancel();
		}
	
	
	public DatagramPacket getPacket(){
		//System.out.print("Hi");
		return packet;
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
