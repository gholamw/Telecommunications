

package link;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import tcdIO.Terminal;

public class Smartphone extends Node{
	
	Terminal terminal;
	int dstPort;
	DatagramSocket socket;
	InetSocketAddress dstAddress;
	
	Smartphone(Terminal tr){
		this.terminal = tr;

	}
	
	
	public void addSmartphone(int srcPort , int dtPort) throws SocketException{
		socket = new DatagramSocket(srcPort);
		dstPort = dtPort;
		dstAddress= new InetSocketAddress("localhost", dstPort);
		addListener(socket);
	}
	
	
//	public void connect(int port){
//		InetAddress add = null;
//		byte[] data= null;
//		DatagramPacket packet= null;
//		data= (this.terminal.readString("Give an address to connect to: ")).getBytes();
//		try {
//			add = InetAddress.getLocalHost();
//			System.out.println(add.toString());
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		socket.connect(add, port);
//		System.out.println(socket.isConnected());
//		return;
//	}
	
	public void start() throws IOException, InterruptedException{
		byte[] data= null;
		DatagramPacket packet= null;
		
			data= (this.terminal.readString("String to send: ")).getBytes();
			
			terminal.println("Sending packet...");
			dstAddress= new InetSocketAddress("localhost", 57000);
			packet= new DatagramPacket(data, data.length, dstAddress);
			socket.send(packet);
			terminal.println("Packet sent");
			//socket.close();
			//this.wait();
	}
	
	
	@Override
	public void onReceipt(DatagramPacket packet) {
		terminal.println("Something went wrong");
		
	}

}

