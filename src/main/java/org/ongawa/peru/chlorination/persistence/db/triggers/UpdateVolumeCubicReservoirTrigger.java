package org.ongawa.peru.chlorination.persistence.db.triggers;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.api.Trigger;

public class UpdateVolumeCubicReservoirTrigger implements Trigger{

	@Override
	public void close() throws SQLException {
		//ignore
	}

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
		if(((int)oldRow[8])<=0)
			newRow[8] = ((int)oldRow[5])*((int)oldRow[6])*((int)oldRow[7]);
		
		if(((int)oldRow[10])<=0)
			newRow[10] = ((int)oldRow[5])*((int)oldRow[6])*((int)oldRow[7])*((int)oldRow[9]);
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3, boolean arg4, int arg5) throws SQLException {
		//ignore
	}

	@Override
	public void remove() throws SQLException {
		//ignore
	}

}
