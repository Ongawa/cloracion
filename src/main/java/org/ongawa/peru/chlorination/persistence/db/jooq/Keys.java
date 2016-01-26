/**
 * This class is generated by jOOQ
 */
package org.ongawa.peru.chlorination.persistence.db.jooq;


import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Backup;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchment;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Catchmentdesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Chlorinecalculation;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Community;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoir;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Cubicreservoirdesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Desinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuredflow;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Measuringpoint;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipe;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Pipedesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Reliefvalve;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Reliefvalvedesinfection;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Subbasin;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Waterspring;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.Watersystem;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.WatersystemHasWaterspring;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.BackupRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CatchmentRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CatchmentdesinfectionRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.ChlorinecalculationRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CommunityRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.CubicreservoirdesinfectionRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.DesinfectionRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.MeasuredflowRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.MeasuringpointRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.PipeRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.PipedesinfectionRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.ReliefvalveRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.ReliefvalvedesinfectionRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.SubbasinRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.WaterspringRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.WatersystemHasWaterspringRecord;
import org.ongawa.peru.chlorination.persistence.db.jooq.tables.records.WatersystemRecord;


/**
 * A class modelling foreign key relationships between tables of the <code>PUBLIC</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<SubbasinRecord, Integer> IDENTITY_SUBBASIN = Identities0.IDENTITY_SUBBASIN;
	public static final Identity<CommunityRecord, Integer> IDENTITY_COMMUNITY = Identities0.IDENTITY_COMMUNITY;
	public static final Identity<WatersystemRecord, Integer> IDENTITY_WATERSYSTEM = Identities0.IDENTITY_WATERSYSTEM;
	public static final Identity<WaterspringRecord, Integer> IDENTITY_WATERSPRING = Identities0.IDENTITY_WATERSPRING;
	public static final Identity<MeasuringpointRecord, Integer> IDENTITY_MEASURINGPOINT = Identities0.IDENTITY_MEASURINGPOINT;
	public static final Identity<CubicreservoirRecord, Integer> IDENTITY_CUBICRESERVOIR = Identities0.IDENTITY_CUBICRESERVOIR;
	public static final Identity<PipeRecord, Integer> IDENTITY_PIPE = Identities0.IDENTITY_PIPE;
	public static final Identity<CatchmentRecord, Integer> IDENTITY_CATCHMENT = Identities0.IDENTITY_CATCHMENT;
	public static final Identity<ReliefvalveRecord, Integer> IDENTITY_RELIEFVALVE = Identities0.IDENTITY_RELIEFVALVE;
	public static final Identity<BackupRecord, Integer> IDENTITY_BACKUP = Identities0.IDENTITY_BACKUP;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<SubbasinRecord> CONSTRAINT_B = UniqueKeys0.CONSTRAINT_B;
	public static final UniqueKey<CommunityRecord> CONSTRAINT_4 = UniqueKeys0.CONSTRAINT_4;
	public static final UniqueKey<WatersystemRecord> CONSTRAINT_C = UniqueKeys0.CONSTRAINT_C;
	public static final UniqueKey<WaterspringRecord> CONSTRAINT_CA = UniqueKeys0.CONSTRAINT_CA;
	public static final UniqueKey<WatersystemHasWaterspringRecord> CONSTRAINT_2 = UniqueKeys0.CONSTRAINT_2;
	public static final UniqueKey<MeasuringpointRecord> CONSTRAINT_48 = UniqueKeys0.CONSTRAINT_48;
	public static final UniqueKey<MeasuredflowRecord> CONSTRAINT_7 = UniqueKeys0.CONSTRAINT_7;
	public static final UniqueKey<ChlorinecalculationRecord> CONSTRAINT_E = UniqueKeys0.CONSTRAINT_E;
	public static final UniqueKey<CubicreservoirRecord> CONSTRAINT_CD = UniqueKeys0.CONSTRAINT_CD;
	public static final UniqueKey<PipeRecord> CONSTRAINT_25 = UniqueKeys0.CONSTRAINT_25;
	public static final UniqueKey<CatchmentRecord> CONSTRAINT_1 = UniqueKeys0.CONSTRAINT_1;
	public static final UniqueKey<ReliefvalveRecord> CONSTRAINT_EA = UniqueKeys0.CONSTRAINT_EA;
	public static final UniqueKey<CubicreservoirdesinfectionRecord> CONSTRAINT_3 = UniqueKeys0.CONSTRAINT_3;
	public static final UniqueKey<PipedesinfectionRecord> CONSTRAINT_3B = UniqueKeys0.CONSTRAINT_3B;
	public static final UniqueKey<ReliefvalvedesinfectionRecord> CONSTRAINT_74 = UniqueKeys0.CONSTRAINT_74;
	public static final UniqueKey<DesinfectionRecord> CONSTRAINT_3F = UniqueKeys0.CONSTRAINT_3F;
	public static final UniqueKey<CatchmentdesinfectionRecord> CONSTRAINT_1E = UniqueKeys0.CONSTRAINT_1E;
	public static final UniqueKey<BackupRecord> CONSTRAINT_745 = UniqueKeys0.CONSTRAINT_745;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<CommunityRecord, SubbasinRecord> FK_COMMUNITY_SUBBASIN1 = ForeignKeys0.FK_COMMUNITY_SUBBASIN1;
	public static final ForeignKey<WatersystemRecord, CommunityRecord> FK_WATERSYSTEM_COMMUNITY1 = ForeignKeys0.FK_WATERSYSTEM_COMMUNITY1;
	public static final ForeignKey<WatersystemHasWaterspringRecord, WatersystemRecord> FK_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM1 = ForeignKeys0.FK_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM1;
	public static final ForeignKey<WatersystemHasWaterspringRecord, WaterspringRecord> FK_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING1 = ForeignKeys0.FK_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING1;
	public static final ForeignKey<MeasuringpointRecord, WatersystemHasWaterspringRecord> FK_MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING1 = ForeignKeys0.FK_MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING1;
	public static final ForeignKey<MeasuredflowRecord, MeasuringpointRecord> FK_MEASUREDFLOW_MEASURINGPOINT1 = ForeignKeys0.FK_MEASUREDFLOW_MEASURINGPOINT1;
	public static final ForeignKey<ChlorinecalculationRecord, WatersystemRecord> FK_CHLORINECALCULATION_WATERSYSTEM1 = ForeignKeys0.FK_CHLORINECALCULATION_WATERSYSTEM1;
	public static final ForeignKey<CubicreservoirRecord, WatersystemRecord> FK_CUBICRESERVOIR_WATERSYSTEM1 = ForeignKeys0.FK_CUBICRESERVOIR_WATERSYSTEM1;
	public static final ForeignKey<PipeRecord, WatersystemRecord> FK_PIPE_WATERSYSTEM1 = ForeignKeys0.FK_PIPE_WATERSYSTEM1;
	public static final ForeignKey<CatchmentRecord, WatersystemRecord> FK_CATCHMENT_WATERSYSTEM1 = ForeignKeys0.FK_CATCHMENT_WATERSYSTEM1;
	public static final ForeignKey<ReliefvalveRecord, WatersystemRecord> FK_RELIEFVALVE_WATERSYSTEM1 = ForeignKeys0.FK_RELIEFVALVE_WATERSYSTEM1;
	public static final ForeignKey<CubicreservoirdesinfectionRecord, DesinfectionRecord> FK_CUBICRESERVOIRDESINFECTION_DESINFECTION1 = ForeignKeys0.FK_CUBICRESERVOIRDESINFECTION_DESINFECTION1;
	public static final ForeignKey<PipedesinfectionRecord, DesinfectionRecord> FK_PIPEDESINFECTION_DESINFECTION1 = ForeignKeys0.FK_PIPEDESINFECTION_DESINFECTION1;
	public static final ForeignKey<ReliefvalvedesinfectionRecord, DesinfectionRecord> FK_RELIEFVALVEDESINFECTION_DESINFECTION1 = ForeignKeys0.FK_RELIEFVALVEDESINFECTION_DESINFECTION1;
	public static final ForeignKey<DesinfectionRecord, WatersystemRecord> FK_DESINFECTION_WATERSYSTEM1 = ForeignKeys0.FK_DESINFECTION_WATERSYSTEM1;
	public static final ForeignKey<CatchmentdesinfectionRecord, DesinfectionRecord> FK_CATCHMENTDESINFECTION_DESINFECTION1 = ForeignKeys0.FK_CATCHMENTDESINFECTION_DESINFECTION1;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<SubbasinRecord, Integer> IDENTITY_SUBBASIN = createIdentity(Subbasin.SUBBASIN, Subbasin.SUBBASIN.IDSUBBASIN);
		public static Identity<CommunityRecord, Integer> IDENTITY_COMMUNITY = createIdentity(Community.COMMUNITY, Community.COMMUNITY.IDCOMMUNITY);
		public static Identity<WatersystemRecord, Integer> IDENTITY_WATERSYSTEM = createIdentity(Watersystem.WATERSYSTEM, Watersystem.WATERSYSTEM.IDWATERSYSTEM);
		public static Identity<WaterspringRecord, Integer> IDENTITY_WATERSPRING = createIdentity(Waterspring.WATERSPRING, Waterspring.WATERSPRING.IDWATERSPRING);
		public static Identity<MeasuringpointRecord, Integer> IDENTITY_MEASURINGPOINT = createIdentity(Measuringpoint.MEASURINGPOINT, Measuringpoint.MEASURINGPOINT.IDMEASURINGPOINT);
		public static Identity<CubicreservoirRecord, Integer> IDENTITY_CUBICRESERVOIR = createIdentity(Cubicreservoir.CUBICRESERVOIR, Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR);
		public static Identity<PipeRecord, Integer> IDENTITY_PIPE = createIdentity(Pipe.PIPE, Pipe.PIPE.IDPIPE);
		public static Identity<CatchmentRecord, Integer> IDENTITY_CATCHMENT = createIdentity(Catchment.CATCHMENT, Catchment.CATCHMENT.IDCATCHMENT);
		public static Identity<ReliefvalveRecord, Integer> IDENTITY_RELIEFVALVE = createIdentity(Reliefvalve.RELIEFVALVE, Reliefvalve.RELIEFVALVE.IDRELIEFVALVE);
		public static Identity<BackupRecord, Integer> IDENTITY_BACKUP = createIdentity(Backup.BACKUP, Backup.BACKUP.IDBACKUP);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<SubbasinRecord> CONSTRAINT_B = createUniqueKey(Subbasin.SUBBASIN, Subbasin.SUBBASIN.IDSUBBASIN);
		public static final UniqueKey<CommunityRecord> CONSTRAINT_4 = createUniqueKey(Community.COMMUNITY, Community.COMMUNITY.IDCOMMUNITY, Community.COMMUNITY.SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<WatersystemRecord> CONSTRAINT_C = createUniqueKey(Watersystem.WATERSYSTEM, Watersystem.WATERSYSTEM.IDWATERSYSTEM, Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY, Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<WaterspringRecord> CONSTRAINT_CA = createUniqueKey(Waterspring.WATERSPRING, Waterspring.WATERSPRING.IDWATERSPRING);
		public static final UniqueKey<WatersystemHasWaterspringRecord> CONSTRAINT_2 = createUniqueKey(WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_IDWATERSYSTEM, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSPRING_IDWATERSPRING);
		public static final UniqueKey<MeasuringpointRecord> CONSTRAINT_48 = createUniqueKey(Measuringpoint.MEASURINGPOINT, Measuringpoint.MEASURINGPOINT.IDMEASURINGPOINT, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING);
		public static final UniqueKey<MeasuredflowRecord> CONSTRAINT_7 = createUniqueKey(Measuredflow.MEASUREDFLOW, Measuredflow.MEASUREDFLOW.DATE, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING);
		public static final UniqueKey<ChlorinecalculationRecord> CONSTRAINT_E = createUniqueKey(Chlorinecalculation.CHLORINECALCULATION, Chlorinecalculation.CHLORINECALCULATION.DATE, Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM, Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<CubicreservoirRecord> CONSTRAINT_CD = createUniqueKey(Cubicreservoir.CUBICRESERVOIR, Cubicreservoir.CUBICRESERVOIR.IDCUBICRESERVOIR, Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM, Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<PipeRecord> CONSTRAINT_25 = createUniqueKey(Pipe.PIPE, Pipe.PIPE.IDPIPE, Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM, Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<CatchmentRecord> CONSTRAINT_1 = createUniqueKey(Catchment.CATCHMENT, Catchment.CATCHMENT.IDCATCHMENT, Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM, Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<ReliefvalveRecord> CONSTRAINT_EA = createUniqueKey(Reliefvalve.RELIEFVALVE, Reliefvalve.RELIEFVALVE.IDRELIEFVALVE, Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM, Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<CubicreservoirdesinfectionRecord> CONSTRAINT_3 = createUniqueKey(Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.CUBICRESERVOIR_IDCUBICRESERVOIR, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<PipedesinfectionRecord> CONSTRAINT_3B = createUniqueKey(Pipedesinfection.PIPEDESINFECTION, Pipedesinfection.PIPEDESINFECTION.DESINFECTION_DATE, Pipedesinfection.PIPEDESINFECTION.PIPE_IDPIPE, Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<ReliefvalvedesinfectionRecord> CONSTRAINT_74 = createUniqueKey(Reliefvalvedesinfection.RELIEFVALVEDESINFECTION, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DESINFECTION_DATE, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.RELIEFVALVE_IDRELIEFVALVE, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<DesinfectionRecord> CONSTRAINT_3F = createUniqueKey(Desinfection.DESINFECTION, Desinfection.DESINFECTION.DATE, Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<CatchmentdesinfectionRecord> CONSTRAINT_1E = createUniqueKey(Catchmentdesinfection.CATCHMENTDESINFECTION, Catchmentdesinfection.CATCHMENTDESINFECTION.DESINFECTION_DATE, Catchmentdesinfection.CATCHMENTDESINFECTION.CATCHMENT_IDCATCHMENT, Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final UniqueKey<BackupRecord> CONSTRAINT_745 = createUniqueKey(Backup.BACKUP, Backup.BACKUP.IDBACKUP);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<CommunityRecord, SubbasinRecord> FK_COMMUNITY_SUBBASIN1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_B, Community.COMMUNITY, Community.COMMUNITY.SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<WatersystemRecord, CommunityRecord> FK_WATERSYSTEM_COMMUNITY1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_4, Watersystem.WATERSYSTEM, Watersystem.WATERSYSTEM.COMMUNITY_IDCOMMUNITY, Watersystem.WATERSYSTEM.COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<WatersystemHasWaterspringRecord, WatersystemRecord> FK_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_IDWATERSYSTEM, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<WatersystemHasWaterspringRecord, WaterspringRecord> FK_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_CA, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING, WatersystemHasWaterspring.WATERSYSTEM_HAS_WATERSPRING.WATERSPRING_IDWATERSPRING);
		public static final ForeignKey<MeasuringpointRecord, WatersystemHasWaterspringRecord> FK_MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_2, Measuringpoint.MEASURINGPOINT, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN, Measuringpoint.MEASURINGPOINT.WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING);
		public static final ForeignKey<MeasuredflowRecord, MeasuringpointRecord> FK_MEASUREDFLOW_MEASURINGPOINT1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_48, Measuredflow.MEASUREDFLOW, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_IDMEASURINGPOINT, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_IDWATERSYSTEM, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN, Measuredflow.MEASUREDFLOW.MEASURINGPOINT_WATERSYSTEM_HAS_WATERSPRING_WATERSPRING_IDWATERSPRING);
		public static final ForeignKey<ChlorinecalculationRecord, WatersystemRecord> FK_CHLORINECALCULATION_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, Chlorinecalculation.CHLORINECALCULATION, Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_IDWATERSYSTEM, Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Chlorinecalculation.CHLORINECALCULATION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<CubicreservoirRecord, WatersystemRecord> FK_CUBICRESERVOIR_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, Cubicreservoir.CUBICRESERVOIR, Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_IDWATERSYSTEM, Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Cubicreservoir.CUBICRESERVOIR.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<PipeRecord, WatersystemRecord> FK_PIPE_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, Pipe.PIPE, Pipe.PIPE.WATERSYSTEM_IDWATERSYSTEM, Pipe.PIPE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Pipe.PIPE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<CatchmentRecord, WatersystemRecord> FK_CATCHMENT_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, Catchment.CATCHMENT, Catchment.CATCHMENT.WATERSYSTEM_IDWATERSYSTEM, Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Catchment.CATCHMENT.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<ReliefvalveRecord, WatersystemRecord> FK_RELIEFVALVE_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, Reliefvalve.RELIEFVALVE, Reliefvalve.RELIEFVALVE.WATERSYSTEM_IDWATERSYSTEM, Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Reliefvalve.RELIEFVALVE.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<CubicreservoirdesinfectionRecord, DesinfectionRecord> FK_CUBICRESERVOIRDESINFECTION_DESINFECTION1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_3F, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.DESINFECTION_DATE, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Cubicreservoirdesinfection.CUBICRESERVOIRDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<PipedesinfectionRecord, DesinfectionRecord> FK_PIPEDESINFECTION_DESINFECTION1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_3F, Pipedesinfection.PIPEDESINFECTION, Pipedesinfection.PIPEDESINFECTION.DESINFECTION_DATE, Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Pipedesinfection.PIPEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<ReliefvalvedesinfectionRecord, DesinfectionRecord> FK_RELIEFVALVEDESINFECTION_DESINFECTION1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_3F, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.DESINFECTION_DATE, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Reliefvalvedesinfection.RELIEFVALVEDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<DesinfectionRecord, WatersystemRecord> FK_DESINFECTION_WATERSYSTEM1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_C, Desinfection.DESINFECTION, Desinfection.DESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Desinfection.DESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
		public static final ForeignKey<CatchmentdesinfectionRecord, DesinfectionRecord> FK_CATCHMENTDESINFECTION_DESINFECTION1 = createForeignKey(org.ongawa.peru.chlorination.persistence.db.jooq.Keys.CONSTRAINT_3F, Catchmentdesinfection.CATCHMENTDESINFECTION, Catchmentdesinfection.CATCHMENTDESINFECTION.DESINFECTION_DATE, Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_IDWATERSYSTEM, Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_IDCOMMUNITY, Catchmentdesinfection.CATCHMENTDESINFECTION.WATERSYSTEM_COMMUNITY_SUBBASIN_IDSUBBASIN);
	}
}
