package chatRoom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class WritterThread extends Thread {
	Socket socket;
	ObjectOutputStream oos;
	String name;
	Key AESKey;

	public WritterThread(String name, Socket socket, ObjectOutputStream oos, Key AESKey) {
		this.socket = socket;
		this.oos = oos;
		this.name = name;
		this.AESKey = AESKey;
		this.start();
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			// System.out.print("Me: ");
			String clientMsg = sc.nextLine();
			clientMsg = name + ": " + clientMsg;
			clientMsg = clientMsg.trim();

			Cipher cipherAES = null;
			byte[] encMsg = null;

//			Send to the server
			if (clientMsg != null) {
				try {
					cipherAES = Cipher.getInstance("AES");
					cipherAES.init(Cipher.ENCRYPT_MODE, AESKey);
					encMsg = cipherAES.doFinal(clientMsg.getBytes());
					oos.writeObject(encMsg);
				} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
						| IllegalBlockSizeException | BadPaddingException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String lastWord = clientMsg.substring(clientMsg.lastIndexOf(" ")+1);
				if (lastWord.equalsIgnoreCase("exitc")) {
					try {
						oos.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}

//			try {
//				oos.writeObject(name + ": " +clientMsg);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}	

		}
	}

}
