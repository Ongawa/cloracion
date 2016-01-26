package org.ongawa.peru.chlorination.modules.backups;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.ongawa.peru.chlorination.persistence.elements.Backup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

public class DropboxUploader extends BackupUploader {

	public static final String SERVICENAME = "Dropbox";
	private static Logger log = LoggerFactory.getLogger(DropboxUploader.class);
	
	public DropboxUploader(String accessToken) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		super(accessToken);
	}

	@Override
	public String getServiceName() {
		return SERVICENAME;
	}

	@Override
	public void proceed() {
		List<Backup> backups = this.ds.getBackups(this.getServiceName());
		DbxRequestConfig config = new DbxRequestConfig("ChlorinationBackup", Locale.getDefault().toString());
		DbxClient client = new DbxClient(config, this.getAccessToken());
		for(Backup backup : backups){
			Path path = FileSystems.getDefault().getPath(backup.getFilePath(), backup.getFileName());
			try {
				FileInputStream inputStream = new FileInputStream(path.toFile());
			    DbxEntry.File uploadedFile = client.uploadFile("/"+backup.getFileName(), DbxWriteMode.add(), path.toFile().length(), inputStream);
			    log.info("Uploaded backup: " + uploadedFile.toString());
			    this.ds.removeBackup(backup);
			    inputStream.close();
			}
			catch(UnknownHostException e){
				log.warn("Backup "+backup.getFilePath()+File.separator+backup.getFileName()+" not uploaded: Is there an Internet connection available?");
				log.warn(e.toString());
				backup.setLastAttempt(new Date(Calendar.getInstance().getTime().getTime()));
				this.ds.editBackup(backup);
			}
			catch(DbxException | IOException e){
				log.warn(e.toString());
				backup.setLastAttempt(new Date(Calendar.getInstance().getTime().getTime()));
				this.ds.editBackup(backup);
			}
		}
	}

	@Override
	public void run() {
		this.proceed();
	}
}
