package org.ongawa.peru.chlorination.modules.backups;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import org.apache.commons.lang.NullArgumentException;
import org.h2.tools.Script;
import org.ongawa.peru.chlorination.KEYS;

public class SQLBackup extends Backup {

	public SQLBackup() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		super();
	}

	@Override
	public void createBackup(Path path) {
		if(path == null)
			throw new NullArgumentException("path");
		
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(path.toFile());
			Script.execute(properties.getProperty(KEYS.DATABASE_URL), properties.getProperty(KEYS.DATABASE_USERNAME), properties.getProperty(KEYS.DATABASE_PASSWORD), stream);
			log.info("Backup created: "+path.toString());
		} catch (FileNotFoundException | SQLException e) {
			log.warn(e.toString());
		}
		
	}

	@Override
	public String getFileExtension() {
		return "sql";
	}

	@Override
	public String getFormat() {
		return "SQL";
	}
}
