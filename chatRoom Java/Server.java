package chatRoom;

import java.io.*;
import java.net.*;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

class Server {

	public static Vector<ObjectOutputStream> clients = new Vector<ObjectOutputStream>();

	public static void main(String[] args) throws IOException, ClassNotFoundException, GeneralSecurityException {
		ServerSocket serverSocket = new ServerSocket(22222);
		System.out.println("Server ready...");

//		Key veriables
		Key AESKey;
		PrivateKey privateKey;
		PublicKey publicKey;
		PublicKey clientPub;

//	    AES key generator
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		AESKey = keyGenerator.generateKey();

//        Create RSA keys
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // KeySize
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();

		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Server accepted the client connection...");

			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				Server.clients.add(oos);
			} catch (IOException e) {
				e.printStackTrace();
			}

//			Accept client public key
			clientPub = (PublicKey) ois.readObject();

//			Send server public key
			oos.writeObject(publicKey);

//			Encrypt AES key with client public key
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, clientPub);
			byte[] encAESKey = cipher.doFinal(AESKey.getEncoded());
			
//			Send AES key
//			System.out.println(AESKey.toString());
			oos.writeObject(encAESKey);

			new ServerThread(socket, ois, oos, AESKey);
		}
	}
}