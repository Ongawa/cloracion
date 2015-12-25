package org.ongawa.peru.chlorination;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
	private static ApplicationProperties appProperties;
	static{
		appProperties = null;
	}
	
	public static ApplicationProperties getInstance() throws IOException{
		if(appProperties == null)
			appProperties = new ApplicationProperties();
		
		return appProperties;
	}
	
	private Properties properties;
	
	private ApplicationProperties() throws IOException{
		this.properties = new Properties();
		String resourceName = KEYS.PROPERTIES_FILENAME;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(resourceName);
		this.properties.load(resourceStream);
	}
	
	public Properties getProperties(){
		return this.properties;
	}
}