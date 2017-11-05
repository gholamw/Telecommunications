import java.awt.Dimension;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

import javax.activation.FileDataSource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.bouncycastle.util.encoders.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailApplication {
	public static void main(String[] args) throws GeneralSecurityException, IOException, ClassNotFoundException{
		userInterface();
		//sendEmail();
		//checkEmail();
	}



	public static void sendEmail(String sender, String password, String reciever, String subject, String body) throws GeneralSecurityException, IOException, ClassNotFoundException{
		String host = "smtp.gmail.com";
		String from = sender;
		String pass = password;
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true"); // added this line
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		String[] to = { reciever }; // added this line

		try {

			Session session = Session.getDefaultInstance(props, null);

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses

			for (int i = 0; i < to.length; i++) { // changed from a while loop

				toAddress[i] = new InternetAddress(to[i]);

			}

			System.out.println(Message.RecipientType.TO);

			for (int i = 0; i < toAddress.length; i++) { // changed from a while
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);

			}


			BouncyCastle bc = new BouncyCastle();

			KeyPair pair = bc.generate("", "");

			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			PublicKey pk = pair.getPublic();
			PrivateKey sk = pair.getPrivate();

			//System.out.println(pk.toString());
			//System.out.println(sk.toString());
			CreateCertificate cr = new CreateCertificate();
			X509Certificate myCert = cr.generateCertificate("gholamwessam@gmail.com", pair, 30, "SHA1withRSA");
			System.out.println(myCert.getIssuerX500Principal().toString());
			File dir = new File("C:\\Users\\gholamw\\Desktop\\EmailApplicationn\\Certs\\gholamwessam@gmail");
			dir.mkdirs();
			File statText = new File("C:\\Users\\gholamw\\Desktop\\EmailApplicationn\\Certs\\gholamwessam@gmail\\gholamwessam@gmail.txt");
            FileOutputStream is = new FileOutputStream(statText);
            
			FileOutputStream f = new FileOutputStream("C:\\Users\\gholamw\\Desktop\\EmailApplicationn\\Certs\\gholamwessam@gmail\\gholamwessam@gmail.txt");
			ObjectOutputStream outputStream = new ObjectOutputStream(f);
			outputStream.writeObject(myCert);

			String text = body;


			String encryptedData = bc.encrypt("", text);

			System.out.println("Text Encryted : " + encryptedData);

			//String decryptedData = bc.decrypt("", encryptedData);

			//System.out.println("Text Decypted : " + decryptedData);

			// create and fill the first message part
		    MimeBodyPart mbp1 = new MimeBodyPart();
		    mbp1.setText(encryptedData);
		    //message.setContent(encryptedData, "text/plain");
		 
		    
			FileInputStream fiss = null;  
			ObjectInputStream in = null;  
			FileInputStream fis111 = new FileInputStream("C:\\Users\\gholamw\\Desktop\\EmailApplicationn\\Certs\\gholamwessam@gmail\\gholamwessam@gmail.txt");  
             in = new ObjectInputStream(fis111);  
             X509Certificate cerrt  = (X509Certificate) in.readObject();  
             
		    // create the second message part
		    MimeBodyPart mbp2 = new MimeBodyPart();
		    mbp2.setText(cerrt.getPublicKey().toString());
		    mbp2.setFileName("Official Certificate");
		 
		    

		 
		    // create the Multipart and add its parts to it
		    Multipart mp = new MimeMultipart();
		    mp.addBodyPart(mbp1);
		    mp.addBodyPart(mbp2);
		 
		    // add the Multipart to the message
		    message.setContent(mp);
		 
		    // set the Date: header
		    message.setSentDate(new Date());
		    message.saveChanges();

			message.setSubject(subject);
			//message.setText(encryptedData);

			Transport transport = session.getTransport("smtp");

			transport.connect(host, from, pass);

			transport.sendMessage(message, message.getAllRecipients());

			transport.close();

		} catch (MessagingException mx) {

			mx.printStackTrace();

		}

	}
	
	public static void storeSecretKey(KeyPair key) throws IOException {

		FileOutputStream f = new FileOutputStream(new File("C:\\Users\\gholamw\\Desktop\\Objects.txt"));

		ObjectOutputStream o = new ObjectOutputStream(f);

		// Write objects to file

		f.write(Base64.encode(key.getPrivate().getEncoded()));

		o.close();

		f.close();

	}
	
	
	public static void checkEmail() throws NoSuchProviderException{
		  try {
			  
		      String host = "pop.gmail.com";// change accordingly
		      String mailStoreType = "pop3";
		      String username = "gholamwessam@gmail.com";// change accordingly
		      String password = "62016206201620";// change accordingly
		      
		      //create properties field
		      Properties properties = new Properties();
		      properties.put("mail.pop3.host", host);
		      properties.put("mail.pop3.port", "995");
		      properties.put("mail.pop3.starttls.enable", "true");
		      Session emailSession = Session.getDefaultInstance(properties);
		      
		      //create the POP3 store object and connect with the pop server
		      Store store = emailSession.getStore("pop3s");
		      store.connect(host, username, password);
		      
		      //create the folder object and open it
		      Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);

		      // retrieve the messages from the folder in an array and print it
		      Message[] messages = emailFolder.getMessages();
		      System.out.println("messages.length---" + messages.length);

		      for (int i = 0, n = messages.length; i < n; i++) {
		         Message message = messages[i];
		         String BodyOfEmail = message.getContent().toString();
		         //String[] array = BodyOfEmail.split("\r");
		         System.out.println("---------------------------------");
		         System.out.println("Email Number " + (i + 1));
		         System.out.println("Subject: " + message.getSubject());
		         System.out.println("From: " + message.getFrom()[0]);
		         System.out.println("Text: " + message.getContent().toString());
		         String decyptedMessage =  message.getContent().toString();
//		         String encryptedMessage = message.getContent().toString();

		         byte[] arr = new byte[1024];
		         //arr = array[0].getBytes();
		         //PrivateKey myDesKey = loadSecretKey();
		         BouncyCastle bc = new BouncyCastle();
		         
		       String  decrypted = bc.decrypt("", decyptedMessage);
		       System.out.println("Text Decryted : " + decrypted);
		      }



		      //close the store and folder objects

		      emailFolder.close(false);

		      store.close();



		      } catch (MessagingException e) {

		         e.printStackTrace();

		      } catch (Exception e) {

		         e.printStackTrace();

		      }

	}
	
	public static PrivateKey loadSecretKey() throws IOException, ClassNotFoundException{



		   FileInputStream fi = new FileInputStream(new File("C:\\Users\\gholamw\\Desktop\\Objects.txt"));
		   ObjectInputStream oi = new ObjectInputStream(fi);

		   // Read objects
		   PrivateKey key = (PrivateKey) oi.readObject();

		   oi.close();
		   fi.close();

		   return key;

	}
	
	public static void userInterface() throws FileNotFoundException, IOException, ClassNotFoundException, GeneralSecurityException{
		
		
		ArrayList<String> registeredUsers = null;
		File dir = new File("C:\\Users\\gholamw\\Desktop\\EmailApplicationn\\PublicKeys\\");
		dir.mkdirs();
		
		dir = new File("C:\\Users\\gholamw\\Desktop\\EmailApplicationn\\PrivateKeys\\");
		dir.mkdirs();
		//Custom button text
		Object[] options = {"Login",
		                    "Add user",
		                    "Remove user"};
		int n = JOptionPane.showOptionDialog(null,
		    "Please select a service ",
		    "Welcome to EmailoOo",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);
		
		
		if(n == 0){
			System.out.println("Login");
			JTextField username = new JTextField();
			JTextField password = new JPasswordField();
			Object[] message = {
			    "Email Address:", username,
			    "Password:", password
			};

			int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
			        System.out.println("Login successful");
			        System.out.println(username.getText());
			        System.out.println(password.getText());
			        
					Object[] options1 = {"Send Email",
		                    "Check Emails"};
		int m = JOptionPane.showOptionDialog(null,
		    "Please select a service ",
		    "Welcome to EmailoOo",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options1,
		    options1[1]);
			
			System.out.println(m);
			if(m == 0){
				JTextField reciever = new JTextField();
				JTextField subject = new JTextField();
				//JTextField emailBody = new JTextField();
				JTextArea emailBody = new JTextArea(5, 30);
				JTextArea ta = new JTextArea();
				JScrollPane sp = new JScrollPane(ta);  

				// Add the scroll pane into the content pane
				JFrame f = new JFrame();
				f.getContentPane().add(sp); 
				
				JTextArea textArea = new JTextArea("");
				JScrollPane scrollPane = new JScrollPane(textArea);  
				textArea.setLineWrap(true);  
				textArea.setWrapStyleWord(true); 
				scrollPane.setPreferredSize( new Dimension( 500, 500 ) );

				Object[] messages = {
				    "reciever:", reciever,
				    "subject:", subject,
				    "Email Body:", scrollPane
				};


				
				int optionss = JOptionPane.showConfirmDialog(null, messages, "Login", JOptionPane.OK_OPTION);
				System.out.println("Optionssss : " + optionss );
				if(optionss == 0){
					String recieverEmail = reciever.getText();
					String emailSubject = subject.getText();
					String emailBodyText = textArea.getText();
					System.out.println("Reciever : " + recieverEmail);
					System.out.println("emailSubject : " + emailSubject);
					System.out.println("emailBodyText : " + emailBodyText);
					sendEmail(username.getText(), password.getText(),recieverEmail,emailSubject,emailBodyText);
				}
			}else{

			}


			} else {
			    System.out.println("Login canceled");
			}
		    

		}else if(n==1){
			System.out.println("Add user");
			FileInputStream fis = new FileInputStream(new File("C:\\Users\\gholamw\\Desktop\\ContactList\\List.txt"));  
			int iByteCount = fis.read(); 
			System.out.println("Byte count : " + iByteCount);
			
			if (iByteCount == -1){
				System.out.println("SOME ERRORS!");
				registeredUsers = new ArrayList<String>();
			   
			}else{
				 System.out.println("NO ERRORS!");
			}
				FileInputStream fiss = null;  
				ObjectInputStream in = null;  
	             fis = new FileInputStream("C:\\Users\\gholamw\\Desktop\\ContactList\\List.txt");  
	             in = new ObjectInputStream(fis);  
	             registeredUsers = (ArrayList) in.readObject();  
	             System.out.println(registeredUsers.toString());
	             if(!registeredUsers.contains("gholamwessafffm@gmail.com")){
	            	 registeredUsers.add("gholamwessafffm@gmail.com");
	             }
			//registeredUsers.add("gholamwessam@gmail.com");
			FileOutputStream f = new FileOutputStream("C:\\Users\\gholamw\\Desktop\\ContactList\\List.txt");
			ObjectOutputStream outputStream = new ObjectOutputStream(f);
			outputStream.writeObject(registeredUsers);
		}else{
			System.out.println("Remove user");

		}
		System.out.println(n);
	}
	
	
}



