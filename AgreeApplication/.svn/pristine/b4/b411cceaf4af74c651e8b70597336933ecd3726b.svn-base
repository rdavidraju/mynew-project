package com.nspl.app.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class PropertiesUtilService {

	/**
	 * Author Kiran
	 * @param propFileName
	 * @return to get the properties (File Sync Methods based on Id)
	 */
	public  Properties getPropertiesFromClasspath(String propFileName)
	{
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream == null)
		{
			try {
				throw new FileNotFoundException("property file '" + propFileName
						+ "' not found in the classpath");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
}
