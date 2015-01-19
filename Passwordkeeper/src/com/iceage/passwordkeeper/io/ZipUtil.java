package com.iceage.passwordkeeper.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	static final int BUFFER = 2048;

	public static void main(String[] args) {
		try {
			File zipFile = new File("d:\\ptc\\a\\test.zip");
			String[] list = new File("d:\\ptc\\").list();
			File[] files = new File[list.length];
			for (int i = 0; i < list.length; i++) {
				files[i] = new File("d:\\ptc\\" + list[i]);
			}
			compress(zipFile, files);
			expand(zipFile, new File("d:\\ptc\\b\\"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static void compress(File outputFile, File[] inputFiles)
			throws IOException {
		try {
			BufferedInputStream origin = null;
			outputFile.mkdirs();outputFile.delete();
			FileOutputStream dest = new FileOutputStream(outputFile);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			byte data[] = new byte[BUFFER];
			for (int i = 0; i < inputFiles.length; i++) {
				File file = inputFiles[i];
				if (file.isFile()) {
					System.out.println("Adding: "+ inputFiles[i].getAbsolutePath());
					FileInputStream fi = new FileInputStream(inputFiles[i]);
					origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(file.getName());
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void expand(File inputFile, File outputFolder) throws IOException {
		try {
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(inputFile);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				if (!outputFolder.exists()){
					outputFolder.mkdir();
				}
				FileOutputStream fos = new FileOutputStream(outputFolder.getAbsolutePath()+"\\"+ entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
