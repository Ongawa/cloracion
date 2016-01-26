package org.ongawa.peru.chlorination.modules.backups;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.NullArgumentException;
import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;

/**
 * 
 * @author Kiko
 *
 */
public abstract class BackupUploader implements Runnable {

	protected Properties properties;
	private String accessToken;
	protected IDataSource ds;
	
	public BackupUploader(String accessToken) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		if(accessToken == null)
			throw new NullArgumentException("accessToken");
		this.accessToken = accessToken;
		this.properties = ApplicationProperties.getInstance().getProperties();
		this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
	}
	
	protected String getAccessToken(){
		return this.accessToken;
	}
	
	public abstract String getServiceName();
	
	public abstract void proceed();
}
