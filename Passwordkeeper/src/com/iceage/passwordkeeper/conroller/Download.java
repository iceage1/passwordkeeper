package com.iceage.passwordkeeper.conroller;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.simple.JSONObject;

import com.iceage.passwordkeeper.api.Conf;
import com.iceage.passwordkeeper.api.IDtoNameConverter;
import com.iceage.passwordkeeper.api.RSAUtil;
import com.iceage.passwordkeeper.io.DirectoryStructure;


@Path("/getResource")
public class Download {

	private static final String Repo_loc = "D:\\Users\\schauhan\\git\\passwordkeeper\\Passwordkeeper\\WebContent\\resource\\";
	
	@GET
	@Path("/json/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJsonFile(@PathParam("id") String id ) {
		String fileName= IDtoNameConverter.getFileName(id);
	    File file = new File(Repo_loc+ "\\"+ fileName);
	    File a= new File("ab");
	    RSAUtil rsaUtil= new RSAUtil();
	    byte[] decodedBytes= null;
	    try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] source = rsaUtil.readEncrpt(file);
			PrivateKey key= RSAUtil.readPrivateKey(new File(Repo_loc+ "\\"+ "pri.key"));
			decodedBytes = rsaUtil.decryptRSAByteChunk(source, 117, cipher, key);
			//rsaUtil.writeDecrypt(decodedBytes, new String(decodedBytes));
			DirectoryStructure.getStructure("");
			System.out.println("asfgertertertetedrtrrrrrrrrrrrr");
			DirectoryStructure.getStructure("Bank");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	    return Response.status(200).entity(new String(decodedBytes)).build();
	    //ResponseBuilder response = Response.ok((Object) a);
	    //response.header("Content-Disposition","attachment; filename="+"userContent");
	    //return response.build();

	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getFile(@PathParam("id") String id ) {
		String fileName= IDtoNameConverter.getFileName(id);
		//TODO better logic needed
		fileName= id.replace("-", "/");
	    
		File file = new File(Conf._repoLoc+fileName);
	    File a= new File("ab");
	    RSAUtil rsaUtil= new RSAUtil();
	    try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] source = rsaUtil.readEncrpt(file);
			PrivateKey key= RSAUtil.readPrivateKey(new File(Repo_loc+ "\\"+ "pri.key"));
			byte[] decodedBytes = rsaUtil.decryptRSAByteChunk(source, 117, cipher, key);
			rsaUtil.writeDecrypt(decodedBytes, a);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	    ResponseBuilder response = Response.ok((Object) a);
	    response.header("Content-Disposition","attachment; filename="+file.getName());
	    return response.build();

	}
	
	
	@GET
	@Path("/path/{dir}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDir(@PathParam("dir") String dir ) {
		dir=dir.replace("-", "/");
		JSONObject object =DirectoryStructure.getStructure(dir);
	    return Response.status(200).entity(object.toString()).build();
	}
} 
