/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Chlorinecalculation;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoir;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuredflow;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuringpoint;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Reliefvalve;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Waterspring;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Watersystem;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.WatersystemHasWaterspring;


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
public class Public extends SchemaImpl {

	private static final long serialVersionUID = 2117186811;

	/**
	 * The reference instance of <code>PUBLIC</code>
	 */
	public static final Public PUBLIC = new Public();

	/**
	 * No further instances allowed
	 */
	private Public() {
		super("PUBLIC");
	}

	@Override
	public final List<Sequence<?>> getSequences() {
		List result = new ArrayList();
		result.addAll(getSequences0());
		return result;
	}

	private final List<Sequence<?>> getSequences0() {
		return Arrays.<Sequence<?>>asList(
			Sequences.SYSTEM_SEQUENCE_0DCBAB1F_4B5A_41B1_9A54_41B5BAAD3477,
			Sequences.SYSTEM_SEQUENCE_2E144B2F_C509_44D4_AF51_12FD446444EF,
			Sequences.SYSTEM_SEQUENCE_4DB32937_93F1_4362_8A5F_C4F95EBD8CAD,
			Sequences.SYSTEM_SEQUENCE_715FB6FF_7F5C_4126_8F64_8B8D39A7BCEE,
			Sequences.SYSTEM_SEQUENCE_8F8999BA_7242_461E_A2F0_82D268F6E5AA,
			Sequences.SYSTEM_SEQUENCE_AB7EC66E_31A4_4398_9FA4_DF2EFC601005,
			Sequences.SYSTEM_SEQUENCE_EE51FDD7_606F_47EA_BB94_E4EE53EBD082,
			Sequences.SYSTEM_SEQUENCE_F7EF39F7_73F4_4A2E_88DC_E706C6A48CDD);
	}

	@Override
	public final List<Table<?>> getTables() {
		List result = new ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final List<Table<?>> getTables0() {
		return Arrays.<Table<?>>asList(
			Subbasin.SUBBASIN,
			Community.COMMUNITY,
			Watersystem.WATERSYSTEM,
			Waterspring.WATERSPRING,
			WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING,
			Measuringpoint.MEASURINGPOINT,
			Measuredflow.MEASUREDFLOW,
			Chlorinecalculation.CHLORINECALCULATION,
			Cubicreservoir.CUBICRESERVOIR,
			Pipe.PIPE,
			Reliefvalve.RELIEFVALVE);
	}
}