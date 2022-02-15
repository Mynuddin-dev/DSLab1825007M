package chatRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ReaderThread extends Thread {
	Socket socket;
	ObjectInputStream ois;
	Key AESKey;

	public ReaderThread(Socket socket, ObjectInputStream ois, Key AESKey) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.ois = ois;
		this.AESKey = AESKey;
		this.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

//			Receive from server..
			byte[] encMsg = null;
			String clientMsg = null;
			Cipher cipherAES = null;
			try {
				encMsg = (byte[]) ois.readObject();
				if (encMsg != null) {
					cipherAES = Cipher.getInstance("AES");
					cipherAES.init(Cipher.DECRYPT_MODE, AESKey);
					clientMsg = new String(cipherAES.doFinal(encMsg));
				}
			} catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				System.out.println("Chat Service terminated successfully for me!!!");
			}

			if (clientMsg != null) {
				String lastWord = clientMsg.substring(clientMsg.lastIndexOf(" ") + 1);
				if (lastWord.equalsIgnoreCase("exitc")) {
					try {
						ois.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				System.out.println(clientMsg.trim());
			} else {
				try {
					ois.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}

	}

}
