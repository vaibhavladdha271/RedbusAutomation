package com.utility;

import com.google.gson.Gson;
import com.ui.pojo.Config;
import com.ui.pojo.Environments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonUtility {
	
	public static String readJsonFile(String env) {
		Gson gson=new Gson();
		File file=new File(System.getProperty("user.dir")+"\\config\\config.json");
		FileReader reader=null;
		try {
			reader=new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Config config=gson.fromJson(reader, Config.class);
		Environments environment=config.getEnvironments().get(env);
		return environment.getUrl();
	}

}
