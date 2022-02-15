package chatRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ServerThread extends Thread {
	Socket socket;
	ObjectOutputStream tempOOS;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	Key AESKey;

	ServerThread(Socket socket, ObjectInputStream ois, ObjectOutputStream oos, Key AESKey) {
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
		this.AESKey = AESKey;
		this.start();
	}

	public void run() {

		while (true) {
			
//			Read from the client
			byte[] encClientMsg = null;
			String clientMsg = null;
			Cipher cipherClientAES = null;
			try {
				encClientMsg = (byte[]) ois.readObject();
				if (encClientMsg != null) {
					cipherClientAES = Cipher.getInstance("AES");
					cipherClientAES.init(Cipher.DECRYPT_MODE, AESKey);
					clientMsg = new String(cipherClientAES.doFinal(encClientMsg));
					clientMsg = clientMsg.trim();
				}
			} catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(clientMsg);
			
			
			String lastWord = clientMsg.substring(clientMsg.lastIndexOf(" ")+1);
			if (lastWord.equalsIgnoreCase("exitc")) {
				try {
//					Remove the instance form client vector
					Server.clients.remove(Server.clients.indexOf(oos));
					
//					Send to the own reader thread to terminate 
					String terminateMsg = clientMsg;
					Cipher cipherServerAES = null;
					byte[] encServerMsg = null;
					try {
						cipherServerAES = Cipher.getInstance("AES");
						cipherServerAES.init(Cipher.ENCRYPT_MODE, AESKey);
						encServerMsg = cipherServerAES.doFinal(terminateMsg.getBytes());
						oos.writeObject(encServerMsg);
					} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
							| IllegalBlockSizeException | BadPaddingException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
//					Close the socket
					ois.close();
					oos.close();
					socket.close();	
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}

			String serverMsg = clientMsg;
			// send to the client
			if (serverMsg != null) {
				try {
					for (int i = 0; i < Server.clients.size(); i++) {
						tempOOS = Server.clients.get(i);
						if (!tempOOS.equals(oos)) {
							Cipher cipherServerAES = null;
							byte[] encServerMsg = null;
							try {
								cipherServerAES = Cipher.getInstance("AES");
								cipherServerAES.init(Cipher.ENCRYPT_MODE, AESKey);
								encServerMsg = cipherServerAES.doFinal(serverMsg.getBytes());
								tempOOS.writeObject(encServerMsg);
							} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
									| IllegalBlockSizeException | BadPaddingException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
//							tempOOS.writeObject(serverMsg);
//							tempOOS.writeObject("\r\n");
							tempOOS.flush();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
