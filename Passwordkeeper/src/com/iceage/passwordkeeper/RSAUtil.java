package com.iceage.passwordkeeper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {
	private static String privateKey = "pri.key";
	private static String publicKey = "pub.key";

	private static String source = "source.txt";
	private static String encrypted = "en.txt";
	private static String dencrypted = "dec.txt";

	public static void main(String[] args) throws IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, FileNotFoundException {
		// System.out.println("asdaasd");
		// KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		// PrivateKey key = keys.getPrivate();
		// writeKey(key, new FileOutputStream("abc.txt"));
		//
		// Cipher cipher = Cipher.getInstance("RSA");
		// cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());
		// String a = "sunil";
		// byte[] encrypted = cipher.doFinal(a.getBytes());
		//
		// PrivateKey keyFromFile = (PrivateKey) readKey(new
		// FileInputStream("abc.txt"));
		// cipher.init(Cipher.DECRYPT_MODE, keyFromFile);
		// byte[] de = cipher.doFinal(encrypted);
		// String result = new String(de);
		// System.out.println(result);
		try {
			RSAUtil rsa = new RSAUtil();
			rsa.initKey();
			Cipher cipher = Cipher.getInstance("RSA");
			rsa.encrypt(cipher);
			rsa.decrypt(cipher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeKey(Key privateKey, OutputStream outputStream) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					outputStream);
			objectOutputStream.writeObject(privateKey);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writePrivateKey(Key key) throws FileNotFoundException {
		writeKey(key, new FileOutputStream(privateKey));
	}

	private static void writePublicKey(Key key) throws FileNotFoundException {
		writeKey(key, new FileOutputStream(publicKey));
	}

	private static Key readKey(InputStream inputStream) {
		Key key = null;
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
					inputStream);
			key = (Key) objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return key;
	}

	private static PrivateKey readPrivateKey() throws FileNotFoundException {
		return (PrivateKey) readKey(new FileInputStream(privateKey));
	}

	private static PublicKey readPublicKey() throws FileNotFoundException {
		return (PublicKey) readKey(new FileInputStream(publicKey));
	}

	private byte[] read(InputStream inputStream) {
		BufferedInputStream in = null;
		byte[] initdataholder = new byte[500000];
		byte[] content = null;
		try {
			in = new BufferedInputStream(inputStream);
			int bytesRead = in.read(initdataholder);
			content = new byte[bytesRead];
			content = Arrays.copyOf(initdataholder, bytesRead);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	private byte[] readsource() throws FileNotFoundException {
		return read(new FileInputStream(source));
	}

	private byte[] readEncrpt() throws FileNotFoundException {
		return read(new FileInputStream(encrypted));
	}

	private void write(OutputStream outStream, byte[] bs) {
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(outStream);
			out.write(bs);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeEncrypt(byte[] bs) throws FileNotFoundException {
		write(new FileOutputStream(encrypted), bs);
	}

	private void writeDecrypt(byte[] bs) throws FileNotFoundException {
		write(new FileOutputStream(dencrypted), bs);

	}

	public void initKey() throws Exception {
		File key = new File(publicKey);
		if (!key.exists()) {
			KeyPair keys = KeyPairGenerator.getInstance("RSA")
					.generateKeyPair();
			writePrivateKey(keys.getPrivate());
			writePublicKey(keys.getPublic());
		}
	}

	public void encrypt(Cipher cipher) throws Exception {
		byte[] source = readsource();
		byte[] encodeBytes = encryptRSAByteChunk(source, 117, cipher);
		writeEncrypt(encodeBytes);
	}

	public void decrypt(Cipher cipher) throws Exception {
		byte[] source = readEncrpt();
		byte[] decodedBytes = decryptRSAByteChunk(source, 117, cipher);
		String result = new String(decodedBytes);
		System.out.println(result);
		writeDecrypt(decodedBytes);
	}

	byte[] process(byte[] bs, int bucketSize) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] processed = new byte[bs.length];
		byte[] temp = new byte[bucketSize];
		int i = bs.length / bucketSize;
		// int fromEncrypt=-1;
		int toEncrypt = -1;
		// int fromProcessed=-1;
		int toProcessed = -1;
		// int processedUnencryptedFrom=-1;
		int processedUnencryptedto = -1;
		int lastbucketFrom = (bs.length / bucketSize) * bucketSize;
		int lastbucketTo = bs.length;
		for (int j = 0; j < i; j++) {
			int from = j * bucketSize;
			int to = (j + 1) * bucketSize;
			temp = Arrays.copyOfRange(bs, from, to);
			temp = cipher.update(temp);
			toProcessed = to - 1;
			if (!(temp.length == 0)) {
				for (int k = toEncrypt + 1, l = 0; l < temp.length; k++, l++) {
					processed[k] = temp[l];
				}
				toEncrypt = toEncrypt + temp.length;
			} else {

			}
		}
		temp = new byte[lastbucketTo - lastbucketFrom];
		temp = Arrays.copyOfRange(bs, lastbucketFrom, lastbucketTo);
		temp = cipher.doFinal(temp);
		for (int k = lastbucketFrom, l = 0; k < lastbucketTo; k++, l++) {
			processed[k] = temp[l];
		}
		return processed;
	}

	private byte[] encryptRSAByteChunk(byte[] source, int chunkSize,
			Cipher cipher) {
		// int processedlength=(int)
		// (((double)source.length/(double)chunkSize)*128);
		int processedlength = (int) (Math.ceil((double) source.length
				/ (double) chunkSize)) * 128;
		byte[] processed = new byte[processedlength];
		try {
			PublicKey key = readPublicKey();
			int unProcessedBytes = source.length;
			byte[] temp = null;
			int unprocessedFrom = 0;
			int i = 0;
			for (i = 0; unProcessedBytes > chunkSize; i++) {
				int from = i * chunkSize;
				int to = (i + 1) * chunkSize;
				unprocessedFrom = to;
				cipher.init(Cipher.ENCRYPT_MODE, key);
				temp = Arrays.copyOfRange(source, from, to);
				byte[] encrypted = cipher.doFinal(temp);
				for (int j = i * 128, k = 0; k < encrypted.length; j++, k++) {
					processed[j] = encrypted[k];
				}
				unProcessedBytes = unProcessedBytes - chunkSize;
			}
			if (unProcessedBytes != 0) {
				cipher.init(Cipher.ENCRYPT_MODE, key);
				temp = Arrays.copyOfRange(source, unprocessedFrom,
						source.length);
				byte[] encrypted = cipher.doFinal(temp);
				for (int j = i * 128, k = 0; k < encrypted.length; j++, k++) {
					processed[j] = encrypted[k];
				}
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return processed;
	}

	private byte[] decryptRSAByteChunk(byte[] source, int chunkSize,
			Cipher cipher) {
		byte[] processed = new byte[source.length];
		int totalLength = 0;
		try {
			PrivateKey key = readPrivateKey();
			int unProcessedBytes = source.length;
			byte[] temp = new byte[128];
			int unprocessedFrom = 0;
			int i = 0;
			for (i = 0; unProcessedBytes > 128; i++) {
				int from = i * 128;
				int to = (i + 1) * 128;
				unprocessedFrom = to;
				cipher.init(Cipher.DECRYPT_MODE, key);
				temp = Arrays.copyOfRange(source, from, to);
				byte[] decrypted = cipher.doFinal(temp);
				totalLength += decrypted.length;
				for (int j = i * chunkSize, k = 0; k < decrypted.length; j++, k++) {
					processed[j] = decrypted[k];
				}
				unProcessedBytes = unProcessedBytes - 128;
			}
			if (unProcessedBytes != 0) {
				cipher.init(Cipher.DECRYPT_MODE, key);
				temp = Arrays.copyOfRange(source, unprocessedFrom,
						source.length);
				byte[] decrypted = cipher.doFinal(temp);
				totalLength += decrypted.length;
				for (int j = i * chunkSize, k = 0; k < decrypted.length; j++, k++) {
					processed[j] = decrypted[k];
				}
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return Arrays.copyOfRange(processed, 0, totalLength);
	}
}
