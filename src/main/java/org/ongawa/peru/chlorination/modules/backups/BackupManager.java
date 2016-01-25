package org.ongawa.peru.chlorination.modules.backups;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.ongawa.peru.chlorination.ApplicationProperties;
import org.ongawa.peru.chlorination.KEYS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackupManager {
	
	private static BackupManager instance;
	private static Logger log;
	
	static{
		instance = null;
		log = LoggerFactory.getLogger(BackupManager.class);
	}
	
	public static BackupManager getInstance() throws IOException{
		if(instance == null)
			instance = new BackupManager();
		
		return instance;
	}
	
	private Properties properties;
	private SimpleDateFormat sdf;
	
	private BackupManager() throws IOException{
		this.properties = ApplicationProperties.getInstance().getProperties();
		this.sdf = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	private void backup() throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException{
		String classpath = this.properties.getProperty(KEYS.DEFAULT_BACKUP);
		Class<?> clazz = Class.forName(classpath);
		Backup backup;
		if(Backup.class.isAssignableFrom(clazz)){
			backup = (Backup)clazz.newInstance();
		}
		else
			throw new ClassCastException("The backup \""+classpath+"\" is not valid");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = Calendar.getInstance().getTime();
		Path path = FileSystems.getDefault().getPath(System.getProperty("user.home")+properties.getProperty(KEYS.APPDATA_PATH)+File.separator+"backups", "Backup_"+backup.getFormat()+"_"+sdf.format(now)+"."+backup.getFileExtension());
		if(!path.toFile().getParentFile().exists())
			path.toFile().getParentFile().mkdirs();
		
		backup.createBackup(path);
		this.properties.setProperty(KEYS.LAST_BACKUP_DATE, sdf.format(now));
		ApplicationProperties.getInstance().storeProperties();
	}
	
	public void proceed() throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException{
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		String lastBackup = this.properties.getProperty(KEYS.LAST_BACKUP_DATE);
		if(lastBackup==null){
			this.backup();
		}
		else{
			try {
				Date last = this.sdf.parse(lastBackup);
				c.setTime(last);
				c.add(Calendar.DATE, Integer.parseInt(this.properties.getProperty(KEYS.BACKUP_DAYS_LAPSE)));
				last = c.getTime();
				if(!last.after(now))
					this.backup();
			} catch (ParseException | NumberFormatException e) {
				log.warn(e.toString());
				this.backup();
			}
		}
	}
}