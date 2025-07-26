package com.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.constants.Env;

public class PropertiesUtil {

	public static String getproperty(Env env, String propertyName) {
		File file = new File(System.getProperty("user.dir") + "\\config\\" + env + ".properties");
		FileReader reader = null;
		Properties properties = new Properties();
		try {
			reader = new FileReader(file);
			properties.load(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String value = properties.getProperty(propertyName.toUpperCase());
		return value;
	}

}
