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

import com.iceage.passwordkeeper.api.IDtoNameConverter;
import com.iceage.passwordkeeper.api.RSAUtil;


@Path("/getResource")
public class Download {

	private static final String FILE_PATH = "D:\\Users\\schauhan\\git\\passwordkeeper\\Passwordkeeper\\WebContent\\resource\\";
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getFile(@PathParam("id") String id ) {
		String fileName= IDtoNameConverter.getFileName(id);
	    File file = new File(FILE_PATH+ "\\"+ fileName);
	    File a= new File("ab");
	    RSAUtil rsaUtil= new RSAUtil();
	    try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] source = rsaUtil.readEncrpt(file);
			PrivateKey key= RSAUtil.readPrivateKey(new File(FILE_PATH+ "\\"+ "pri.key"));
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
	    response.header("Content-Disposition","attachment; filename="+fileName);
	    return response.build();

	}
} 
