package org.ongawa.peru.chlorination;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

import com.itextpdf.text.log.SysoCounter;

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
		/*String resourceName = KEYS.PROPERTIES_FILENAME;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream resourceStream = loader.getResourceAsStream(resourceName);*/
		InputStream bundledPropertiesFile = ApplicationProperties.class.getResourceAsStream("/" + KEYS.PROPERTIES_FILENAME);
		Properties bundledProperties = new Properties();
		bundledProperties.load(bundledPropertiesFile);
		
		// If the local properties does not exists, create it:
		// Remember to replace the '~' if present
		File localDataFolder = new File(bundledProperties.getProperty(KEYS.LOCAL_DATA_FOLDER).replaceFirst("~",
		                                System.getProperty("user.home")));
		File localPropertiesFile = new File( localDataFolder.getAbsoluteFile(), KEYS.PROPERTIES_FILENAME);
		
		// Absolute path for files:
		if (! localPropertiesFile.exists()) {
		    localDataFolder.getAbsoluteFile().mkdir();
		    localPropertiesFile.createNewFile();
		    bundledProperties.store(new FileOutputStream(localPropertiesFile),"Initial properties for Chlorination app");
		}
		
		
		this.properties.load(new FileInputStream(localPropertiesFile));
	}
	
	public Properties getProperties(){
		return this.properties;
	}
	
	public void storeProperties() throws FileNotFoundException, IOException{
	    // Remember to replace the '~' if present
	    File localDataFolder = new File(this.properties.getProperty(KEYS.LOCAL_DATA_FOLDER).replaceFirst("~",
                System.getProperty("user.home")));
	    File localPropertiesFile = new File(localDataFolder, KEYS.PROPERTIES_FILENAME);
	    this.properties.store(new FileOutputStream(localPropertiesFile), "Generated properties file for Chlorination app");
		this.readProperties();
	}
}
