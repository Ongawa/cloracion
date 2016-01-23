/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record5;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection;


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
public class CatchmentdesinfectionRecord extends UpdatableRecordImpl<CatchmentdesinfectionRecord> implements Record9<Timestamp, Integer, Integer, Integer, Integer, Integer, Double, Double, Double> {

	private static final long serialVersionUID = 1488862077;

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.DESINFECTION_DATE</code>.
	 */
	public void setDesinfectionDate(Timestamp value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.DESINFECTION_DATE</code>.
	 */
	public Timestamp getDesinfectionDate() {
		return (Timestamp) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT</code>.
	 */
	public void setCatchmentIdcatchment(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT</code>.
	 */
	public Integer getCatchmentIdcatchment() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public void setWatersystemIdwatersystem(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public Integer getWatersystemIdwatersystem() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public void setWatersystemCommunityIdcommunity(Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public Integer getWatersystemCommunityIdcommunity() {
		return (Integer) getValue(3);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public void setWatersystemCommunitySubbasinIdsubbasin(Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public Integer getWatersystemCommunitySubbasinIdsubbasin() {
		return (Integer) getValue(4);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.COUNT</code>.
	 */
	public void setCount(Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.COUNT</code>.
	 */
	public Integer getCount() {
		return (Integer) getValue(5);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.CHLORINEQTY</code>.
	 */
	public void setChlorineqty(Double value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.CHLORINEQTY</code>.
	 */
	public Double getChlorineqty() {
		return (Double) getValue(6);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.DEMANDSPOONS</code>.
	 */
	public void setDemandspoons(Double value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.DEMANDSPOONS</code>.
	 */
	public Double getDemandspoons() {
		return (Double) getValue(7);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENTDESINFECTION.RETENTIONTIME</code>.
	 */
	public void setRetentiontime(Double value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENTDESINFECTION.RETENTIONTIME</code>.
	 */
	public Double getRetentiontime() {
		return (Double) getValue(8);
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
	// Record9 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row9<Timestamp, Integer, Integer, Integer, Integer, Integer, Double, Double, Double> fieldsRow() {
		return (Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row9<Timestamp, Integer, Integer, Integer, Integer, Integer, Double, Double, Double> valuesRow() {
		return (Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field1() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.DESINFECTION_DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field6() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.COUNT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field7() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.CHLORINEQTY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field8() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.DEMANDSPOONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field9() {
		return Catchmentdesinfection.CATCHMENTDESINFECTION.RETENTIONTIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value1() {
		return getDesinfectionDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getCatchmentIdcatchment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getWatersystemIdwatersystem();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value4() {
		return getWatersystemCommunityIdcommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value5() {
		return getWatersystemCommunitySubbasinIdsubbasin();
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
		return getChlorineqty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value8() {
		return getDemandspoons();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value9() {
		return getRetentiontime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value1(Timestamp value) {
		setDesinfectionDate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value2(Integer value) {
		setCatchmentIdcatchment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value3(Integer value) {
		setWatersystemIdwatersystem(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value4(Integer value) {
		setWatersystemCommunityIdcommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value5(Integer value) {
		setWatersystemCommunitySubbasinIdsubbasin(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value6(Integer value) {
		setCount(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value7(Double value) {
		setChlorineqty(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value8(Double value) {
		setDemandspoons(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord value9(Double value) {
		setRetentiontime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentdesinfectionRecord values(Timestamp value1, Integer value2, Integer value3, Integer value4, Integer value5, Integer value6, Double value7, Double value8, Double value9) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		value8(value8);
		value9(value9);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached CatchmentdesinfectionRecord
	 */
	public CatchmentdesinfectionRecord() {
		super(Catchmentdesinfection.CATCHMENTDESINFECTION);
	}

	/**
	 * Create a detached, initialised CatchmentdesinfectionRecord
	 */
	public CatchmentdesinfectionRecord(Timestamp desinfectionDate, Integer catchmentIdcatchment, Integer watersystemIdwatersystem, Integer watersystemCommunityIdcommunity, Integer watersystemCommunitySubbasinIdsubbasin, Integer count, Double chlorineqty, Double demandspoons, Double retentiontime) {
		super(Catchmentdesinfection.CATCHMENTDESINFECTION);

		setValue(0, desinfectionDate);
		setValue(1, catchmentIdcatchment);
		setValue(2, watersystemIdwatersystem);
		setValue(3, watersystemCommunityIdcommunity);
		setValue(4, watersystemCommunitySubbasinIdsubbasin);
		setValue(5, count);
		setValue(6, chlorineqty);
		setValue(7, demandspoons);
		setValue(8, retentiontime);
	}
}