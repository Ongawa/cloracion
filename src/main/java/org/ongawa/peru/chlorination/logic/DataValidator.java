package org.ongawa.peru.chlorination.logic;

public class DataValidator {
    
    
	public static String checkDota(double dot) {
        if(dot >300 || dot<0)
        return "[ERROR] Dotación: Número de litros por día que consume una persona.\n Debe de ser un valor entre 0 y 300 litros.";
        else 
        	return "";
    }
	public static String checkPrecClor(double soles) {
        if(soles > 50.00 || soles<0.01)
        return "[ERROR] Precio del kg de cloro en soles. Debe de ser un valor entre 0.01 y 50.00.";
        else 
        	return "";
    }
	public static String checkFami(int num) {
        if(num > 600 || num<1)
        return "[ERROR] Número de familias";
        else 
        	return "";
    }
	public static String checkCaud(double num) {
        if(num > 500.00 || num<0.01)
        return "[ERROR] Cuadal en litros por segundo ";
        else 
        	return "";
    }
	/*Function checkWaterQ(double turbidez, double pH)
	 * El agua ha de cumplir unos requisitos mínimos para ser considerada “clorable”.
	 * Los dos parámetros más importantes son la turbidez y el pH.
	 *  El agua NO debe ser clorada cuando:
	 * La turbidez es mayor a 5 NTU.
	 * El pH está fuera del rango: 6,5 < pH < 8,5. Este rango es el establecido por la OMS para aguas para consumo humano, pero tiene relación también con la eficacia de la cloración, tal y como se explica en el siguiente apartado.

	 */
	public static String checkWaterQ(double turbidez, double pH){
		if(turbidez > 5.00 )
	        return "[AVISO] El agua NO debe ser clorada: Turbidez mayor a 5 NTU ";
	        else if(pH<6.5 || pH> 8.5)
	        	return "[AVISO] El pH está fuera del rango: 6,5 < pH < 8,5. Este rango es el establecido por la OMS para aguas para consumo humano.\n A partir de pH=8 la cloración es mucho menos efectiva y necesita más tiempo de reacción o más cloro";
	        else
	        	return "";
	}
	public static String checkNumeElem(int num) {
        if(num > 500 || num<0)
        return "[ERROR] Numero de elementos";
        else 
        	return "";
    }
	public static String checkLado(double num) {
        if(num > 50.00 || num<0.01)
        return "[ERROR] Lado esperado entre 0 metros y 30 metros";
        else 
        	return "";
    }
	public static String checkAltura(double num) {
        if(num > 50.00 || num<0.01)
        return "[ERROR] Altura esperada entre 0 metros y 30 metros";
        else 
        	return "";
    }
	public static String checkDiametro(double num) {
        if(num > 20.00 || num<0.01)
        return "[ERROR] Diametro esperado entre 0\" y 10\"";
        else 
        	return "";
    }
	public static String checkLong(double num) {
        if(num > 25000.00 || num<0.01)
        return "[ERROR] Longitud esperada entre 0 metros y 20000 metros";
        else 
        	return "";
    }
	public static String checkConcSolu(double num) {
        if(num > 1500.00 || num<0.001)
        return "[ERROR] Concentracion de la solucion esperada entre 0 mg/L y 1000 mg/L";
        else 
        	return "";
    }
	public static String checkPure(double num) {
        if(num > 100.00 || num<0.001)
        return "[ERROR] Pureza del cloro esperada en porcentaje";
        else 
        	return "";
    }
	
	public static String checkTiemRecar(double num) {
        if(num > 60.00 || num<0.01)
        return "[ERROR] Tiempo de recarga esperado entre 1 día y 60 días.";
        else 
        	return "";
    }
	public static String checkPrecUnit(double num) {
        if(num > 5000.00 || num<0.01)
        return "[ERROR] Precio en soles por unidad.";
        else 
        	return "";
    }
	public static String checkReem(double num) {
        if(num > 100.00 || num<0.01)
        return "[ERROR] Número de reemplazos anuales.";
        else 
        	return "";
    }
	public static String checkHono(double num) {
        if(num > 10000.00 || num<0.01)
        return "[ERROR] Honorarios de operador.";
        else 
        	return "";
    }
	public static String checkHoraGote(double num) {
        if(num > 24.00 || num<0.01)
        return "[ERROR] Horas de goteo diario menor de 14 horas.";
        else 
        	return "";
    }
	
	
	
	
}