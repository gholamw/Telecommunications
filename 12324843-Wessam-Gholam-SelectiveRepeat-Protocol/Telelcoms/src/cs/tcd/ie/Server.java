package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;

	Terminal terminal;
	final int noise = 50;

	/*
	 * 
	 */
	Server(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(port);
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public void onReceipt(DatagramPacket packet) {
		try {
			boolean packetDropped = isPacketDropped();
			if (packetDropped) {
				StringContent content = new StringContent(packet);
				System.out.println(content);
				int seq = content.toString().charAt(1);
				//terminal.println(content.toString() + seq); dont print coz its been dropped
				DatagramPacket response;
				response = (new StringContent("NAK" + seq)).toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
				//return; (not sure if i need this)
				terminal.println("Packet dropped : " + " " +  new StringContent(packet));
				
			} else {
				StringContent content = new StringContent(packet);
				int seq = content.toString().charAt(1);
				terminal.println(content.toString() + seq);
				DatagramPacket response;
				response = (new StringContent("ACK" + seq)).toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isPacketDropped() {
		if ((Math.random() * 100) > noise) {
			return false;
		}
		return true;
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		this.wait();
	}

	/*
	 * 
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Server");
			(new Server(terminal, DEFAULT_PORT)).start();
			terminal.println("Program completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}