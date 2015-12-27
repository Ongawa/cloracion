package org.ongawa.peru.chlorination.persistence.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionsPool {
	/**
	 * @author Kiko
	 */
	
	//Not a real connections pool. Refactor this!
	
	
	private static ConnectionsPool pool;
	private static Logger log;
	
	static{
		pool = null;
		log = LoggerFactory.getLogger(ConnectionsPool.class);
	}
	
	public static ConnectionsPool getInstance(){
		if(pool == null){
			pool = new ConnectionsPool();
		}
		
		return pool;
	}
	
	private Properties properties;
	
	private ConnectionsPool(){
		try {
			this.properties = ApplicationProperties.getInstance().getProperties();
		} catch (IOException e) {
			log.warn(e.toString());
		}
	}
	
	public Connection getConnection() throws SQLException{
		String url = properties.getProperty(KEYS.DATABASE_URL);
		String username = properties.getProperty(KEYS.DATABASE_USERNAME);
		String password = properties.containsKey(KEYS.DATABASE_PASSWORD)?properties.getProperty(KEYS.DATABASE_PASSWORD):"";
		
		Connection connection = DriverManager.getConnection(url, username, password);
		log.debug("Connection created: "+connection+" and valid: "+connection.isValid(0));
		
		return connection;
	}
}
