package org.ongawa.peru.chlorination.persistence;

import java.io.IOException;
import java.util.Properties;

import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kiko
 */
public class DataSourceFactory {

	private static DataSourceFactory dsf;
	private static Logger log;
	
	static{
		log = LoggerFactory.getLogger(DataSourceFactory.class);
		dsf = null;
	}
	
	public static DataSourceFactory getInstance(){
		if(dsf == null)
			dsf = new DataSourceFactory();
		
		return dsf;
	}
	
	private Properties properties;
	
	private DataSourceFactory(){
		this.properties = null;
		try {
			this.properties = ApplicationProperties.getInstance().getProperties();
		} catch (IOException e) {
			log.warn("Error getting the properties: "+e.toString());
		}
	}
	
	public IDataSource getDefaultDataSource() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String classpath = this.properties.getProperty(KEYS.DEFAULT_DATASOURCE);
		try{
			return this.getDataSource(classpath);
		}
		catch(ClassCastException e){
			throw new ClassCastException("The datasource classpath set on the properties file is not valid.\n"+e.getMessage());
		}
	}
	
	public IDataSource getDataSource(Class<?> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return this.getDataSource(clazz.getName());
	}
	
	public IDataSource getDataSource(String classpath) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> dataSource = Class.forName(classpath);
		if(IDataSource.class.isAssignableFrom(dataSource)){
			return (IDataSource)dataSource.newInstance();
		}
		else
			throw new ClassCastException("The datasource \""+classpath+"\" is not valid");
	}
}
