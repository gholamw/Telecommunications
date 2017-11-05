package cs.tcd.ie;

import java.net.DatagramSocketImpl;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Class that replaces the send method of DatagramSocket with a lossy equivalent.
 *
 */
public class DatagramSocket extends java.net.DatagramSocket {

	private static int noise = 50;    // Level between 0 and 100 that determines at which rate packets are dropped
										// A value of 50 causes about 50% of the packets to be dropped.
										// A higher value cases more packets to be dropped.
	/**
	 * Default constructor that invokes the default constructor of the parent class.
	 * 
	 * @throws SocketException
	 */
	public DatagramSocket() throws SocketException {
		super();
	}

	/**
	 * Constructor that forwards the DatagramSocketImpl argument to the 
	 * corresponding constructor of the parent class.
	 * 
	 * @param arg0
	 * @throws SocketException
	 */
	protected DatagramSocket(DatagramSocketImpl arg0) throws SocketException {
		super(arg0);
	}

	/**
	 * Constructor that forwards the int argument to the 
	 * corresponding constructor of the parent class.
	 * 
	 * @param srcPort
	 * @throws SocketException
	 */
	protected DatagramSocket(int srcPort) throws SocketException {
		super(srcPort);
	}
	
	
	/**
	 * Sets the noise level of the socket.
	 * 
	 * @param noise New value for the noise level of the socket class.
	 */
	public void setNoise(int noise) {
		DatagramSocket.noise= noise;
	}
	
	
	/**
	 * Depending on a random number, the given packet will be send or dropped.
	 * 
	 * @param arg0 Packet to be sent
	 */
	public void send(DatagramPacket arg0) throws IOException {
		if ((Math.random()*100) > noise) {  // the packet will be send depending on a random number between 0 and 100
			super.send(arg0);
		}
		else {
			// System.out.println("** Packet dropped");
		}
	}
}