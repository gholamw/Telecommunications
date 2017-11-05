
//package link;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import tcdIO.Terminal;

public class Smartphone extends Node {

	Terminal terminal;
	int dstPort;
	DatagramSocket socket;
	InetSocketAddress dstAddress;
	private String phoneName;

	Smartphone(Terminal tr, String name) {
		this.terminal = tr;
		phoneName = name;

	}

	public void addSmartphone(int srcPort, int dtPort) throws SocketException {
		socket = new DatagramSocket(srcPort);
		dstPort = dtPort;
		dstAddress = new InetSocketAddress("localhost", dstPort);
		addListener(socket);
	}

	public String getSmartphoneName() {
		return phoneName;
	}

	public void start() throws IOException, InterruptedException {
		byte[] data = null;
		DatagramPacket packet = null;
		data = (this.terminal.readString("String to send: ")).getBytes();

		terminal.println("Sending packet...");
		packet = new DatagramPacket(data, data.length, dstAddress);
		socket.send(packet);
		terminal.println("Packet sent");
	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		byte[] arr = packet.getData();
		terminal.println("The message is " + new String(packet.getData()));
		System.out.println("smartphone message is " + (char) arr[0]);

	}

}
