package org.ongawa.peru.chlorination.persistence.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSource {
	/**
	 * @author Kiko
	 */

	private static DataSource datasource;
	private static Logger log;
	static{
		datasource = null;
		log = LoggerFactory.getLogger(DataSource.class);
	}
	
	public static DataSource getInstance(){
		if(datasource == null)
			datasource = new DataSource();
		
		return datasource;
	}
	
	private Connection connection;
	
	private DataSource(){
	}
	
	private SubBasin readSubBasin(Record record){
		SubBasin subBasin = new SubBasin(record.getValue(Subbasin.SUBBASIN.IDSUBBASIN), record.getValue(Subbasin.SUBBASIN.NAME));
		return subBasin;
	}
	
	public List<SubBasin> getSubBasins(){
		List<SubBasin> subBasins = null;
		
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			subBasins = new ArrayList<SubBasin>();
			
			DSLContext create = DSL.using(this.connection, SQLDialect.H2);
			Result<Record> result = create.select().from(Subbasin.SUBBASIN).fetch();
			SubBasin subBasin;
			
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				subBasins.add(subBasin);
			}
			this.connection.close();
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return subBasins;
	}
	
	public SubBasin getSubBasin(int idSubBasin){
		SubBasin subBasin = null;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = DSL.using(this.connection, SQLDialect.H2);
			Result<Record> result = create.select().from(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.IDSUBBASIN.eq(idSubBasin)).limit(1).fetch();
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				break;
			}
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return subBasin;
	}
	
	public SubBasin getSubBasin(String name){
		SubBasin subBasin = null;
		try {
			this.connection = ConnectionsPool.getInstance().getConnection();
			DSLContext create = DSL.using(this.connection, SQLDialect.H2);
			Result<Record> result = create.select().from(Subbasin.SUBBASIN).where(Subbasin.SUBBASIN.NAME.eq(name)).limit(1).fetch();
			for(Record record : result){
				subBasin = this.readSubBasin(record);
				break;
			}
		} catch (SQLException e) {
			log.warn(e.toString());
		}
		
		return subBasin;
	}
}
