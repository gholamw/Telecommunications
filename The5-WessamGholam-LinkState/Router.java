package link;

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

import org.apache.commons.lang3.SerializationUtils;

import tcdIO.Terminal;

public class Router extends Node {

	//
	// class RoutingTable implements Serializable{
	// int[][] array;
	// public RoutingTable(){
	// array = new int[7][7];
	// }
	// }

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

	// ///Global Variables/////

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
	public RoutingTable rtable;
	private Smartphone phone;
	private boolean knowingNetwroksTopology;

	// ///Global Variables/////

	Router(Terminal tr, String name, int number, boolean bool, Smartphone sp) {
		this.terminal = tr;
		routerName = name;
		isStartingRouter = number;
		isEndPoint = bool;
		rtable = new RoutingTable();
		phone = sp;
		knowingNetwroksTopology = false;

	}

	public void setRouter(int sourcePort) throws SocketException {
		socket = new DatagramSocket(sourcePort);
		addListener(socket);
	}

	public void setRouterManuel(int sourcePort, ForwardTable tb1)
			throws SocketException {
		socket = new DatagramSocket(sourcePort);
		addListener(socket);
		forwardTable = tb1;
		fetchForwardTable(tb1);
	}

	public String getRouterName() {
		return routerName;
	}

	private void fetchForwardTable(ForwardTable tb1) {
		String[] rountingArray = tb1.forwardTableArray();
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

		// byte[]r = routingTableToArrayOfBytes(rountingArray,
		// rountingArray.length);
		// route = r;

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
		// System.out.println("router" + this.routerName + "prev dst 1" +
		// array[3]);

		int updatedSeq1 = Integer.parseInt(sourceRouter.forwardTable.sequence)
				+ distanceOne;

		int updatedSeq2 = Integer.parseInt(sourceRouter.forwardTable.sequence)
				+ distanceTwo;

		dstRouter1.forwardTable.sequence = Integer.toString(updatedSeq1);
		dstRouter2.forwardTable.sequence = Integer.toString(updatedSeq2);
	}

	public byte[] serializeRoutingTable(RoutingTable s) throws Exception {
		// RoutingTable rt
		// FileOutputStream fout = new FileOutputStream("file.txt");
		// ObjectOutputStream out = new ObjectOutputStream(fout);
		// out.writeObject(rt);
		// out.flush();
		// String s = "TCD";
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(s);
		System.out.println("Serialization Success");

		return b.toByteArray();
	}

	public RoutingTable DeserializeRoutingTable(byte[] data) throws Exception {
		// ObjectInputStream in = new ObjectInputStream(new
		// FileInputStream("file.txt"));
		// RoutingTable s = (RoutingTable) in.readObject();
		// System.out.println("Deserialization Success");
		// in.close();
		ByteArrayInputStream b = new ByteArrayInputStream(data);
		ObjectInputStream o = new ObjectInputStream(b);
		System.out.println("Deserialization Success");
		// return (String) o.readObject();
		return (RoutingTable) o.readObject();

	}

	public String createRoutingTable() {
		String s = "TCD";
		return s;

	}

	public RoutingTable updateRoutingTable(RoutingTable tb) {
		Router currentRouter = sourceRouter;
		// Router source = null;
		// private Router dstRouter1;
		// private Router dstRouter2;
		// String dst1 = dstRouter1.routerName;
		// String dst2 = dstRouter2.routerName;
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

		tb.array[arr0][arr1] = distanceOne;
		tb.array[arr0][arr2] = distanceTwo;
		return tb;

	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		byte[] data = null;
		byte[] objectInBytes = null;
		RoutingTable tb2 = null;
		updateTable();

		// String tableString = table.toString();
		// String s=Arrays.toString(table).replaceAll("(\\[|\\]|,| )", "");
		// System.out.println("table" + s);
		// String str = "Your string";
		// byte[] t = tableString.getBytes();
		ArrayList<List<String>> twoDim = null;
		byte[] array1 = packet.getData();
		if (array1[0] == 'R') {
			terminal.print("Routing table has been recieved ");
			// if(previousPort1 != -1){
			// DatagramPacket p = packet;
			// InetSocketAddress dstAddress = new InetSocketAddress("localhost",
			// previousPort1);
			// // packet = new DatagramPacket(p.getData(), p.getLength(),
			// //dstAddress);
			// packet = new DatagramPacket(array1, array1.length , dstAddress);
			// try {
			// socket.send(packet);
			// //packet.setData(t);
			// //socket.send(packet);
			//
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			//
			//
			// if(previousPort2 != -1){
			// DatagramPacket p = packet;
			// InetSocketAddress dstAddress = new InetSocketAddress("localhost",
			// previousPort2);
			// //packet = new DatagramPacket(p.getData(), p.getLength(),
			// //dstAddress);
			// packet = new DatagramPacket(array1, array1.length , dstAddress);
			// try {
			// socket.send(packet);
			// //packet.setData(t);
			// //socket.send(packet);
			//
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			return;
		}
		if (isStartingRouter == 0) {
			// try {
			String s = createRoutingTable();
			RoutingTable tb = new RoutingTable();
			RoutingTable tb1 = updateRoutingTable(tb);
			try {
				objectInBytes = serializeRoutingTable(tb1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] objectInBytes2 = new byte[packet.getLength() + 1
					+ objectInBytes.length];
			byte[] packetContent = packet.getData();
			byte[] sourceRouterName = this.getRouterName().getBytes();
			System.out.println("length of source router name: "
					+ sourceRouterName.length + "content: "
					+ sourceRouterName[0]);
			byte[] arr = new byte[1 + objectInBytes.length];
			// arr[0] = 'R';
			// System.arraycopy(objectInBytes, 0, arr, 1, objectInBytes.length);

			// twoDim = createRoutingTable();
			// String s = "S";
			// data = SerializationUtils.serialize(s);

		} else {

			objectInBytes = packet.getData();
			try {
				// RoutingTable tb = DeserializeRoutingTable(objectInBytes);
				// String tb = DeserializeRoutingTable(objectInBytes);
				RoutingTable tb = DeserializeRoutingTable(objectInBytes);
				if (this.isEndPoint == false) {
					tb2 = updateRoutingTable(tb);
				}

				for (int i = 0; i < tb.array.length; i++) {
					terminal.println(" ");

					for (int j = 0; j < tb.array[0].length; j++) {
						// System.out.print(matrix[i][j] + " ");
						terminal.print(tb.array[i][j] + " ");

					}
				}
				terminal.println(" ");
				if (this.isEndPoint == false) {
					objectInBytes = serializeRoutingTable(tb2);
				} else {
					objectInBytes = serializeRoutingTable(tb);

					// RoutingTable routing = new RoutingTable();
					// routing = tb;

					if (previousRouter1 != null) {
						terminal.println("Previous router is: "
								+ previousRouter1.getRouterName());
						System.out.println("Previous router is: "
								+ previousRouter1.getRouterName());
						previousRouter1.rtable = tb;
					}

					// this.previousRouter1.
					
					for (int i = 0; i < this.previousRouter1.rtable.array.length; i++) {
						terminal.println(" start printing prev routing table ");

						for (int j = 0; j < this.previousRouter1.rtable.array[0].length; j++) {
							// System.out.print(matrix[i][j] + " ");
							terminal.print(tb.array[i][j] + " ");

						}
					}

				}



				byte[] arr = new byte[1 + objectInBytes.length];
				arr[0] = 'R';
				System.arraycopy(objectInBytes, 0, arr, 1, objectInBytes.length);

				//
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// data = packet.getData();
			// String mar = (String) SerializationUtils.deserialize(data);
			// data = mar.getBytes();

		}

		// ArrayList<List<String>> tableUpdated = updateRoutingTable(twoDim);
		//
		// for(int i=0; i<twoDim.size(); i++){
		// System.out.println(twoDim.get(i));
		// }

		// Uncomment when nessecary
		// byte[] message = packet.getData();
		// byte t[] = new byte[2];
		// t[0] = message[0];
		// t[1] = message[1];

		// packet.setData(t);

		System.out.println("here it comes:^)");
		if (this.isEndPoint == true) {
			//try {
				knowingNetwroksTopology = true;
				//phone.addSmartphone(52000, 57000);

				//phone.start();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return;
		}
		byte[] dat = packet.getData();
		terminal.println(new String(dat));
		// socket.close();
		// try {
		// socket = new DatagramSocket(56000);
		// } catch (SocketException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		DatagramPacket p = packet;
		InetSocketAddress dstAddress = new InetSocketAddress("localhost",
				nextHop);
		// packet = new DatagramPacket(p.getData(), p.getLength(), dstAddress);
		packet = new DatagramPacket(objectInBytes, objectInBytes.length,
				dstAddress);
		try {
			socket.send(packet);
			// packet.setData(t);
			// socket.send(packet);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.close();
		if (this.isEndPoint == true) {
			return;
		}

	}

	// private byte[] routingTableToArrayOfBytes(String[] rountingArray, int
	// size){
	// byte[] routing = new byte[size];
	//
	// for(int i=0; i<size; i++){
	// //routing[i] = rountingArray.getBytes();
	//
	// routing[i]=Byte.parseByte(rountingArray[i]);
	// }
	// return routing;
	// }

	// public ArrayList<List<String>> createRoutingTable() {
	// ArrayList<List<String>> twoDim = new ArrayList<List<String>>();
	// twoDim.add(new ArrayList<String>());
	// for (int i = 0; i < 7; i++) {
	// twoDim.get(0).add("");
	// }
	//
	// twoDim.get(0).set(1, routerName);
	// twoDim.get(0).set(2, dstOne);
	// twoDim.get(0).set(3, dstTwo);
	// // twoDim.add(new ArrayList<String>());
	// // twoDim.get(1).set(1, routerName);
	//
	// return twoDim;
	//
	// }
	//
	//
	//
	//
	//
	// public ArrayList<List<String>> updateRoutingTable(ArrayList<List<String>>
	// twoDim) {
	// twoDim.add(new ArrayList<String>());
	// for(int i=0; i<7; i++){
	// twoDim.get(twoDim.size()-1).add(Double.toString(Double.POSITIVE_INFINITY));
	//
	// }
	// twoDim.get(twoDim.size() - 1).set(0, routerName);
	// int rowIndex = twoDim.size() - 1;
	// int columnIndex1 = 0;
	// int columnIndex2 = 0;
	//
	// switch (dstOne) {
	// case "b":
	// twoDim.get(0).set(1, "b");
	// columnIndex1 = 1;
	// break;
	// case "c":
	// twoDim.get(0).set(2, "c");
	// columnIndex1 = 2;
	// break;
	// case "d":
	// twoDim.get(0).set(3, "d");
	// columnIndex1 = 3;
	// break;
	// case "e":
	// twoDim.get(0).set(4, "e");
	// columnIndex1 = 4;
	// break;
	// case "f":
	// twoDim.get(0).set(5, "f");
	// columnIndex1 = 5;
	// break;
	// case "g":
	// twoDim.get(0).set(6, "g");
	// columnIndex1 = 6;
	// break;
	// case "h":
	// twoDim.get(0).set(7, "h");
	// columnIndex1 = 7;
	// break;
	//
	// }
	//
	// switch (dstTwo) {
	// case "b":
	// twoDim.get(0).set(1, "b");
	// columnIndex2 = 1;
	// break;
	// case "c":
	// twoDim.get(0).set(2, "c");
	// columnIndex2 = 2;
	// break;
	// case "d":
	// twoDim.get(0).set(3, "d");
	// columnIndex2 = 3;
	// break;
	// case "e":
	// twoDim.get(0).set(4, "e");
	// columnIndex2 = 4;
	// break;
	// case "f":
	// twoDim.get(0).set(5, "f");
	// columnIndex2 = 5;
	// break;
	// case "g":
	// twoDim.get(0).set(6, "g");
	// columnIndex2 = 6;
	// break;
	// case "h":
	// twoDim.get(0).set(7, "h");
	// columnIndex2 = 7;
	// break;
	// }
	//
	//
	// twoDim.get(rowIndex).set(columnIndex1, Integer.toString(distanceOne));
	// twoDim.get(rowIndex).set(columnIndex2, Integer.toString(distanceTwo));
	// return twoDim;
	//
	// }

}
