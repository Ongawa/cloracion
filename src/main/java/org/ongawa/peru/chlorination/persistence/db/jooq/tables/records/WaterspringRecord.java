/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Waterspring;


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
public class WaterspringRecord extends UpdatableRecordImpl<WaterspringRecord> implements Record2<Integer, String> {

	private static final long serialVersionUID = -1199477336;

	/**
	 * Setter for <code>PUBLIC.WATERSPRING.IDWATERSPRING</code>.
	 */
	public void setIdwaterspring(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>PUBLIC.WATERSPRING.IDWATERSPRING</code>.
	 */
	public Integer getIdwaterspring() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>PUBLIC.WATERSPRING.NAME</code>.
	 */
	public void setName(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>PUBLIC.WATERSPRING.NAME</code>.
	 */
	public String getName() {
		return (String) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Waterspring.WATERSPRING.IDWATERSPRING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Waterspring.WATERSPRING.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getIdwaterspring();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WaterspringRecord value1(Integer value) {
		setIdwaterspring(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WaterspringRecord value2(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WaterspringRecord values(Integer value1, String value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached WaterspringRecord
	 */
	public WaterspringRecord() {
		super(Waterspring.WATERSPRING);
	}

	/**
	 * Create a detached, initialised WaterspringRecord
	 */
	public WaterspringRecord(Integer idwaterspring, String name) {
		super(Waterspring.WATERSPRING);

		setValue(0, idwaterspring);
		setValue(1, name);
	}
}
