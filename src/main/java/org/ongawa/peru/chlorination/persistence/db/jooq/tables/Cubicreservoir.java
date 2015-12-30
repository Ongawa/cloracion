/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.Keys;
import org.ongawa.peru.chlorination.persistence.db.jooq.Public;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirRecord;


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
public class Cubicreservoir extends TableImpl<CubicreservoirRecord> {

	private static final long serialVersionUID = -1724452090;

	/**
	 * The reference instance of <code>PUBLIC.CUBICRESERVOIR</code>
	 */
	public static final Cubicreservoir CUBICRESERVOIR = new Cubicreservoir();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<CubicreservoirRecord> getRecordType() {
		return CubicreservoirRecord.class;
	}

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.IDCUBICRESERVOIR</code>.
	 */
	public final TableField<CubicreservoirRecord, Integer> IDCUBICRESERVOIR = createField("IDCUBICRESERVOIR", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public final TableField<CubicreservoirRecord, Integer> WATERSYSTEM_IDWATERSYSTEM = createField("WATERSYSTEM_IDWATERSYSTEM", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public final TableField<CubicreservoirRecord, Integer> WATERSYSTEM_COMMUNITY_IDCOMMUNITY = createField("WATERSYSTEM_COMMUNITY_IDCOMMUNITY", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public final TableField<CubicreservoirRecord, Integer> WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN = createField("WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.NAME</code>.
	 */
	public final TableField<CubicreservoirRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.WIDTH</code>.
	 */
	public final TableField<CubicreservoirRecord, Double> WIDTH = createField("WIDTH", org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.LENGTH</code>.
	 */
	public final TableField<CubicreservoirRecord, Double> LENGTH = createField("LENGTH", org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.HEIGHT</code>.
	 */
	public final TableField<CubicreservoirRecord, Double> HEIGHT = createField("HEIGHT", org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.COUNT</code>.
	 */
	public final TableField<CubicreservoirRecord, Integer> COUNT = createField("COUNT", org.jooq.impl.SQLDataType.INTEGER.defaulted(true), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIR.REQUIREDCONCENTRATION</code>.
	 */
	public final TableField<CubicreservoirRecord, Double> REQUIREDCONCENTRATION = createField("REQUIREDCONCENTRATION", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * Create a <code>PUBLIC.CUBICRESERVOIR</code> table reference
	 */
	public Cubicreservoir() {
		this("CUBICRESERVOIR", null);
	}

	/**
	 * Create an aliased <code>PUBLIC.CUBICRESERVOIR</code> table reference
	 */
	public Cubicreservoir(String alias) {
		this(alias, CUBICRESERVOIR);
	}

	private Cubicreservoir(String alias, Table<CubicreservoirRecord> aliased) {
		this(alias, aliased, null);
	}

	private Cubicreservoir(String alias, Table<CubicreservoirRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<CubicreservoirRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CUBICRESERVOIR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<CubicreservoirRecord> getPrimaryKey() {
		return Keys.CONSTRAINT_CD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<CubicreservoirRecord>> getKeys() {
		return Arrays.<UniqueKey<CubicreservoirRecord>>asList(Keys.CONSTRAINT_CD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<CubicreservoirRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<CubicreservoirRecord, ?>>asList(Keys.FK_CUBICRESERVOIR_WATERSYSTEM1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cubicreservoir as(String alias) {
		return new Cubicreservoir(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Cubicreservoir rename(String name) {
		return new Cubicreservoir(name, null);
	}
}