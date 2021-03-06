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
    public static final double K1 = 1.3;//variation coef.
    public static final int YEARS_PROJECTION = 20; 
    
    public static final int COLD_CLIMATE_PPM=5000;
    public static final int WARN_CLIMATE_PPM=10000; 
    
    public static String getInhabitantsFromFamilies(String familiesCount) {
        int nFamilies = Integer.parseInt(familiesCount);
        int nInhabitants = nFamilies*FAMILIES_TO_INHABITANTS;
        return String.valueOf(nInhabitants);
    }
    /* 
     * Funtion getFutPopulation to compute future number of inhabintants
     * input (String growth Rate 
     * String inhabitants at the date )
     *  output String estimation of the future population
     * */
    public static String getFutPopulation(double crec , int inh0){
//    	double crec =  Integer.parseInt(growthRate)/100; // % to 0.0
//    	int inh0 = Integer.parseInt(inhabitants);
    	double inh1 = inh0 * Math.pow((1+crec/100), YEARS_PROJECTION);
    	return String.valueOf(inh1);
    	
    }
    /*
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
     *  		drops per min
    */
    public static double[] chlorination(String caudal,String concentracionCl, String volTanque, String tRegarga, String hGoteoDia, String demCl, String clPricePerKG) {
    	double[] res = new double[8];
    	double vTank = Double.parseDouble(volTanque);
    	double dC = Double.parseDouble(demCl); 
        double flNat= Double.parseDouble(caudal); 
        double hGoteo = Double.parseDouble(hGoteoDia);
        double dRecarga = Double.parseDouble(tRegarga); 
        double perCl = Double.parseDouble(concentracionCl)/100;
        double clPrice = Double.valueOf(clPricePerKG);
        double cloroAdosificar=  dC * flNat * DIA2S * hGoteo * dRecarga / (perCl  * 24 * 1000000) ; // kg en el tiempo de recarga
        res[0] = cloroAdosificar*1000/dRecarga;// 1000 stands for g and dRecargar for day (g/day)
        res[1] = cloroAdosificar;//  (kg/periodoRecarga)
        res[2] = cloroAdosificar*30.5/dRecarga;// 30 stands for days in month and dRecargar for day (kg/month)
        double caudalGoteo = vTank/(dRecarga*hGoteo*60); // 60 stands for min (l/min)
        res[3] = caudalGoteo;//l/min
        res[4] = caudalGoteo*1000;//1000 stands for ml (ml/min)
        res[5] = caudalGoteo*1000*ML2GOTAS; //1000 stands for ml  (gotas/min)
        
        // Prices
        res[6] = res[1] * clPrice;
        res[7] = res[2] * clPrice;
        return res; 
    }

    
    
    /**
     * Function volTanCaD to compute the minimum volume of the tank to keep the concentration of Cl in an accurate range
     * as a function of the climate
     * input  quantity of Cl to ration 
     *        climate concentration ppm: warm zones 10000 ppm cold zones 5000ppm
     * output vol in litres        
     */
    public static double volTanCaD (int climate, double cloroAdosificar){
    	
    	/*double tempConc;
    	//if (climate.equals("Calido")||climate.equals("Templado"))
    	tempConc = 5000; // ppm mg/L
    	//else 
    		tempConc = 10000;//ppm mg/L*/
    	//double cAd = Double.parseDouble(cloroAdosificar);  //g/Trecarga
        double vt = cloroAdosificar/climate*1000;//1000 stands for mg 
        return vt;// L
    	
    }
    
    /**
     * Function volTanTam to compute the volume of the tank as a function of its size
     * input  length (m)
     * 		  width (m)
     * 		  height (m)
     * output vol in litres        
     */
    public static double volTanTam (double length, double width, double height){
    	
    	//double l = Double.parseDouble(length);  //m
    	//double w = Double.parseDouble(width); //m
    	//double h = Double.parseDouble(height); //m
        
        double vt = length*width*height*1000;//1000 stands for m3 to liters 
        return vt;// L
    	
    }
    /*
    * Function volTub to compute the volume of the pipes as a function of its diameter 
    * input  diameter (")
    *        longitud m
    * output vol in litres        
    */
   public static double volTub (double diametro, double longitud){
   	
   	double r = diametro/2 * PULGADA2M;  //m
   	//double l = Double.parseDouble(longitud); //m
   	double area = (3.1416*r*r);
       double vt = longitud*area*1000;//1000 stands for m3 to liters 
       return vt;// L
   	
   }
    /**
     *  Function desinfection to compute the quantities for desinfection procedure 
     *  input: # units
     *  	   vol L
     *         concentration of the desinfectant compound  mg/L
     *  output: mgr/l of Cl 
    */
    public static double[] desinfection(int numEle, double vTank, double concDes, double concentracionCl) {
    	double[] res = new double[3];
    	//double vTank = Double.parseDouble(vol);
    	//double concDes = Double.parseDouble(concentracion); 
    	double concCl = concentracionCl/100;
        //int numEle = Integer.parseInt(units);
        
        double cucharasDesin=  vTank*concDes/concCl / (1000 * CUCHARA2GR); // cucharadas por elemento
        res[0] = cucharasDesin* CUCHARA2GR;// 1000 stands for kg 
        res[2] = cucharasDesin;//  
        res[1] = cucharasDesin* CUCHARA2GR*numEle;// 1000 stands for kg. Total kg of desinf for all elem
        //res[2] = String.valueOf(cucharasDesin*numEle);//  Total cucharas of desinf for all elem
        
        return res; 
    }
    /*
     * Function popFlow to compute the flow to chlorinate as a function of the population, default value
     * input: number of inhabitants
     *        daily variation coefficient
     *        number of litres per day per person
     *        percentage of loss
     */
    public static double caudalMin (double pob, double dot, String losses){
        // pob -> #pax
    	// dot -> litros*persona*dia
    	double l = Double.parseDouble(losses); // % of losses
        double c_min = K1*pob*dot/(1-l/100);//1000 stands for m3 to liters 
        return c_min/(24*60*60);// L/segundo
    	
    }
    /* Function gastosCl to compute the cost of Cl for chlorination and desinfection
    * input: price of the kg of Cl
    *        consumption of Cl per month for cholorination
    *        anual rate of desinfection
    *        consumption of Cl per desinfection
    *  output: cuota de soles al mes
    */
   public static double [] gastosCl (String price, String consumptionCl ,String desinfectionPerYear, double consumptionDesin, String familias){
	   double [] c_min = new double[2];
   	double solesCl = Double.parseDouble(price);  // soles/kg
   	double c_cl = Double.parseDouble(consumptionCl); // Consumo de cloro por operacion kg/mes
   	//double c_ds = Double.parseDouble(consumptionDesin); // Consumo de cloro por desinfeccion kg/operacion
   	int n_ds = Integer.parseInt(desinfectionPerYear); // Numero de desinfecciones anuales
   	double n_fam = Double.parseDouble(familias);
   	c_min[0] = solesCl*(c_cl + n_ds*consumptionDesin/12);//12 stands for months on a year
     c_min[1] = solesCl*(c_cl + n_ds*consumptionDesin/12)/n_fam;//12 stands for months on a year. Family fee 		   
       return c_min;// soles al mes
   	
   }
   /* Function cuotaFam to compute the fee per family of the system
   * input: gastos en cloro MES
   *        gastos en reparición ANUAL
   *        gastos de gestión ANUAL
   *        gastos en operario ANUAL
   *  output: cuota en soles al mes por familia
   */
  public static double[] cuotaFam (Double gastoCl, String reparacionAnual ,String gestionAnual , String operarioAnual, String familias){
  	double [] c_min = new double[2];
  	double rep = Double.parseDouble(reparacionAnual)/12;  // soles
  	double gest = Double.parseDouble(gestionAnual)/12; // 
  	double oper = Double.parseDouble(operarioAnual)/12; // 
  	double n_fam = Double.parseDouble(familias); //
    c_min[0] = (rep+gest+oper+gastoCl)/n_fam;//12 stands for months on a year
    c_min[1] = rep+gest+oper+gastoCl; //monthly expenses
      return c_min;// soles al mes
  	
  }
    
}
