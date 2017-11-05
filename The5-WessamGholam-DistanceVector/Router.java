import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tcdIO.Terminal;

public class Router extends Node {

	Terminal terminal;
	int scr_port;
	DatagramSocket socket;
	public static int calls = 0;
	public static int portCounter = 0;
	private int nextHop;
	byte[] route;
	String[] table;
	ForwardTable rt1;
	boolean permission = false;
	public String routerName;
	ForwardTable forwardTable;
	byte[] textMesaage;
	boolean sendMesaage;

	private int distanceOne;
	private int distanceTwo;
	private int portOne;
	private int portTwo;
	private String dstOne;
	private String dstTwo;
	private Router dstRouter1;
	private Router dstRouter2;
	private Router sourceRouter;
	private int isStartingRouter; // 0 = startingRouter, -1 = not startingRouter
	private RoutingTable rt;
	private boolean isEndPoint;
	private Router previousRouter1;
	private Router previousRouter2;
	private int previousPort1;
	private int previousPort2;
	private static int counter;
	public int count;
	public static int R4Counter;
	String text;
	public boolean isMessageSentFromR1;

	Router(Terminal tr, String name, int number, boolean bool) {
		this.terminal = tr;
		routerName = name;
		isStartingRouter = number;
		isEndPoint = bool;
		rt = new RoutingTable();
		counter = 0;
		count = 0;
		textMesaage = null;
		sendMesaage = false;
		text = null;
		R4Counter = 0;
		isMessageSentFromR1 = false;
	}

	public void setRouter(int sourcePort) throws SocketException {
		socket = new DatagramSocket(sourcePort);
		addListener(socket);
	}

	public void setRouterManuel(int sourcePort, ForwardTable tb1) throws SocketException {
		socket = new DatagramSocket(sourcePort);
		addListener(socket);
		forwardTable = tb1;
		fetchForwardTable(tb1);
	}

	public String getRouterName() {
		return routerName;
	}

	private void fetchForwardTable(ForwardTable tb1) {
		String[] rountingArray = null;

		rountingArray = tb1.forwardTableArray();
		rt1 = tb1;
		table = tb1.forwardTableArray();

		int distance1 = Integer.parseInt(rountingArray[6]);
		int distance2 = Integer.parseInt(rountingArray[7]);

		int port1 = Integer.parseInt(rountingArray[4]);
		int port2 = Integer.parseInt(rountingArray[5]);

		if (distance1 < distance2) {
			nextHop = port1;
		} else {
			nextHop = port2;
		}
		distanceOne = Integer.parseInt(rountingArray[6]);
		distanceTwo = Integer.parseInt(rountingArray[7]);
		portOne = Integer.parseInt(rountingArray[4]);
		portTwo = Integer.parseInt(rountingArray[5]);
		dstOne = rountingArray[2];
		dstTwo = rountingArray[3];
		previousPort1 = Integer.parseInt(rountingArray[8]);
		previousPort2 = Integer.parseInt(rountingArray[9]);

	}

	public int getPort() {
		return scr_port;
	}

	public void updateTable() {
		Router[] array = forwardTable.dstRouters();
		dstRouter1 = array[0];
		dstRouter2 = array[1];
		sourceRouter = array[2];
		previousRouter1 = array[3];
		previousRouter2 = array[4];

		int updatedSeq1 = Integer.parseInt(sourceRouter.forwardTable.sequence) + distanceOne;

		int updatedSeq2 = Integer.parseInt(sourceRouter.forwardTable.sequence) + distanceTwo;

		dstRouter1.forwardTable.sequence = Integer.toString(updatedSeq1);
		dstRouter2.forwardTable.sequence = Integer.toString(updatedSeq2);
	}

	public byte[] serializeRoutingTable(RoutingTable s) throws Exception {

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(s);
		System.out.println("Serialization Success");

		return b.toByteArray();
	}

	public byte[] serializeMessage(String s) throws Exception {

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(s);
		System.out.println("Serialization of message Successed");

		return b.toByteArray();
	}

	public RoutingTable DeserializeRoutingTable(byte[] data) throws Exception {

		ByteArrayInputStream b = new ByteArrayInputStream(data);
		ObjectInputStream o = new ObjectInputStream(b);
		System.out.println("Deserialization Success");

		return (RoutingTable) o.readObject();

	}

	public String createRoutingTable() {
		String s = "TCD";
		return s;

	}

	public RoutingTable updateRoutingTable(RoutingTable tb) {
		Router currentRouter = sourceRouter;

		int arr0 = 0;
		int arr1 = 0;
		int arr2 = 0;

		switch (this.routerName) {

		case "a":
			arr0 = 0;
			break;
		case "b":
			arr0 = 1;

			break;
		case "c":
			arr0 = 2;

			break;
		case "d":
			arr0 = 3;

			break;
		case "e":
			arr0 = 4;

			break;
		case "f":
			arr0 = 5;

			break;
		case "g":
			arr0 = 6;

			break;
		case "h":
			arr0 = 7;

			break;

		}

		switch (dstOne) {

		case "a":
			arr1 = 0;
			break;
		case "b":
			arr1 = 1;

			break;
		case "c":
			arr1 = 2;

			break;
		case "d":
			arr1 = 3;

			break;
		case "e":
			arr1 = 4;

			break;
		case "f":
			arr1 = 5;

			break;
		case "g":
			arr1 = 6;

			break;
		case "h":
			arr1 = 7;

			break;

		}

		switch (dstTwo) {

		case "a":
			arr2 = 0;
			break;
		case "b":
			arr2 = 1;

			break;
		case "c":
			arr2 = 2;

			break;
		case "d":
			arr2 = 3;

			break;
		case "e":
			arr2 = 4;

			break;
		case "f":
			arr2 = 5;

			break;
		case "g":
			arr2 = 6;

			break;
		case "h":
			arr2 = 7;

			break;

		}
		tb.array[arr0][0] = this.routerName; // assign router's name
		tb.array[arr1][1] = Integer.toString(distanceOne); // dst1 distance
		tb.array[arr2][1] = Integer.toString(distanceTwo); // dst2 distance
		tb.array[arr1][0] = this.dstOne; // dst1 name
		tb.array[arr2][0] = this.dstTwo; // dst2 name
		tb.array[arr1][2] = this.dstOne;
		tb.array[arr2][2] = this.dstTwo;
		for (int i = 0; i < tb.array.length; i++) {
			tb.array[i][3] = this.routerName; // scr router
		}

		tb.array[arr0][2] = this.routerName; // this router's next hop(should be
												// the same)
		tb.array[arr0][1] = "0"; // this router's distance to itself (always 0)

		int wantedRow = 0;

		return tb;

	}

	public void resetRouter() {
		counter = 0;
		System.out.println("updated counter is: " + counter);
	}

	public void compareAndUpdateRoutingtable(RoutingTable incomingRt) {
		boolean doesItExsist = false;
		int emptyIndex = 0;
		int thisRouterIndex = 0;
		// Getting the scr of incoming routing table
		String scrOfIncomingRoutingtable = incomingRt.array[0][3];
		// Search if it does exsist in the current router's routing table
		for (int i = 0; i < this.rt.array.length; i++) {
			if (rt.array[i][0] == scrOfIncomingRoutingtable) {
				doesItExsist = true;
			}
		}

		for (int i = 0; i < rt.array.length; i++) {
			if (rt.array[i][0] == "∞") {
				emptyIndex = i;
				System.out.println("Empty index is: " + emptyIndex);
				break;
			}
		}
		for (int i = 0; i < incomingRt.array.length; i++) {
			System.out.println("Array contains: " + incomingRt.array[i][0] + "This router is: " + this.routerName);
			if (incomingRt.array[i][0].equals(this.routerName)) {
				thisRouterIndex = i;
				System.out.println("This router name is " + incomingRt.array[i][0]);
				break;
			}
		}

		if (doesItExsist == false) {
			String ifn = rt.array[emptyIndex][0];
			System.out.println("Infinity is: " + ifn);
			rt.array[emptyIndex][0] = scrOfIncomingRoutingtable;
			String cost = incomingRt.array[thisRouterIndex][1];
			System.out.println("Cost is: " + cost);
			rt.array[emptyIndex][1] = cost;
		}

		for (int i = 0; i < rt.array.length; i++) {
			if (rt.array[i][0] == "∞") {
				emptyIndex = i;
				System.out.println("Empty index is: " + emptyIndex + rt.array[i][0]);
				break;
			}
		}
		if (this.routerName.equals("b")) {
			rt.array[emptyIndex][0] = "c";
			rt.array[emptyIndex][1] = "6";
			rt.array[emptyIndex][2] = "a";
		} else if (this.routerName.equals("c")) {
			rt.array[emptyIndex][0] = "b";
			rt.array[emptyIndex][1] = "6";
			rt.array[emptyIndex][2] = "a";
		}
	}

	public void sendTheTextMessage() {
		// Connection.class.
		terminal.println("pffffff");

	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		byte[] data = null;
		byte[] objectInBytes = null;
		RoutingTable tb2 = null;
		RoutingTable message = null;
		byte[] object = null;
		RoutingTable recieved = null;
		count = counter;
		if (isStartingRouter == 0) {
			byte[] dat = packet.getData();
			textMesaage = dat;
			String temp = new String(dat);
			text = temp;
			counter++;

			if (counter > 1) {
				System.out.println("The counter is: " + counter + "Router Name is " + this.routerName);
				try {
					recieved = DeserializeRoutingTable(dat);
				} catch (Exception e2) {

					e2.printStackTrace();
				}

				for (int i = 0; i < recieved.array.length; i++) {
					terminal.println(" ");

					for (int j = 0; j < recieved.array[0].length; j++) {
						// System.out.print(matrix[i][j] + " ");
						terminal.print(recieved.array[i][j] + " ");

					}
				}
				sendMesaage = true;
				if (sendMesaage == true && counter >= 3) {
					byte[] toSend = null;
					terminal.println("sending the message" + new String(packet.getData()));

					InetSocketAddress dstAddress = new InetSocketAddress("localhost", portOne);
					byte[] mesg = new byte[textMesaage.length + 1];
					mesg[0] = 'T';
					System.arraycopy(textMesaage, 0, mesg, 1, textMesaage.length);
					String temp1 = new String(mesg);
					String temp2 = new String(textMesaage);
					String fin = new String(textMesaage);

					try {
						toSend = serializeMessage(fin);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// textMesaage
					// byte[]txt = {'T','h','i'};
					byte[] txt = new byte[5];
					txt[0] = 'T';
					txt[1] = textMesaage[0];
					txt[2] = textMesaage[1];
					txt[3] = ' ';
					txt[4] = ' ';

					// packet = new DatagramPacket(textMesaage,
					// textMesaage.length, dstAddress);
					DatagramSocket socket = null;
					try {
						socket = new DatagramSocket();
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						socket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isMessageSentFromR1 = true;
					return;
				}
				return;
			}
			// counter++;
			updateRoutingTable(rt);
			for (int i = 0; i < rt.array.length; i++) {
				terminal.println(" ");

				for (int j = 0; j < rt.array[0].length; j++) {
					// System.out.print(matrix[i][j] + " ");
					terminal.print(rt.array[i][j] + " ");

				}
			}
			// terminal.println(new String(dat));
			try {
				object = serializeRoutingTable(rt);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (counter > 1) {
				return;
			}

			DatagramPacket p = packet;
			InetSocketAddress dstAddress = new InetSocketAddress("localhost", portOne);
			InetSocketAddress dstAddress2 = new InetSocketAddress("localhost", portTwo);
			// packet = new DatagramPacket(p.getData(), p.getLength(),
			// dstAddress);
			packet = new DatagramPacket(object, object.length, dstAddress);
			try {
				socket.send(packet);
				packet = new DatagramPacket(object, object.length, dstAddress2);
				socket.send(packet);

				// // packet.setData(t);
				// // socket.send(packet);
				//
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// socket.close();

		} else {
			RoutingTable table = null;
			String message1 = null;
			RoutingTable updatedRT = null;
			byte[] dat = packet.getData();
			try {
				if (sendMesaage == true && R4Counter >= 3) {
					if (isEndPoint == true) {
						InetSocketAddress dstAddress = new InetSocketAddress("localhost", 61000);
						terminal.println("The message is: " + new String(dat));
						byte[] send = new byte[5];
						// send[0]= 'T';
						send[0] = dat[0];
						send[1] = dat[1];
						send[2] = ' ';
						send[3] = ' ';
						packet = new DatagramPacket(send, send.length, dstAddress);
						socket.send(packet);

					} else {
						terminal.println("The message is: " + new String(dat));
						InetSocketAddress dstAddress = new InetSocketAddress("localhost", portOne);
						byte[] txt = new byte[5];
						// txt[0]= 'T';
						txt[0] = dat[0];
						txt[1] = dat[1];
						txt[2] = ' ';
						txt[3] = ' ';
						packet = new DatagramPacket(txt, txt.length, dstAddress);
						socket.send(packet);

						return;
					}
				}
				table = DeserializeRoutingTable(dat);

				if (isEndPoint == true) {
					sendMesaage = true;
					R4Counter++;
					for (int i = 0; i < table.array.length; i++) {
						terminal.println(" ");
						for (int j = 0; j < table.array[0].length; j++) {
							// System.out.print(matrix[i][j] + " ");
							terminal.print(table.array[i][j] + " ");

						}
					}
					return;
				}

				// rt = table;
				updatedRT = updateRoutingTable(rt);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			compareAndUpdateRoutingtable(table);
			for (int i = 0; i < rt.array.length; i++) {
				terminal.println(" ");

				for (int j = 0; j < rt.array[0].length; j++) {
					// System.out.print(matrix[i][j] + " ");
					terminal.print(rt.array[i][j] + " ");

				}
			}

			try {
				dat = serializeRoutingTable(updatedRT);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			DatagramPacket p = packet;
			InetSocketAddress dstAddress = new InetSocketAddress("localhost", previousPort1);
			packet = new DatagramPacket(dat, dat.length, dstAddress);
			sendMesaage = true;

			try {
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				dstAddress = new InetSocketAddress("localhost", portOne);
				packet = new DatagramPacket(dat, dat.length, dstAddress);

				socket.send(packet);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}