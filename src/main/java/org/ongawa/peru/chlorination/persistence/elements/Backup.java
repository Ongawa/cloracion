package org.ongawa.peru.chlorination.persistence.elements;

import java.sql.Date;

import org.apache.commons.lang.NullArgumentException;

/**
 * 
 * @author Kiko
 *
 */
public class Backup {

	private int id;
	private String filePath;
	private String fileName;
	private String service;
	private Date lastAttempt;
	
	public Backup(int id, String filePath, String fileName, String service) {
		super();
		if(filePath == null)
			throw new NullArgumentException("filePath");
		if(fileName == null)
			throw new NullArgumentException("fileName");
		if(service == null)
			throw new NullArgumentException("service");
		
		this.id = id;
		this.filePath = filePath;
		this.fileName = fileName;
		this.service = service;
	}
	
	public Backup(String filePath, String fileName, String service) {
		super();
		if(filePath == null)
			throw new NullArgumentException("filePath");
		if(fileName == null)
			throw new NullArgumentException("fileName");
		if(service == null)
			throw new NullArgumentException("service");
		
		this.id = -1;
		this.filePath = filePath;
		this.fileName = fileName;
		this.service = service;
	}

	public Date getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(Date lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

	public int getId() {
		return id;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getService() {
		return service;
	}
}
