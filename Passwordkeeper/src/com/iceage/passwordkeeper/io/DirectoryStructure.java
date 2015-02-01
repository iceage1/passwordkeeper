package com.iceage.passwordkeeper.io;

import java.io.File;

import org.json.simple.JSONObject;

import com.iceage.passwordkeeper.api.Conf;

public class DirectoryStructure {
	public static JSONObject getStructure(String directory){
		if (directory.equalsIgnoreCase("abc")){
			directory="";
		}
		
		File currrentDir= new File(Conf.getRepoLoc()+directory);
		File[] files=currrentDir.listFiles();
		JSONObject jsonObject= new JSONObject();
		String curPath= currrentDir.getAbsolutePath().replace("\\","/").concat("/");
		if (curPath.length() > Conf._repoLoc.length()){
			curPath=curPath.substring(Conf._repoLoc.length());
		}else{
			curPath="";
		}
		String parPath= currrentDir.getParent().replace("\\","/").concat("/");
		if (parPath.length() > Conf._repoLoc.length()){
			parPath=parPath.substring(Conf._repoLoc.length());
		}else if(parPath.length() == Conf._repoLoc.length()){
			parPath="";
		}else{
			parPath="Not";
		}
		
		jsonObject.put(".", curPath);
		jsonObject.put("..", parPath);
		
		for (File file : files) {
			String relativePath=file.getAbsolutePath();
			String path=relativePath.substring(relativePath.lastIndexOf("\\")+1);
			JSONObject object= new JSONObject();
			object.put("isFile", file.isFile());
			jsonObject.put(path, object);
		}
		return jsonObject;
		
	}
}
