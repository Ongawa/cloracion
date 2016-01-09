/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.Keys;
import org.ongawa.peru.chlorination.persistence.db.jooq.Public;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirdesinfectionRecord;


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
public class Cubicreservoirdesinfection extends TableImpl<CubicreservoirdesinfectionRecord> {

	private static final long serialVersionUID = -1587839828;

	/**
	 * The reference instance of <code>PUBLIC.CUBICRESERVOIRDESINFECTION</code>
	 */
	public static final Cubicreservoirdesinfection CUBICRESERVOIRDESINFECTION = new Cubicreservoirdesinfection();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<CubicreservoirdesinfectionRecord> getRecordType() {
		return CubicreservoirdesinfectionRecord.class;
	}

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DATE</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Timestamp> DATE = createField("DATE", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Integer> CUBICRESERVOIR_IDCUBICRESERVOIR = createField("CUBICRESERVOIR_IDCUBICRESERVOIR", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Integer> CUBICRESERVOIR_WATERSYSTEM_IDWATERSYSTEM = createField("CUBICRESERVOIR_WATERSYSTEM_IDWATERSYSTEM", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Integer> CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_IDCOMMUNITY = createField("CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_IDCOMMUNITY", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Integer> CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN = createField("CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.COUNT</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Integer> COUNT = createField("COUNT", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.WATERHEIGHT</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> WATERHEIGHT = createField("WATERHEIGHT", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.VOLUME</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> VOLUME = createField("VOLUME", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CHLORINECONCENTRATION</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> CHLORINECONCENTRATION = createField("CHLORINECONCENTRATION", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMANDACTIVECHLORINE</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> DEMANDACTIVECHLORINE = createField("DEMANDACTIVECHLORINE", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMAND70CHLORINE</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> DEMAND70CHLORINE = createField("DEMAND70CHLORINE", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> DEMANDSPOONS = createField("DEMANDSPOONS", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>PUBLIC.CUBICRESERVOIRDESINFECTION.RETENTIONTIME</code>.
	 */
	public final TableField<CubicreservoirdesinfectionRecord, Double> RETENTIONTIME = createField("RETENTIONTIME", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * Create a <code>PUBLIC.CUBICRESERVOIRDESINFECTION</code> table reference
	 */
	public Cubicreservoirdesinfection() {
		this("CUBICRESERVOIRDESINFECTION", null);
	}

	/**
	 * Create an aliased <code>PUBLIC.CUBICRESERVOIRDESINFECTION</code> table reference
	 */
	public Cubicreservoirdesinfection(String alias) {
		this(alias, CUBICRESERVOIRDESINFECTION);
	}

	private Cubicreservoirdesinfection(String alias, Table<CubicreservoirdesinfectionRecord> aliased) {
		this(alias, aliased, null);
	}

	private Cubicreservoirdesinfection(String alias, Table<CubicreservoirdesinfectionRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<CubicreservoirdesinfectionRecord> getPrimaryKey() {
		return Keys.CONSTRAINT_3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<CubicreservoirdesinfectionRecord>> getKeys() {
		return Arrays.<UniqueKey<CubicreservoirdesinfectionRecord>>asList(Keys.CONSTRAINT_3);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<CubicreservoirdesinfectionRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<CubicreservoirdesinfectionRecord, ?>>asList(Keys.FK_CUBICRESERVOIRDESINFECTION_CUBICRESERVOIR1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cubicreservoirdesinfection as(String alias) {
		return new Cubicreservoirdesinfection(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Cubicreservoirdesinfection rename(String name) {
		return new Cubicreservoirdesinfection(name, null);
	}
}
