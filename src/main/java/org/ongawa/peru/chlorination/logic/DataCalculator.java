package org.ongawa.peru.chlorination.logic;

public class DataCalculator {
   
    public static final int M32L = 1000;
    public static final double PULGADA2M = 0.0254;
    public static final int CUCHARA2GR = 10;
    public static final int ML2GOTAS = 20;
    public static final int DIA2S = 24*60*60;
    public static final int VOL_COMP_MADRE_L =1;
    public static final int VOL_BALDE_L =10;
    public static final int CONC_COMP_MADRE = 10; // g/L =  1%
    public static final int FAMILIES_TO_INHABITANTS = 5;
    
    public static String getInhabitantsFromFamilies(String familiesCount) {
        int nFamilies = Integer.parseInt(familiesCount);
        int nInhabitants = nFamilies*FAMILIES_TO_INHABITANTS;
        return String.valueOf(nInhabitants);
    }
    /**
     *  Function getSolucionMadre to compute the weight (gr) of Cl
     *  compound to prepare the mother compound to measure CL demand by the Baldes method 
     *  input: % of the free Cl of the comercial Cl compound
     *  output: gr of Cl needed to prepare mother compound
    */
    /*public static String grClSolucionMadre(String concentracionCl) {
        int concCl= Integer.parseInt(concentracionCl); // % Cl Compound
        double grCl = CONC_COMP_MADRE * VOL_COMP_MADRE_L / (concCl * 10) ;
        return String.valueOf(grCl);
    }*/
    /**
     *  Function clDemand to compute the demand (gr/l) of comercial Ca(OCl2) 
     *  which gives a CLR of 0.5mg/l by the Baldes method 
     *  input: chosen quantity  (the Baldes test)
     *  	   gr CL used for the mother compound
     *  output: mgr/l of Cl 
    */
    public static String clDemand(String cantidadSolMadre, String grCl) {
    	double qCl = Double.parseDouble(grCl); // gr of Cl used to prepare mother compound
        double qSolMad= Double.parseDouble(cantidadSolMadre); // Result of the baldes test (l)
        double demCl=  qSolMad*qCl / VOL_BALDE_L *1000; // Cl Demand of the water (mgr/l)
        return String.valueOf(demCl);
    }
    /**
     *  Function chlorination (Cloro a dosificar  y caudal de goteo caudal) to compute the chlorination values 
     *  input: Cl demand mgr/l
     *  	   flow lps
     *  	   hours  of tipping per day 
     *  	   #days of recharging the ration tank
     *         % of the free Cl of the comercial Cl compound 
     *  output: mgr/l of Cl 
    */
    public static String[] chlorination(String caudal,String concentracionCl, String volTanque, String tRegarga, String hGoteoDia, String demCl) {
    	String[] res = new String [6];
    	double vTank = Double.parseDouble(volTanque);
    	double dC = Double.parseDouble(demCl); 
        double flNat= Double.parseDouble(caudal); 
        double hGoteo = Double.parseDouble(hGoteoDia);
        double dRecarga = Double.parseDouble(tRegarga); 
        int perCl = Integer.parseInt(concentracionCl);
        double cloroAdosificar=  dC * flNat * DIA2S * hGoteo * dRecarga / (perCl * 1000000 * 24) ; // kg en el tiempo de recarga
        res[0] = String.valueOf(cloroAdosificar*1000/dRecarga);// 1000 stands for g and dRecargar for day (g/day)
        res[1] = String.valueOf(cloroAdosificar);//  (kg/periodoRecarga)
        res[2] = String.valueOf(cloroAdosificar*30/dRecarga);// 30 stands for days in month and dRecargar for day (kg/month)
        double caudalGoteo = vTank/(dRecarga*hGoteo*60); // 60 stands for min (l/min)
        res[3] = String.valueOf(caudalGoteo);//l/min
        res[4] = String.valueOf(caudalGoteo*1000/60);//1000 stands for ml and 60 for sec (ml/s)
        res[5] = String.valueOf(caudalGoteo*1000*ML2GOTAS/60); //1000 stands for ml and 60 for sec (gotas/s)
        return res; 
    }
    /**
     * Function popFlow to compute the flow to chlorinate as a function of the population, default value
     * input: number of inhabitants
     *        daily variation coefficient
     *        number of litres per day per person
     *        percentage of loss
     */
    public static double popFlow (String numHab, String coefVar, String dotacion, String perdidas){
    	double nHab = Double.parseDouble(numHab);
    	double k1 = Double.parseDouble(coefVar); 
        double dot= Double.parseDouble(dotacion); 
        double perL = Double.parseDouble(perdidas);
        double res = nHab*k1*dot/(1-perL/100);
        return res;
    	
    }
    
    /**
     * Function volTanCaD to compute the minimum volume of the tank to keep the concentration of Cl in an accurate range
     * as a function of the climate
     * input  quantity of Cl to ration 
     *        climate concentration ppm: warm zones 10000 ppm cold zones 5000ppm
     * output vol in litres        
     */
    public static double volTanqueCaD (String climate, String cloroAdosificar){
    	
    	double tempConc;
    	if (climate.equals("Calido")||climate.equals("Templado"))
    	tempConc = 5000; // ppm mg/L
    	else 
    		tempConc = 10000;//ppm mg/L
    	double cAd = Double.parseDouble(cloroAdosificar);  //g/Trecarga
        
        double vt = cAd/tempConc*1000;//1000 stands for mg 
        return vt;// L
    	
    }
    
    
}
