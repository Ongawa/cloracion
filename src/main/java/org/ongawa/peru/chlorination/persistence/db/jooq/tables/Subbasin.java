/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.Keys;
import org.ongawa.peru.chlorination.persistence.db.jooq.Public;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.SubbasinRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Subbasin extends TableImpl<SubbasinRecord> {

	private static final long serialVersionUID = -2069789948;

	/**
	 * The reference instance of <code>PUBLIC.SUBBASIN</code>
	 */
	public static final Subbasin SUBBASIN = new Subbasin();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<SubbasinRecord> getRecordType() {
		return SubbasinRecord.class;
	}

	/**
	 * The column <code>PUBLIC.SUBBASIN.IDSUBBASIN</code>.
	 */
	public final TableField<SubbasinRecord, Integer> IDSUBBASIN = createField("IDSUBBASIN", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>PUBLIC.SUBBASIN.NAME</code>.
	 */
	public final TableField<SubbasinRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(100).nullable(false), this, "");

	/**
	 * Create a <code>PUBLIC.SUBBASIN</code> table reference
	 */
	public Subbasin() {
		this("SUBBASIN", null);
	}

	/**
	 * Create an aliased <code>PUBLIC.SUBBASIN</code> table reference
	 */
	public Subbasin(String alias) {
		this(alias, SUBBASIN);
	}

	private Subbasin(String alias, Table<SubbasinRecord> aliased) {
		this(alias, aliased, null);
	}

	private Subbasin(String alias, Table<SubbasinRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<SubbasinRecord, Integer> getIdentity() {
		return Keys.IDENTITY_SUBBASIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<SubbasinRecord> getPrimaryKey() {
		return Keys.CONSTRAINT_B;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<SubbasinRecord>> getKeys() {
		return Arrays.<UniqueKey<SubbasinRecord>>asList(Keys.CONSTRAINT_B);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Subbasin as(String alias) {
		return new Subbasin(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Subbasin rename(String name) {
		return new Subbasin(name, null);
	}
}
