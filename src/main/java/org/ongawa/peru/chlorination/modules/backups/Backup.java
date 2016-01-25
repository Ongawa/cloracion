package org.ongawa.peru.chlorination.modules.backups;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Kiko
 *
 */
public abstract class Backup {
	
	protected static Logger log = LoggerFactory.getLogger(Backup.class);
	
	IDataSource ds;
	Properties properties;
	
	public Backup() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
		this.properties = ApplicationProperties.getInstance().getProperties();
	}
	
	public abstract void createBackup(Path path);
	
	public abstract String getFileExtension();
	
	public abstract String getFormat();
}
