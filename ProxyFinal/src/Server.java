import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server extends Thread {
	private static final int BUFFER_SIZE = 32768;
	static ArrayList<String> blockedWebsites = new ArrayList<String>();
	static Hashtable<Integer, byte[]> cachedWebsites = new Hashtable<Integer, byte[]>();
	static boolean cached = false;
	static String folderPath = "C:"+File.separator +"Users"+File.separator+"gholamw"+File.separator+"Desktop"+File.separator+"Java"; 
	boolean file = new File(folderPath).mkdir();

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		System.out.println("Please enter websites you wish to block : ");


		while(!in.hasNext("stop")){
			blockedWebsites.add(in.next());
			for(int i=0; i<blockedWebsites.size(); i++){
				System.out.println(i + ")" + " " + blockedWebsites.get(i));
			}
		}

		System.out.println("Finishes");
		(new Server()).run();
	}

	public Server() {
		super("Server Thread");

	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(9000)) {
			Socket socket;

			try {
				while ((socket = serverSocket.accept()) != null) {
					(new Handler(socket)).start();
				}
			} catch (IOException e) {
				e.printStackTrace(); // TODO: implement catch
			}
		} catch (IOException e) {
			e.printStackTrace(); // TODO: implement catch
			return;
		}
	}

	public class Handler extends Thread {


		private final Socket clientSocket;
		private boolean previousWasR = false;

		public Handler(Socket clientSocket) {
			this.clientSocket = clientSocket;

		}

		@Override
		public void run() {
			Socket socket = null;
			try {
				for(int i =0; i<blockedWebsites.size(); i++){
					System.out.println(i + " " + "Website blocked" + " :" + blockedWebsites.get(i));
				}

				String request[] = null;
				String header[] = null;
				header = getHeader(clientSocket, false);

				for (int i = 0; i < header.length; i++) {
					System.out.println(header[i]);
				}
				
				System.out.println("Request : " + "   " + header[0]);
				if (header[0].contains("GET")) {
					httpConnection(clientSocket, header[0], header[1]);
				} else if (header[0].contains("CONNECT")) {

					final Socket forwardSocket;
					final Socket socket1;
					String[] arr = header[0].split(" ");
					String[] arr2 = arr[1].split(":");
					int portSecured = Integer.parseInt(arr2[1]);
					System.out.println("Host" + " " + arr2[0]);
					System.out.println("PortNumber" + " " + portSecured);
					socket1= new Socket(arr2[0].trim(), portSecured);
					System.out.println("HEEEREEE " + arr2[0].trim() + "   " +  portSecured);
					
					PrintWriter outtt = new PrintWriter(clientSocket.getOutputStream());
					System.out.println("Recieving .......");
					outtt.println("HTTP/1.1 OK 200 Connection established\r\n");
					outtt.flush();


					Thread remoteToClient = new Thread() {
						@Override
						public void run() {
							pingPongDataThread(socket1, clientSocket);
						}
					};
					remoteToClient.start();
					try {
						//if (previousWasR) {
						int read = clientSocket.getInputStream().read();
						if (read != -1) {
							if (read != '\n') {
								socket1.getOutputStream().write(read);
							}
							pingPongDataThread(clientSocket, socket1);
						} else {
							if (!socket1.isOutputShutdown()) {
								socket1.shutdownOutput();
							}
							if (!clientSocket.isInputShutdown()) {
								clientSocket.shutdownInput();
							}
						}
						//} else {
						//}
					} finally {
						try {
							remoteToClient.join();
						} catch (InterruptedException e) {
							e.printStackTrace();  // TODO: implement catch
						}
					}

					socket.close();
				}

			}catch(IOException e3){
				
			}
		}


		private void pingPongDataThread(Socket inputSocket, Socket outputSocket) {
			try {
				InputStream inputStream = inputSocket.getInputStream();
				try {
					OutputStream outputStream = outputSocket.getOutputStream();
					try {
						byte[] buffer = new byte[4096];
						int read;
						do {
							read = inputStream.read(buffer);
							if (read > 0) {
								outputStream.write(buffer, 0, read);
								if (inputStream.available() < 1) {
									outputStream.flush();
								}
							}
						} while (read >= 0);
					} finally {
						if (!outputSocket.isOutputShutdown()) {
							outputSocket.shutdownOutput();
						}
					}
				} finally {
					if (!inputSocket.isInputShutdown()) {
						inputSocket.shutdownInput();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();  // TODO: implement catch
			}
		}

		private void readFromUser(Scanner in) throws InterruptedException {
			if (in.hasNext()) {
				String website = in.next();
				blockedWebsites.add(website);
				for(int i =0; i<blockedWebsites.size(); i++){
					System.out.println(i + " " + "Website blocked" + " :" + blockedWebsites.get(i));
				}

			}
		}

		private String[] getHeader(Socket socket, boolean getHeader) throws IOException {
			String[] data = new String[5];
			InputStream in = socket.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in));
			String inputLine = "";
			int counter = 0;
			while ((inputLine = rd.readLine()) != null) {
				if (counter == 4)
					return data;
				try {
				} catch (Exception e) {
					break;
				}
				data[counter] = inputLine.toString();

				counter++;
			}
			return data;

		}



		private void httpConnection(Socket socket, String request, String hostName) {
			try {

				String[] host = hostName.split(":");
				boolean isItBlocked = false;



				for (int i = 0; i < blockedWebsites.size(); i++) {
					if (blockedWebsites.get(i).equals(host[1].trim())) {
						isItBlocked = true;
					}
				}

				if (!isItBlocked) {
					InputStream input = null;
					//System.out.println(request);
					String[] arr = request.split(" ");
					//System.out.println(arr[1]);
					byte[] data = new byte[4032];
					URL myUrl = new URL(arr[1]);
					URLConnection connection = myUrl.openConnection();
					HttpURLConnection conn = (HttpURLConnection) connection;
					conn.getContentType();
					//conn.setAllowUserInteraction(true);
					//conn.getResponseMessage();
					input = conn.getInputStream();
					BufferedReader rd = new BufferedReader(new InputStreamReader(input));
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());



					File f = new File(folderPath+File.separator+host[1].trim()+File.separator+host[1].trim()+".html");
					if(f.exists() == true){

						System.out.println("YES CACHED");
						Path path = Paths.get(folderPath+File.separator+host[1].trim()+File.separator+host[1].trim()+".html");
						byte[] dataFromFile = Files.readAllBytes(path);
						int index = input.read( dataFromFile, 0, dataFromFile.length );
						out.write( dataFromFile, 0, index );
						out.flush();
						out.close();

						return;
					}

					System.out.println("THIS MEANS IT IS GOING TO THE SERVER !!");
					byte by[] = new byte[ 32768 ];
					int index = input.read( by, 0, 32768 );
					while ( index != -1 )
					{
						out.write( by, 0, index );
						index = input.read( by, 0, 32768 );
					}

					out.flush();
					out.close();
					caching(by,host[1].trim());
				} else {
					InputStream input = null;
					PrintWriter outt = new PrintWriter(socket.getOutputStream(), true);
					//System.out.println("Website is BLOCKED" + "^^^^^^^^^");
					String noToWeb = "Forbidden";
					outt.write(noToWeb);
					outt.println("HTTP/1.1 403 Connection established\n");
					outt.flush();
					outt.close();

				}

			} catch (MalformedURLException e) {
				System.err.println("IndexOutOfBoundsException: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Caught IOException: " + e.getMessage());

			}
		}

		private void caching(byte[] html, String hostName) throws IOException{

			String Path = folderPath+File.separator+hostName+File.separator+hostName+".html"; 
			File f = new File(Path);
			f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileOutputStream out = new FileOutputStream(Path);
			out.write(html);
			out.close();
		}
	}

	public class ClientToServerThread extends Thread{
		InputStream client;
		OutputStream server;
		public ClientToServerThread(InputStream client, OutputStream server) {
			super("ClientToServerThread");
			this.client = client;
			this.server = server;
		}

		@Override
		public void run(){
			byte[] data = new byte[BUFFER_SIZE];

			try {
				int lastTimeRead = (int) System.currentTimeMillis();
				int currentTime = 0;
				while(true){
					if(client.available()>0){
						int num = client.read(data, 0, data.length);
						server.write(data, 0, num);
						lastTimeRead = (int) System.currentTimeMillis();
						System.out.println("iNSIDE cLIEN tO sERVER thREAD 1");
					}
					currentTime = (int) System.currentTimeMillis();;
					if(currentTime - lastTimeRead > 10*1000)
						break;
				}

				System.out.println("Exit thREAD 1");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public class ServerToClientThread extends Thread{
		InputStream server;
		OutputStream client;
		public ServerToClientThread(InputStream server, OutputStream client) {
			super("ServerToClientThread");
			this.client = client;
			this.server = server;
		}

		@Override
		public void run(){
			byte[] data = new byte[BUFFER_SIZE];

			try {
				int lastTimeRead = (int) System.currentTimeMillis();
				int currentTime = 0;
				while(true){
					if(server.available()>0){
						int num = server.read(data, 0, data.length);
						client.write(data, 0, num);
						System.out.println("iNSIDE  sERVER tO cLIENT thREAD 2");
						lastTimeRead = (int) System.currentTimeMillis();
					}

					currentTime = (int) System.currentTimeMillis();;
					if(currentTime - lastTimeRead > 10*1000)
						break;
				}

				System.out.println("Exit thREAD 2");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
