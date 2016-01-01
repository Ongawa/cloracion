package org.ongawa.peru.chlorination;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Kiko
 */
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
		this.readProperties();
	}
	
	private void readProperties() throws IOException{
		this.properties = new Properties();
		String resourceName = KEYS.PROPERTIES_FILENAME;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(resourceName);
		this.properties.load(resourceStream);
	}
	
	public Properties getProperties(){
		return this.properties;
	}
	
	public void storeProperties() throws FileNotFoundException, IOException{
		Path path  = FileSystems.getDefault().getPath(properties.getProperty(KEYS.RESOURCES_PATH), KEYS.PROPERTIES_FILENAME);
		this.properties.store(new FileOutputStream(path.toFile()), "Generated properties file for Chlorination app");
		this.readProperties();
	}
}
