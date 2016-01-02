--SUBBASIN
INSERT INTO SUBBASIN VALUES (1, 'Manzanayocc');

--COMMUNITY
INSERT INTO COMMUNITY VALUES (1, 1, 'Nueva Esperanza');
INSERT INTO COMMUNITY VALUES (2, 1, 'Manzanayocc');
INSERT INTO COMMUNITY VALUES (3, 1, 'Pampaspata');
INSERT INTO COMMUNITY VALUES (4, 1, 'Huaracco');
INSERT INTO COMMUNITY VALUES (5, 1, 'Mamachapampa');
INSERT INTO COMMUNITY VALUES (6, 1, 'Unión La Victoria');
INSERT INTO COMMUNITY VALUES (7, 1, 'Jatumpampa - Suso');
INSERT INTO COMMUNITY VALUES (8, 1, 'Chaupiyacu');
INSERT INTO COMMUNITY VALUES (9, 1, 'Rudio - Uchucancha');

--WATERSYSTEM
INSERT INTO WATERSYSTEM VALUES (1, 1, 1, 'Nueva Esperanza', 38, 190, 282, 2, 1, 10, 2536);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum) VALUES (2, 2, 1, 'Manzanayocc Loma', 37, 185, 275, 2, 1);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum) VALUES (3, 2, 1, 'Manzanayocc', 54, 270, 401, 2, 1);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (4, 3, 1, 'Sector alto', 47, 235, 349, 2, 1, 10);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (5, 3, 1, 'Sector central', 82, 410, 609, 2, 1, 15);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (6, 4, 1, 'Huaracco', 62, 310, 461, 2, 1, 10);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (7, 5, 1, 'Mamachapampa', 82, 410, 609, 2, 1, 10);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (8, 6, 1, 'Unión La Victoria', 50, 250, 371, 2, 1, 10);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (9, 7, 1, 'Jatumpampa - Suso', 35, 175, 260, 2, 1, 5);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (10, 8, 1, 'Sector Secceccocha', 19, 95, 141, 2, 1, 5);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (11, 8, 1, 'Sector Central', 42, 210, 312, 2, 1, 20);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (12, 9, 1, 'Rudio', 22, 110, 163, 2, 1, 3);
INSERT INTO WATERSYSTEM (idWaterSystem, Community_idCommunity, Community_SubBasin_idSubBasin, name, familiesNum, population, populationForecast, growingIndex, JASSNum, reservoirVolume) VALUES (13, 9, 1, 'Uchucancha', 14, 70, 104, 2, 1, 3);

--WATERSPTRING
INSERT INTO WATERSPRING VALUES (1, 'Paccaypata');
INSERT INTO WATERSPRING VALUES (2, 'Toccara');
INSERT INTO WATERSPRING VALUES (3, 'Manzanacucho');
INSERT INTO WATERSPRING VALUES (4, 'Yacutoccyacc');
INSERT INTO WATERSPRING VALUES (5, 'Wuaraccopata');
INSERT INTO WATERSPRING VALUES (6, 'Tablaccacca');
INSERT INTO WATERSPRING VALUES (7, 'Ccayccahuycco y Manantial Huaracco');
INSERT INTO WATERSPRING VALUES (8, 'Miski Yaku Waccta');
INSERT INTO WATERSPRING VALUES (9, 'Yacutoccyacc 2');
INSERT INTO WATERSPRING VALUES (10, 'Puquiocucho');
INSERT INTO WATERSPRING VALUES (11, 'Checcopuquio');
INSERT INTO WATERSPRING VALUES (12, 'Patahuaycco');
INSERT INTO WATERSPRING VALUES (13, 'Uchuhuaycco');
INSERT INTO WATERSPRING VALUES (14, 'Alalahuaicco');

--WATERSYSTEM_HAS_WATERSPRING
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (1, 1, 1, 1, 0.26);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING (WaterSystem_idWaterSystem, WaterSystem_Community_idCommunity, WaterSystem_Community_SubBasin_idSubBasin, WaterSpring_idWaterSpring) VALUES (2, 2, 1, 2);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (3, 2, 1, 3, 3.47);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (4, 3, 1, 4, 0.32);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (5, 3, 1, 5, 0.56);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (6, 4, 1, 6, 0.43);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (7, 5, 1, 7, 0.56);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (8, 6, 1, 8, 0.34);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (9, 7, 1, 9, 0.24);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (10, 8, 1, 10, 0.13);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (11, 8, 1, 11, 0.29);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (12, 9, 1, 12, 0.15);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (13, 9, 1, 13, 0.10);
INSERT INTO WATERSYSTEM_HAS_WATERSPRING VALUES (4, 3, 1, 14, 0.32);

--MEASURINGPOINT
INSERT INTO MEASURINGPOINT VALUES (1, 7, 5, 1, 7, 'Manantial 1');
INSERT INTO MEASURINGPOINT VALUES (2, 7, 5, 1, 7, 'Rebose de manantial 1');
INSERT INTO MEASURINGPOINT VALUES (3, 7, 5, 1, 7, 'RP6 Manantial 1');
INSERT INTO MEASURINGPOINT VALUES (4, 7, 5, 1, 7, 'RP6 Ccayccahuycco');
INSERT INTO MEASURINGPOINT VALUES (5, 4, 3, 1, 4, 'Llorón 1');
INSERT INTO MEASURINGPOINT VALUES (6, 4, 3, 1, 4, 'Llorón 2 y 3');
INSERT INTO MEASURINGPOINT VALUES (7, 4, 3, 1, 14, 'Alalahuaicco');
INSERT INTO MEASURINGPOINT VALUES (8, 12, 9, 1, 12, 'Llorón 1');
INSERT INTO MEASURINGPOINT VALUES (9, 12, 9, 1, 12, 'Llorón 2 y 3');
INSERT INTO MEASURINGPOINT VALUES (10, 2, 2, 1, 2, 'Llorón 1');
INSERT INTO MEASURINGPOINT VALUES (11, 2, 2, 1, 2, 'Llorón 2');
INSERT INTO MEASURINGPOINT VALUES (12, 2, 2, 1, 2, 'Llorón 3');

--MEASUREDFLOW
INSERT INTO MEASUREDFLOW VALUES ('2015-06-18 00:00:00', 1, 7, 5, 1, 7, 0.70, 'Hay dos manantiales. El Manantial 1 se encuentra en la zona de Huaracco y Ccayccahuycco debajo de Pampaspata. En la RP6, que se sitúa a unos 10 metros por encima del reservorio, se juntan los dos caudales. Nos encontramos que de los 0,7 l/s que manan del Manantial 1, sólo llegan a la RP6 0,01 l/s, razón desconocida.');
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-18 00:00:00', 2, 7, 5, 1, 7, 0.62);
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-18 00:00:00', 3, 7, 5, 1, 7, 0.01);
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-18 00:00:00', 4, 7, 5, 1, 7, 1.16);
INSERT INTO MEASUREDFLOW VALUES ('2015-06-18 00:00:00', 5, 4, 3, 1, 4, 0.12, 'Pampaspata Itchupata se abastece de dos manantiales. Medimos el caudal en Yacutoccyacc, a donde llega la tubería desde Alalahuaicco.');
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-18 00:00:00', 6, 4, 3, 1, 4, 0.76);
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-19 00:00:00', 7, 4, 3, 1, 14, 0.19);
INSERT INTO MEASUREDFLOW VALUES ('2015-06-19 00:00:00', 8, 12, 9, 1, 12, 0.46, 'Rudio se abastece de un único manantial.');
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-19 00:00:00', 9, 12, 9, 1, 12, 0.97);
INSERT INTO MEASUREDFLOW VALUES ('2015-06-19 00:00:00', 10, 2, 2, 1, 2, 0.31, 'Había gusanillos');
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-19 00:00:00', 11, 2, 2, 1, 2, 0.25);
INSERT INTO MEASUREDFLOW (date, MeasuringPoint_idMeasuringPoint, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_idWaterSystem, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_idCommunity, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSystem_Community_SubBasin_idSubBasin, MeasuringPoint_WaterSystem_has_WaterSpring_WaterSpring_idWaterSpring, flow) VALUES ('2015-06-19 00:00:00', 12, 2, 2, 1, 2, 0.25);
