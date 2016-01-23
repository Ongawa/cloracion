/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection;


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
public class DesinfectionRecord extends UpdatableRecordImpl<DesinfectionRecord> implements Record7<Timestamp, Integer, Integer, Integer, String, Double, Double> {

	private static final long serialVersionUID = 418384655;

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.DATE</code>.
	 */
	public void setDate(Timestamp value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.DATE</code>.
	 */
	public Timestamp getDate() {
		return (Timestamp) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public void setWatersystemIdwatersystem(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public Integer getWatersystemIdwatersystem() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public void setWatersystemCommunityIdcommunity(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public Integer getWatersystemCommunityIdcommunity() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public void setWatersystemCommunitySubbasinIdsubbasin(Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public Integer getWatersystemCommunitySubbasinIdsubbasin() {
		return (Integer) getValue(3);
	}

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.CHLORINETYPE</code>.
	 */
	public void setChlorinetype(String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.CHLORINETYPE</code>.
	 */
	public String getChlorinetype() {
		return (String) getValue(4);
	}

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.CHLORINEPURENESS</code>.
	 */
	public void setChlorinepureness(Double value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.CHLORINEPURENESS</code>.
	 */
	public Double getChlorinepureness() {
		return (Double) getValue(5);
	}

	/**
	 * Setter for <code>PUBLIC.DESINFECTION.CHLORINEPRICE</code>.
	 */
	public void setChlorineprice(Double value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>PUBLIC.DESINFECTION.CHLORINEPRICE</code>.
	 */
	public Double getChlorineprice() {
		return (Double) getValue(6);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record4<Timestamp, Integer, Integer, Integer> key() {
		return (Record4) super.key();
	}

	// -------------------------------------------------------------------------
	// Record7 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row7<Timestamp, Integer, Integer, Integer, String, Double, Double> fieldsRow() {
		return (Row7) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row7<Timestamp, Integer, Integer, Integer, String, Double, Double> valuesRow() {
		return (Row7) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field1() {
		return Desinfection.DESINFECTION.DATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field5() {
		return Desinfection.DESINFECTION.CHLORINETYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field6() {
		return Desinfection.DESINFECTION.CHLORINEPURENESS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field7() {
		return Desinfection.DESINFECTION.CHLORINEPRICE;
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
		return getWatersystemIdwatersystem();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getWatersystemCommunityIdcommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value4() {
		return getWatersystemCommunitySubbasinIdsubbasin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value5() {
		return getChlorinetype();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value6() {
		return getChlorinepureness();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value7() {
		return getChlorineprice();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value1(Timestamp value) {
		setDate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value2(Integer value) {
		setWatersystemIdwatersystem(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value3(Integer value) {
		setWatersystemCommunityIdcommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value4(Integer value) {
		setWatersystemCommunitySubbasinIdsubbasin(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value5(String value) {
		setChlorinetype(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value6(Double value) {
		setChlorinepureness(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord value7(Double value) {
		setChlorineprice(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesinfectionRecord values(Timestamp value1, Integer value2, Integer value3, Integer value4, String value5, Double value6, Double value7) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached DesinfectionRecord
	 */
	public DesinfectionRecord() {
		super(Desinfection.DESINFECTION);
	}

	/**
	 * Create a detached, initialised DesinfectionRecord
	 */
	public DesinfectionRecord(Timestamp date, Integer watersystemIdwatersystem, Integer watersystemCommunityIdcommunity, Integer watersystemCommunitySubbasinIdsubbasin, String chlorinetype, Double chlorinepureness, Double chlorineprice) {
		super(Desinfection.DESINFECTION);

		setValue(0, date);
		setValue(1, watersystemIdwatersystem);
		setValue(2, watersystemCommunityIdcommunity);
		setValue(3, watersystemCommunitySubbasinIdsubbasin);
		setValue(4, chlorinetype);
		setValue(5, chlorinepureness);
		setValue(6, chlorineprice);
	}
}
