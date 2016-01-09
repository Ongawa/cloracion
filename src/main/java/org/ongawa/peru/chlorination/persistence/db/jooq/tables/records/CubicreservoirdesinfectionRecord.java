/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record13;
import org.jooq.Record5;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoirdesinfection;


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
public class CubicreservoirdesinfectionRecord extends UpdatableRecordImpl<CubicreservoirdesinfectionRecord> implements Record13<Timestamp, Integer, Integer, Integer, Integer, Integer, Double, Double, Double, Double, Double, Double, Double> {

	private static final long serialVersionUID = -667659190;

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DATE</code>.
	 */
	public void setDate(Timestamp value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DATE</code>.
	 */
	public Timestamp getDate() {
		return (Timestamp) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR</code>.
	 */
	public void setCubicreservoirIdcubicreservoir(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR</code>.
	 */
	public Integer getCubicreservoirIdcubicreservoir() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public void setCubicreservoirWatersystemIdwatersystem(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public Integer getCubicreservoirWatersystemIdwatersystem() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public void setCubicreservoirWatersystemCommunityIdcommunity(Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public Integer getCubicreservoirWatersystemCommunityIdcommunity() {
		return (Integer) getValue(3);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public void setCubicreservoirWatersystemCommunitySubbasinIdsubbasin(Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public Integer getCubicreservoirWatersystemCommunitySubbasinIdsubbasin() {
		return (Integer) getValue(4);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.COUNT</code>.
	 */
	public void setCount(Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.COUNT</code>.
	 */
	public Integer getCount() {
		return (Integer) getValue(5);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.WATERHEIGHT</code>.
	 */
	public void setWaterheight(Double value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.WATERHEIGHT</code>.
	 */
	public Double getWaterheight() {
		return (Double) getValue(6);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.VOLUME</code>.
	 */
	public void setVolume(Double value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.VOLUME</code>.
	 */
	public Double getVolume() {
		return (Double) getValue(7);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CHLORINECONCENTRATION</code>.
	 */
	public void setChlorineconcentration(Double value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.CHLORINECONCENTRATION</code>.
	 */
	public Double getChlorineconcentration() {
		return (Double) getValue(8);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMANDACTIVECHLORINE</code>.
	 */
	public void setDemandactivechlorine(Double value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMANDACTIVECHLORINE</code>.
	 */
	public Double getDemandactivechlorine() {
		return (Double) getValue(9);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMAND70CHLORINE</code>.
	 */
	public void setDemand70chlorine(Double value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMAND70CHLORINE</code>.
	 */
	public Double getDemand70chlorine() {
		return (Double) getValue(10);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS</code>.
	 */
	public void setDemandspoons(Double value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS</code>.
	 */
	public Double getDemandspoons() {
		return (Double) getValue(11);
	}

	/**
	 * Setter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.RETENTIONTIME</code>.
	 */
	public void setRetentiontime(Double value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>PUBLIC.CUBICRESERVOIRDESINFECTION.RETENTIONTIME</code>.
	 */
	public Double getRetentiontime() {
		return (Double) getValue(12);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record5<Timestamp, Integer, Integer, Integer, Integer> key() {
		return (Record5) super.key();
	}

	// -------------------------------------------------------------------------
	// Record13 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row13<Timestamp, Integer, Integer, Integer, Integer, Integer, Double, Double, Double, Double, Double, Double, Double> fieldsRow() {
		return (Row13) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row13<Timestamp, Integer, Integer, Integer, Integer, Integer, Double, Double, Double, Double, Double, Double, Double> valuesRow() {
		return (Row13) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field1() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_IDWATERSYSTEM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_IDCOMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field6() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.COUNT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field7() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERHEIGHT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field8() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.VOLUME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field9() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CHLORINECONCENTRATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field10() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMANDACTIVECHLORINE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field11() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMAND70CHLORINE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field12() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DEMANDSPOONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field13() {
		return Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.RETENTIONTIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value1() {
		return getDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getCubicreservoirIdcubicreservoir();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getCubicreservoirWatersystemIdwatersystem();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value4() {
		return getCubicreservoirWatersystemCommunityIdcommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value5() {
		return getCubicreservoirWatersystemCommunitySubbasinIdsubbasin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value6() {
		return getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value7() {
		return getWaterheight();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value8() {
		return getVolume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value9() {
		return getChlorineconcentration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value10() {
		return getDemandactivechlorine();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value11() {
		return getDemand70chlorine();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value12() {
		return getDemandspoons();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value13() {
		return getRetentiontime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value1(Timestamp value) {
		setDate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value2(Integer value) {
		setCubicreservoirIdcubicreservoir(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value3(Integer value) {
		setCubicreservoirWatersystemIdwatersystem(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value4(Integer value) {
		setCubicreservoirWatersystemCommunityIdcommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value5(Integer value) {
		setCubicreservoirWatersystemCommunitySubbasinIdsubbasin(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value6(Integer value) {
		setCount(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value7(Double value) {
		setWaterheight(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value8(Double value) {
		setVolume(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value9(Double value) {
		setChlorineconcentration(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value10(Double value) {
		setDemandactivechlorine(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value11(Double value) {
		setDemand70chlorine(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value12(Double value) {
		setDemandspoons(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord value13(Double value) {
		setRetentiontime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CubicreservoirdesinfectionRecord values(Timestamp value1, Integer value2, Integer value3, Integer value4, Integer value5, Integer value6, Double value7, Double value8, Double value9, Double value10, Double value11, Double value12, Double value13) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		value8(value8);
		value9(value9);
		value10(value10);
		value11(value11);
		value12(value12);
		value13(value13);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached CubicreservoirdesinfectionRecord
	 */
	public CubicreservoirdesinfectionRecord() {
		super(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION);
	}

	/**
	 * Create a detached, initialised CubicreservoirdesinfectionRecord
	 */
	public CubicreservoirdesinfectionRecord(Timestamp date, Integer cubicreservoirIdcubicreservoir, Integer cubicreservoirWatersystemIdwatersystem, Integer cubicreservoirWatersystemCommunityIdcommunity, Integer cubicreservoirWatersystemCommunitySubbasinIdsubbasin, Integer count, Double waterheight, Double volume, Double chlorineconcentration, Double demandactivechlorine, Double demand70chlorine, Double demandspoons, Double retentiontime) {
		super(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION);

		setValue(0, date);
		setValue(1, cubicreservoirIdcubicreservoir);
		setValue(2, cubicreservoirWatersystemIdwatersystem);
		setValue(3, cubicreservoirWatersystemCommunityIdcommunity);
		setValue(4, cubicreservoirWatersystemCommunitySubbasinIdsubbasin);
		setValue(5, count);
		setValue(6, waterheight);
		setValue(7, volume);
		setValue(8, chlorineconcentration);
		setValue(9, demandactivechlorine);
		setValue(10, demand70chlorine);
		setValue(11, demandspoons);
		setValue(12, retentiontime);
	}
}