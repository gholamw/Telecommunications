
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

public abstract class Node {
	static final int PACKETSIZE = 65536;

	transient DatagramSocket socket;
	ArrayList<Listener> listener;
	CountDownLatch latch;
	public static int counter = 0;

	Node() {
		listener = new ArrayList<Listener>();
	}

	public void addListener(DatagramSocket socket) {
		Listener l;

		l = new Listener(socket);
		listener.add(l);
		l.setDaemon(true);
		l.start();
	}

	public abstract void onReceipt(DatagramPacket packet);

	/**
	 *
	 * Listener thread
	 * 
	 * Listens for incoming packets on a datagram socket and informs registered
	 * receivers about incoming packets.
	 */
	class Listener extends Thread {

		transient DatagramSocket socket;

		Listener(DatagramSocket socket) {
			this.socket = socket;
		}

		/*
		 * Listen for incoming packets and inform receivers
		 */
		public void run() {
			try {
				// Endless loop: attempt to receive packet, notify receivers,
				// etc
				while (true) {
					counter++;
					DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
					socket.receive(packet);

					onReceipt(packet);
				}

			} catch (Exception e) {
				if (!(e instanceof SocketException))
					e.printStackTrace();
			}
		}
	}
}
