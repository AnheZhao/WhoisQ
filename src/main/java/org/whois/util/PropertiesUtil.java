package org.whois.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class PropertiesUtil {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Properties readProperties(final String fileName) throws IOException {
		Properties dbProps = null;
		if (fileName == null) {
			return dbProps;
		}
		InputStream inputStream;
		ClassLoader cl = PropertiesUtil.class.getClassLoader();
		if (cl != null) {
			inputStream = cl.getResourceAsStream(fileName);
		} else {
			inputStream = ClassLoader
					.getSystemResourceAsStream(fileName);
		}
		dbProps = new Properties();
		dbProps.load(inputStream);
		for (Object key : dbProps.keySet()) {
			System.out.println(key + "---" + dbProps.get(key));
		}
		inputStream.close();

		return dbProps;
	}
	
	public static String readFileLastModified(final String fileName){
		ClassLoader cl = PropertiesUtil.class.getClassLoader();
		File file = null;
		if (cl != null) {
			file = new File(cl.getResource(fileName).getFile());
		} else {
			file = new File(ClassLoader.getSystemClassLoader().getResource(fileName).getFile());
		}
		Calendar cal = Calendar.getInstance(); 
		long time = file.lastModified(); 
		cal.setTimeInMillis(time); 
		System.out.println("修改时间： " + sdf.format(cal.getTime())); 
		return sdf.format(cal.getTime());
	}

	public static void main(String[] args) {
		try {
			readProperties("whois.properties");
			readFileLastModified("whois.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
