/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record4;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment;


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
public class CatchmentRecord extends UpdatableRecordImpl<CatchmentRecord> implements Record9<Integer, Integer, Integer, Integer, String, Double, Double, Double, Integer> {

	private static final long serialVersionUID = 998213116;

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.IDCATCHMENT</code>.
	 */
	public void setIdcatchment(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.IDCATCHMENT</code>.
	 */
	public Integer getIdcatchment() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public void setWatersystemIdwatersystem(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM</code>.
	 */
	public Integer getWatersystemIdwatersystem() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public void setWatersystemCommunityIdcommunity(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY</code>.
	 */
	public Integer getWatersystemCommunityIdcommunity() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public void setWatersystemCommunitySubbasinIdsubbasin(Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN</code>.
	 */
	public Integer getWatersystemCommunitySubbasinIdsubbasin() {
		return (Integer) getValue(3);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.NAME</code>.
	 */
	public void setName(String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.NAME</code>.
	 */
	public String getName() {
		return (String) getValue(4);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.WIDTH</code>.
	 */
	public void setWidth(Double value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.WIDTH</code>.
	 */
	public Double getWidth() {
		return (Double) getValue(5);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.LENGTH</code>.
	 */
	public void setLength(Double value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.LENGTH</code>.
	 */
	public Double getLength() {
		return (Double) getValue(6);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.HEIGHT</code>.
	 */
	public void setHeight(Double value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.HEIGHT</code>.
	 */
	public Double getHeight() {
		return (Double) getValue(7);
	}

	/**
	 * Setter for <code>PUBLIC.CATCHMENT.COUNT</code>.
	 */
	public void setCount(Integer value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>PUBLIC.CATCHMENT.COUNT</code>.
	 */
	public Integer getCount() {
		return (Integer) getValue(8);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record4<Integer, Integer, Integer, Integer> key() {
		return (Record4) super.key();
	}

	// -------------------------------------------------------------------------
	// Record9 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row9<Integer, Integer, Integer, Integer, String, Double, Double, Double, Integer> fieldsRow() {
		return (Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row9<Integer, Integer, Integer, Integer, String, Double, Double, Double, Integer> valuesRow() {
		return (Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Catchment.CATCHMENT.IDCATCHMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field5() {
		return Catchment.CATCHMENT.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field6() {
		return Catchment.CATCHMENT.WIDTH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field7() {
		return Catchment.CATCHMENT.LENGTH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Double> field8() {
		return Catchment.CATCHMENT.HEIGHT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field9() {
		return Catchment.CATCHMENT.COUNT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getIdcatchment();
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
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value6() {
		return getWidth();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value7() {
		return getLength();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double value8() {
		return getHeight();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value9() {
		return getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value1(Integer value) {
		setIdcatchment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value2(Integer value) {
		setWatersystemIdwatersystem(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value3(Integer value) {
		setWatersystemCommunityIdcommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value4(Integer value) {
		setWatersystemCommunitySubbasinIdsubbasin(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value5(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value6(Double value) {
		setWidth(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value7(Double value) {
		setLength(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value8(Double value) {
		setHeight(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord value9(Integer value) {
		setCount(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CatchmentRecord values(Integer value1, Integer value2, Integer value3, Integer value4, String value5, Double value6, Double value7, Double value8, Integer value9) {
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
	 * Create a detached CatchmentRecord
	 */
	public CatchmentRecord() {
		super(Catchment.CATCHMENT);
	}

	/**
	 * Create a detached, initialised CatchmentRecord
	 */
	public CatchmentRecord(Integer idcatchment, Integer watersystemIdwatersystem, Integer watersystemCommunityIdcommunity, Integer watersystemCommunitySubbasinIdsubbasin, String name, Double width, Double length, Double height, Integer count) {
		super(Catchment.CATCHMENT);

		setValue(0, idcatchment);
		setValue(1, watersystemIdwatersystem);
		setValue(2, watersystemCommunityIdcommunity);
		setValue(3, watersystemCommunitySubbasinIdsubbasin);
		setValue(4, name);
		setValue(5, width);
		setValue(6, length);
		setValue(7, height);
		setValue(8, count);
	}
}
