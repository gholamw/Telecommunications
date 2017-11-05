package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;
//import tcd.lossy.DatagramSocket;

import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;

	Terminal terminal;
	public static int toTerminates = 1;
	
	/*
	 * 
	 */
	Server(Terminal terminal, int port) {
		try {
			this.terminal= terminal;
			socket= new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public void onReceipt(DatagramPacket packet) {
		int test =100;
		Random generator = new Random();
		int num = generator.nextInt(100);
		System.out.println("Number Generated : " + num);
		try {

//			while(num == test){
//				return;
//			}
			
			// if odd recieve and ACK, If even reject and re-send
			for(int i = 0; i > -1; i++)
			{
				if(toTerminates % 2 == 1){
					toTerminates++;
					System.out.println("Recieveing packet" + " " + new StringContent(packet));
					break;
					
				}else if(toTerminates % 2 == 0){
					toTerminates++;
					System.out.println("Not Recieveing packet" + " " + new StringContent(packet));
					return;
				}
			}

			StringContent content= new StringContent(packet);
			char[] arr = (content.toString()).toCharArray();
			int status = (int) arr[1];
			int newStatus = (status + 1) % 2;

			terminal.println(content.toString());

			DatagramPacket response;
			response= (new StringContent("ACK" + newStatus)).toDatagramPacket();
			response.setSocketAddress(packet.getSocketAddress());
			socket.send(response);
		}
		catch(Exception e) {e.printStackTrace();}
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
			Terminal terminal= new Terminal("Server");
			(new Server(terminal, DEFAULT_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
