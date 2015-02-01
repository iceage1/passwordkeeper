package com.iceage.passwordkeeper.api;

public class IDtoNameConverter {

	
	public static String getFileName(String id){
		StringBuilder a= new StringBuilder();
		String fileName = null;
		// Test implementation should be fetched from Db
		switch(id){
		case "hgjvvchcjvjcjcccjlcljcc":
			fileName= "abc";
		case "asdsafsdddsggdsgdgdgdfg":
			fileName= "Bank/folder.contents";
		}
		return fileName;
	}
	
	public static String getID(String fileName){
		StringBuilder a= new StringBuilder();
		return fileName;
	}
}
