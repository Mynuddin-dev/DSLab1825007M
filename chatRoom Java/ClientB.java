package chatRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ClientB {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, GeneralSecurityException, GeneralSecurityException {

		System.out.println("Client ready...");
		Socket socket = new Socket("127.0.0.1", 22222);
		System.out.println("Client connected to the server...");

//		Key veriables
		Key AESKey;
	    PrivateKey privateKey;
	    PublicKey publicKey;
	    PublicKey serverPub;
	    
//      Create RSA keys
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // KeySize
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
		
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
//		Send Client public key
		oos.writeObject(publicKey);
		
//		Receive Server public key
		serverPub = (PublicKey) ois.readObject();
		
//		Receive AES key
		byte[] encAESKey = (byte[]) ois.readObject();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] tempAESKey = cipher.doFinal(encAESKey);
        AESKey = new SecretKeySpec(tempAESKey, 0, tempAESKey.length, "AES");
//        System.out.println(AESKey.toString());
		
		
		String name = "Mynuddin";
		new WritterThread(name, socket, oos, AESKey);
		new ReaderThread(socket, ois, AESKey);
	}
}
