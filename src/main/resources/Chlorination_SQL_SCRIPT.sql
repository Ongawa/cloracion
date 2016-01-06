-- -----------------------------------------------------
-- Table SubBasin
-- -----------------------------------------------------
DROP TABLE IF EXISTS SubBasin ;

CREATE TABLE IF NOT EXISTS SubBasin (
  idSubBasin INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (idSubBasin))
;

CREATE UNIQUE INDEX subbasin_name_UNIQUE ON SubBasin (name ASC);


-- -----------------------------------------------------
-- Table Community
-- -----------------------------------------------------
DROP TABLE IF EXISTS Community ;

CREATE TABLE IF NOT EXISTS Community (
  idCommunity INT NOT NULL AUTO_INCREMENT,
  SubBasin_idSubBasin INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (idCommunity, SubBasin_idSubBasin),
  CONSTRAINT fk_Community_SubBasin1
    FOREIGN KEY (SubBasin_idSubBasin)
    REFERENCES SubBasin (idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_Community_SubBasin1_idx ON Community (SubBasin_idSubBasin ASC);

CREATE UNIQUE INDEX community_name_UNIQUE ON Community (name ASC);


-- -----------------------------------------------------
-- Table WaterSystem
-- -----------------------------------------------------
DROP TABLE IF EXISTS WaterSystem ;

CREATE TABLE IF NOT EXISTS WaterSystem (
  idWaterSystem INT NOT NULL AUTO_INCREMENT,
  Community_idCommunity INT NOT NULL,
  Community_SubBasin_idSubBasin INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  familiesNum INT NOT NULL,
  population INT NULL COMMENT 'Estimado a 5 miembros por familia',
  populationForecast INT NULL COMMENT 'Proyección de habitantes en 20 años',
  growingIndex DOUBLE NULL COMMENT 'Expresado en %',
  endowment DOUBLE NULL,
  JASSNum INT NULL,
  futureNeededFlow DOUBLE NULL,
  reservoirVolume DOUBLE NULL COMMENT 'Unidad en metros cúbicos',
  systemElevation INT NULL COMMENT 'Altura msnm',
  PRIMARY KEY (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin),
  CONSTRAINT fk_WaterSystem_Community1
    FOREIGN KEY (Community_idCommunity , Community_SubBasin_idSubBasin)
    REFERENCES Community (idCommunity , SubBasin_idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_WaterSystem_Community1_idx ON WaterSystem (Community_idCommunity ASC, Community_SubBasin_idSubBasin ASC);

CREATE UNIQUE INDEX watersystem_name_UNIQUE ON WaterSystem (name ASC);


-- -----------------------------------------------------
-- Table WaterSpring
-- -----------------------------------------------------
DROP TABLE IF EXISTS WaterSpring ;

CREATE TABLE IF NOT EXISTS WaterSpring (
  idWaterSpring INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (idWaterSpring))
;

CREATE UNIQUE INDEX waterspring_name_UNIQUE ON WaterSpring (name ASC);


-- -----------------------------------------------------
-- Table WaterSystem_has_WaterSpring
-- -----------------------------------------------------
DROP TABLE IF EXISTS WaterSystem_has_WaterSpring ;

CREATE TABLE IF NOT EXISTS WaterSystem_has_WaterSpring (
  WaterSystem_idWaterSystem INT NOT NULL,
  WaterSystem_Community_idCommunity INT NOT NULL,
  WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  WaterSpring_idWaterSpring INT NOT NULL,
  PRIMARY KEY (WaterSystem_idWaterSystem, WaterSystem_Community_idCommunity, WaterSystem_Community_SubBasin_idSubBasin, WaterSpring_idWaterSpring),
  CONSTRAINT fk_WaterSystem_has_WaterSpring_WaterSystem1
    FOREIGN KEY (WaterSystem_idWaterSystem , WaterSystem_Community_idCommunity , WaterSystem_Community_SubBasin_idSubBasin)
    REFERENCES WaterSystem (idWaterSystem , Community_idCommunity , Community_SubBasin_idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_WaterSystem_has_WaterSpring_WaterSpring1
    FOREIGN KEY (WaterSpring_idWaterSpring)
    REFERENCES WaterSpring (idWaterSpring)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_WaterSystem_has_WaterSpring_WaterSpring1_idx ON WaterSystem_has_WaterSpring (WaterSpring_idWaterSpring ASC);

CREATE INDEX fk_WaterSystem_has_WaterSpring_WaterSystem1_idx ON WaterSystem_has_WaterSpring (WaterSystem_idWaterSystem ASC, WaterSystem_Community_idCommunity ASC, WaterSystem_Community_SubBasin_idSubBasin ASC);


-- -----------------------------------------------------
-- Table MeasuringPoint
-- -----------------------------------------------------
DROP TABLE IF EXISTS MeasuringPoint ;

CREATE TABLE IF NOT EXISTS MeasuringPoint (
  idMeasuringPoint INT NOT NULL AUTO_INCREMENT,
  WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem INT NOT NULL,
  WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity INT NOT NULL,
  WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (idMeasuringPoint, WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring),
  CONSTRAINT fk_MeasuringPoint_WaterSystem_has_WaterSpring1
    FOREIGN KEY (WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem , WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity , WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin , WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring)
    REFERENCES WaterSystem_has_WaterSpring (WaterSystem_idWaterSystem , WaterSystem_Community_idCommunity , WaterSystem_Community_SubBasin_idSubBasin , WaterSpring_idWaterSpring)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_MeasuringPoint_WaterSystem_has_WaterSpring1_idx ON MeasuringPoint (WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem ASC, WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity ASC, WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin ASC, WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring ASC);

CREATE UNIQUE INDEX measuringpoint_name_UNIQUE ON MeasuringPoint (idMeasuringPoint, WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem,  WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity,  WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin,  WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, name ASC);


-- -----------------------------------------------------
-- Table MeasuredFlow
-- -----------------------------------------------------
DROP TABLE IF EXISTS MeasuredFlow ;

CREATE TABLE IF NOT EXISTS MeasuredFlow (
  date TIMESTAMP NOT NULL,
  MeasuringPoint_idMeasuringPoint INT NOT NULL,
  MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem INT NOT NULL,
  MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity INT NOT NULL,
  MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring INT NOT NULL,
  flow DOUBLE(2) NOT NULL,
  comments CLOB NULL,
  PRIMARY KEY (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring),
  CONSTRAINT fk_MeasuredFlow_MeasuringPoint1
    FOREIGN KEY (MeasuringPoint_idMeasuringPoint , MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem , MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity , MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin , MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring)
    REFERENCES MeasuringPoint (idMeasuringPoint , WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem , WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity , WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin , WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_MeasuredFlow_MeasuringPoint1_idx ON MeasuredFlow (MeasuringPoint_idMeasuringPoint ASC, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem ASC, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity ASC, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin ASC, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring ASC);


-- -----------------------------------------------------
-- Table ChlorineCalculation
-- -----------------------------------------------------
DROP TABLE IF EXISTS ChlorineCalculation ;

CREATE TABLE IF NOT EXISTS ChlorineCalculation (
  date DATETIME NOT NULL,
  WaterSystem_idWaterSystem INT NOT NULL,
  WaterSystem_Community_idCommunity INT NOT NULL,
  WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  population INT NULL,
  tankVolume DOUBLE NULL,
  tankUsefulVolume DOUBLE NULL,
  endowment DOUBLE NULL COMMENT 'm^3/persona x dia',
  chlorinePureness DOUBLE NULL COMMENT 'Expresado en %',
  inputFlow DOUBLE NULL COMMENT 'Medido en litros/segundo',
  reloadTime INT NULL COMMENT 'Medido en días',
  demandCLR DOUBLE NOT NULL COMMENT 'Medido en mg/litro',
  demandActiveChlorine DOUBLE NOT NULL COMMENT 'Medido en mg/litro',
  demandCommonProduct DOUBLE NOT NULL COMMENT 'Medido en mg/litro',
  PRIMARY KEY (date, WaterSystem_Community_idCommunity, WaterSystem_idWaterSystem, WaterSystem_Community_SubBasin_idSubBasin),
  CONSTRAINT fk_ChlorineCalculation_WaterSystem1
    FOREIGN KEY (WaterSystem_idWaterSystem , WaterSystem_Community_idCommunity , WaterSystem_Community_SubBasin_idSubBasin)
    REFERENCES WaterSystem (idWaterSystem , Community_idCommunity , Community_SubBasin_idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE)

COMMENT = 'Los campos referentes a numero de habitantes, capacidad del tanque y capacidad útil se ponen duplicados en esta tabla para poder llevar un histórico de todas las mediciones y las características de cada medición.';

CREATE INDEX fk_ChlorineCalculation_WaterSystem1_idx ON ChlorineCalculation (WaterSystem_idWaterSystem ASC, WaterSystem_Community_idCommunity ASC, WaterSystem_Community_SubBasin_idSubBasin ASC);


-- -----------------------------------------------------
-- Table CubicReservoir
-- -----------------------------------------------------
DROP TABLE IF EXISTS CubicReservoir ;

CREATE TABLE IF NOT EXISTS CubicReservoir (
  idCubicReservoir INT NOT NULL AUTO_INCREMENT,
  WaterSystem_idWaterSystem INT NOT NULL,
  WaterSystem_Community_idCommunity INT NOT NULL,
  WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  name VARCHAR(100) NULL,
  width DOUBLE NOT NULL,
  length DOUBLE NOT NULL,
  height DOUBLE NOT NULL,
  count INT NULL DEFAULT 1,
  requiredConcentration DOUBLE NULL,
  PRIMARY KEY (idCubicReservoir, WaterSystem_idWaterSystem, WaterSystem_Community_idCommunity, WaterSystem_Community_SubBasin_idSubBasin),
  CONSTRAINT fk_CubicReservoir_WaterSystem1
    FOREIGN KEY (WaterSystem_idWaterSystem , WaterSystem_Community_idCommunity , WaterSystem_Community_SubBasin_idSubBasin)
    REFERENCES WaterSystem (idWaterSystem , Community_idCommunity , Community_SubBasin_idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_CubicReservoir_WaterSystem1_idx ON CubicReservoir (WaterSystem_idWaterSystem ASC, WaterSystem_Community_idCommunity ASC, WaterSystem_Community_SubBasin_idSubBasin ASC);


-- -----------------------------------------------------
-- Table Pipe
-- -----------------------------------------------------
DROP TABLE IF EXISTS Pipe ;

CREATE TABLE IF NOT EXISTS Pipe (
  idPipe INT NOT NULL AUTO_INCREMENT,
  WaterSystem_idWaterSystem INT NOT NULL,
  WaterSystem_Community_idCommunity INT NOT NULL,
  WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  name VARCHAR(100) NULL,
  diameter DOUBLE NOT NULL,
  length DOUBLE NOT NULL,
  count INT NULL DEFAULT 1,
  requiredConcentration DOUBLE NULL,
  PRIMARY KEY (idPipe, WaterSystem_idWaterSystem, WaterSystem_Community_idCommunity, WaterSystem_Community_SubBasin_idSubBasin),
  CONSTRAINT fk_Pipe_WaterSystem1
    FOREIGN KEY (WaterSystem_idWaterSystem , WaterSystem_Community_idCommunity , WaterSystem_Community_SubBasin_idSubBasin)
    REFERENCES WaterSystem (idWaterSystem , Community_idCommunity , Community_SubBasin_idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_Pipe_WaterSystem1_idx ON Pipe (WaterSystem_idWaterSystem ASC, WaterSystem_Community_idCommunity ASC, WaterSystem_Community_SubBasin_idSubBasin ASC);


-- -----------------------------------------------------
-- Table ReliefValve
-- -----------------------------------------------------
DROP TABLE IF EXISTS ReliefValve ;

CREATE TABLE IF NOT EXISTS ReliefValve (
  idReliefValve INT NOT NULL AUTO_INCREMENT,
  WaterSystem_idWaterSystem INT NOT NULL,
  WaterSystem_Community_idCommunity INT NOT NULL,
  WaterSystem_Community_SubBasin_idSubBasin INT NOT NULL,
  name VARCHAR(100) NULL,
  width DOUBLE NOT NULL,
  length DOUBLE NOT NULL,
  height DOUBLE NOT NULL,
  count INT NULL DEFAULT 1,
  requiredConcentration DOUBLE NULL,
  PRIMARY KEY (idReliefValve, WaterSystem_idWaterSystem, WaterSystem_Community_idCommunity, WaterSystem_Community_SubBasin_idSubBasin),
  CONSTRAINT fk_ReliefValve_WaterSystem1
    FOREIGN KEY (WaterSystem_idWaterSystem , WaterSystem_Community_idCommunity , WaterSystem_Community_SubBasin_idSubBasin)
    REFERENCES WaterSystem (idWaterSystem , Community_idCommunity , Community_SubBasin_idSubBasin)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
;

CREATE INDEX fk_ReliefValve_WaterSystem1_idx ON ReliefValve (WaterSystem_idWaterSystem ASC, WaterSystem_Community_idCommunity ASC, WaterSystem_Community_SubBasin_idSubBasin ASC);
