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
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.ChlorinecalculationRecord;


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
public class Chlorinecalculation extends TableImpl<ChlorinecalculationRecord> {

	private static final long serialVersionUID = 1230679752;

	/**
	 * The reference instance of <code>PUBLIC.CHLORINECALCULATION</code>
	 */
	public static final Chlorinecalculation CHLORINECALCULATION = new Chlorinecalculation();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ChlorinecalculationRecord> getRecordType() {
		return ChlorinecalculationRecord.class;
	}

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.DATE</code>.
	 */
	public final TableField<ChlorinecalculationRecord, Timestamp> DATE = createField("DATE", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public final TableField<ChlorinecalculationRecord, Integer> WATERSYSTEM_COMMUNITY_IDCOMMUNITY = createField("WATERSYSTEM_COMMUNITY_IDCOMMUNITY", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public final TableField<ChlorinecalculationRecord, Integer> WATERSYSTEM_IDWATERSYSTEM = createField("WATERSYSTEM_IDWATERSYSTEM", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public final TableField<ChlorinecalculationRecord, Integer> WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN = createField("WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.POPULATION</code>.
	 */
	public final TableField<ChlorinecalculationRecord, String> POPULATION = createField("POPULATION", org.jooq.impl.SQLDataType.VARCHAR.length(45), this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.TANKVOLUME</code>.
	 */
	public final TableField<ChlorinecalculationRecord, Short> TANKVOLUME = createField("TANKVOLUME", org.jooq.impl.SQLDataType.SMALLINT, this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.TANKUSEFULVOLUME</code>.
	 */
	public final TableField<ChlorinecalculationRecord, Short> TANKUSEFULVOLUME = createField("TANKUSEFULVOLUME", org.jooq.impl.SQLDataType.SMALLINT, this, "");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.ENDOWMENT</code>. m^3/persona x dia
	 */
	public final TableField<ChlorinecalculationRecord, Short> ENDOWMENT = createField("ENDOWMENT", org.jooq.impl.SQLDataType.SMALLINT, this, "m^3/persona x dia");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.CHLORINEPURENESS</code>. Expresado en tanto por 1
	 */
	public final TableField<ChlorinecalculationRecord, Byte> CHLORINEPURENESS = createField("CHLORINEPURENESS", org.jooq.impl.SQLDataType.TINYINT, this, "Expresado en tanto por 1");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.INPUTFLOW</code>. Medido en litros/segundo
	 */
	public final TableField<ChlorinecalculationRecord, Byte> INPUTFLOW = createField("INPUTFLOW", org.jooq.impl.SQLDataType.TINYINT, this, "Medido en litros/segundo");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.RELOADTIME</code>. Medido en días
	 */
	public final TableField<ChlorinecalculationRecord, Integer> RELOADTIME = createField("RELOADTIME", org.jooq.impl.SQLDataType.INTEGER, this, "Medido en días");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.DEMANDCLR</code>. Medido en mg/litro
	 */
	public final TableField<ChlorinecalculationRecord, Byte> DEMANDCLR = createField("DEMANDCLR", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "Medido en mg/litro");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.DEMANDACTIVECHLORINE</code>. Medido en mg/litro
	 */
	public final TableField<ChlorinecalculationRecord, Byte> DEMANDACTIVECHLORINE = createField("DEMANDACTIVECHLORINE", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "Medido en mg/litro");

	/**
	 * The column <code>PUBLIC.CHLORINECALCULATION.DEMANDCOMMONPRODUCT</code>. Medido en mg/litro
	 */
	public final TableField<ChlorinecalculationRecord, Byte> DEMANDCOMMONPRODUCT = createField("DEMANDCOMMONPRODUCT", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "Medido en mg/litro");

	/**
	 * Create a <code>PUBLIC.CHLORINECALCULATION</code> table reference
	 */
	public Chlorinecalculation() {
		this("CHLORINECALCULATION", null);
	}

	/**
	 * Create an aliased <code>PUBLIC.CHLORINECALCULATION</code> table reference
	 */
	public Chlorinecalculation(String alias) {
		this(alias, CHLORINECALCULATION);
	}

	private Chlorinecalculation(String alias, Table<ChlorinecalculationRecord> aliased) {
		this(alias, aliased, null);
	}

	private Chlorinecalculation(String alias, Table<ChlorinecalculationRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ChlorinecalculationRecord> getPrimaryKey() {
		return Keys.CONSTRAINT_E;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ChlorinecalculationRecord>> getKeys() {
		return Arrays.<UniqueKey<ChlorinecalculationRecord>>asList(Keys.CONSTRAINT_E);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ChlorinecalculationRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ChlorinecalculationRecord, ?>>asList(Keys.FK_CHLORINECALCULATION_WATERSYSTEM1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Chlorinecalculation as(String alias) {
		return new Chlorinecalculation(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Chlorinecalculation rename(String name) {
		return new Chlorinecalculation(name, null);
	}
}