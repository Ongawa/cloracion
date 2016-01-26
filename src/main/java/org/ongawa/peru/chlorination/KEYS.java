package org.ongawa.peru.chlorination;

/**
 * @author Kiko
 **/
public class KEYS {
	public static final String PROPERTIES_FILENAME = "application.properties";
	
	public static final String DATABASE_URL = "DATABASE_URL";
	public static final String DATABASE_USERNAME = "DATABASE_USERNAME";
	public static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";
	public static final String DATABASE_CREATION_SCRIPT = "DATABASE_CREATION_SCRIPT";
	
	public static final String DEFAULT_DATASOURCE = "DEFAULT_DATASOURCE";
	
	public static final String APP_FIRST_RUN = "APP_FIRST_RUN";
	public static final String RESOURCES_PATH = "RESOURCES_PATH";
	public static final String APPDATA_PATH = "APPDATA_PATH";
	public static final String BACKUP_DAYS_LAPSE = "BACKUP_DAYS_LAPSE";
	public static final String LAST_BACKUP_DATE = "LAST_BACKUP_DATE";
	public static final String DEFAULT_BACKUP = "DEFAULT_BACKUP";
	
	public static final String REPORT_BUNDLE_NAME = "REPORT_BUNDLE_NAME";
	public static final String REPORT_GENERIC_TITLE_FIRST_CHUNK = "report_generic_title_first_chunk";
	public static final String REPORT_MANAGEMENT_TITLE_CHUNK = "report_management_title_chunk";
	public static final String REPORT_DESIGN_TITLE_CHUNK = "report_design_title_chunk";
	public static final String REPORT_DESINFECTION_TITLE_CHUNK = "report_desinfection_title_chunk";
	public static final String REPORT_GENERIC_TITLE_LAST_CHUNK = "report_generic_title_last_chunk";
	public static final String REPORT_GENERIC_GENERATIONDATE = "report_generic_generationdate";
	public static final String REPORT_GENERIC_AUTHOR = "report_generic_auhtor";
	public static final String REPORT_GENERIC_SYSTEMDATA = "report_generic_systemdata";
	public static final String REPORT_GENERIC_SUBBASIN = "report_generic_subbasin";
	public static final String REPORT_GENERIC_COMMUNITY = "report_generic_community";
	public static final String REPORT_GENERIC_WATERSYSTEM = "report_generic_watersystem";
	public static final String REPORT_GENERIC_FAMILIESNUM = "report_generic_familiesnum";
	public static final String REPORT_GENERIC_POPULATION = "report_generic_population";
	public static final String REPORT_GENERIC_NATURALFLOW = "report_generic_naturalflow";
	public static final String REPORT_GENERIC_FLOWTOCHLORINE = "report_generic_flowtochlorine";
	public static final String REPORT_GENERIC_CHLORINETYPE = "report_generic_chlorinetype";
	public static final String REPORT_GENERIC_CHLORINEPURENESS = "report_generic_chlorinepureness";
	public static final String REPORT_GENERIC_TANKVOLUME = "report_generic_tankvolume";
	public static final String REPORT_GENERIC_RELOADTIME = "report_generic_reloadtime";
	public static final String REPORT_GENERIC_DRIPPINGPERDAY = "report_generic_drippingperday";
	public static final String REPORT_GENERIC_CHLORINEDEMAND = "report_generic_chlorinedemand";
	public static final String REPORT_GENERIC_CHLORINEPRICE = "report_generic_chlorineprice";
	public static final String REPORT_GENERIC_ENDOWMENT = "report_generic_endowment";
	public static final String REPORT_GENERIC_GROWINGINDEX = "report_generic_growingindex";
	public static final String REPORT_GENERIC_CURRENTPOPULATIONNEEDEDFLOW = "report_generic_currentpopulationneededflow";
	public static final String REPORT_GENERIC_FUTUREPOPULATIONNEEDEDFLOW = "report_generic_futurepopulationneededflow";
	public static final String REPORT_GENERIC_CLIMATE = "report_generic_climate";
	public static final String REPORT_MANAGEMENT_NOCHLORINECALCULATION = "report_management_nochlorinecalculation";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_TITLE = "report_design_watersystemdetails_title";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_NUM = "report_design_watersystemdetails_table_num";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_NAME = "report_design_watersystemdetails_table_name";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_ELEMENT = "report_design_watersystemdetails_table_element";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_CUBICRESERVOIR = "report_design_watersystemdetails_table_element_cubicreservoir";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_CATCHMENT = "report_design_watersysyemdetails_table_element_catchment";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_DISTRIBUTIONPIPE = "report_design_watersystemdetails_table_element_distributionpipe";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_CONDUCTIONPIPE = "report_design_watersystemdetails_table_element_conductionpipe";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_RELIEFVALVE = "report_design_watersystemdetails_table_element_reliefvalve";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_ELEMENTSNUM = "report_design_watersystemdetails_table_elementsnum";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_CHLORINEPERELEMENT = "report_design_watersystemdetails_table_chlorineperelement";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_SPOONSPERELEMENT = "report_design_watersystemdetails_table_spoonsperlement";
	public static final String REPORT_DESIGN_WATERSYSTEMDETAILS_RETENTIONTIME = "report_design_watersystemdetails_table_retentiontime";
	public static final String REPORT_DESIGN_NODESIGNDATA = "report_design_nodesigndata";
	public static final String REPORT_DESIGN_CHLORINETOTALAMOUNT_RESULTS = "report_design_chlorinetotalamount_results";
	public static final String REPORT_DESIGN_DESINFECTIONSNUMPERYEAR_RESULTS = "report_design_desinfectionsnumperyear_results";
	public static final String REPORT_DESIGN_FAMILYFEECALCULATION_TITLE = "report_design_familyfeecalculation_title";
	public static final String REPORT_DESIGN_FEECALCULATION_TABLE_HEADER = "report_design_feecalculation_table_header";
	public static final String REPORT_DESIGN_FEECALCULATION_INPUT_COLUMNS = "report_design_feecalculation_input_column";
	public static final String REPORT_DESIGN_FEECALCULATION_UNIT_COLUMN = "report_design_feecalculation_unit_colum";
	public static final String REPORT_DESIGN_FEECALCULATION_CHLORINEFORCHLORINATION_ROW = "report_design_feecalculation_chlorineforchlorination_row";
	public static final String REPORT_DESIGN_FEECALCULATION_CHLORINEFORDESINFECTION_ROW = "report_design_feecalculation_chlorinefordesinfection_row";
	public static final String REPORT_DESIGN_FEECALCULATION_SAPSPARES_ROW = "report_design_feecalculation_sapspares_row";
	public static final String REPORT_DESIGN_FEECALCULATION_JASSMANAGEMENT_ROW = "report_design_feecalculation_jassmanagement_row";
	public static final String REPORT_DESIGN_FEECALCULATION_OPERATORPAYMENT_ROW = "report_design_feecalculation_operatorpayment_row";
	public static final String REPORT_DESIGN_FEECALCULATION_TOTAL_ROW = "report_design_feecalculation_total_row";
	public static final String REPORT_DESIGN_FEECALCULATION_ONLYCHLORINE = "report_design_feecalculation_onlychlorine";
	public static final String REPORT_DESIGN_FEECALCULATION_FAMILYFEE = "report_design_feecalculation_familyfee";
	public static final String REPORT_DESIGN_WARNINGS_TITLE = "report_design_warnings_title";
	public static final String REPORT_DESIGN_WARNINGS_CONTENT = "report_design_warnings_content";
	public static final String REPORT_GENERIC_RESULTS = "report_generic_results";
	public static final String REPORT_RESULTS_MANAGEMENT_DOSE = "report_results_management_dose";
	public static final String REPORT_RESULTS_MANAGEMENT_DRIPPINGFLOW = "report_results_management_drippingflow";
	public static final String REPORT_RESULTS_MANAGEMENT_CHLORINATIONCOST = "report_results_management_chlorinationcost";
	public static final String REPORT_RESULTS_DESIGN_CHLORINEAMOUNTNEEDED = "report_results_design_chlorineamountneeded";
	public static final String REPORT_RESULTS_DESIGN_TANKVOLUME = "report_results_design_tankvolume";
	public static final String REPORT_RESULTS_GENERIC_WARNINGS_TITLE = "report_results_generic_warnings_title";
	public static final String REPORT_RESULTS_MANAGEMENT_WARNINGS = "report_results_management_warnings";
	public static final String REPORT_DESINFECTION_WATERSYSTEMDETAILS_TITLE = "report_desinfection_watersystemdetails_title";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_NUM = "report_desinfection_watersystem_table_num";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_NAME = "report_desinfection_watersystem_table_name";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT = "report_desinfection_watersystem_table_element";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_CUBICRESERVOIR = "report_desinfection_watersystem_table_element_cubicreservoir";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_CATCHMENT = "report_desinfection_watersystem_table_element_catchment";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_DISTRIBUTIONPIPE = "report_desinfection_watersystem_table_element_distributionpipe";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_CONDUCTIONPIPE = "report_desinfection_watersystem_table_element_conductionpipe";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENT_RELIEFVALVE = "report_desinfection_watersystem_table_element_reliefvalve";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_ELEMENTSNUM = "report_desinfection_watersystem_table_elementsnum";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_CHLORINEPERELEMENT = "report_desinfection_watersystem_table_chlorineperelement";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_SPOONSPERELEMENT = "report_desinfection_watersystem_table_spoonsperelement";
	public static final String REPORT_DESINFECTION_WATERSYSTEM_TABLE_RETENTIONTIME = "report_desinfection_watersystem_table_retentiontime";
	public static final String REPORT_DESINFECTION_RESULTS_TOTALCHLORINEAMOUNT = "report_desinfection_results_totalchlorineamount";
	public static final String REPORT_DESINFECTION_RESULTS_DESINFECTIONCOST = "report_desinfection_results_desinfectioncost";
	public static final String REPORT_DESINFECTION_WARNINGS_CONTENT = "report_desinfection_warnings_content";
}
