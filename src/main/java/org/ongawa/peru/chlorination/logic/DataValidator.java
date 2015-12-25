package org.ongawa.peru.chlorination.logic;

public class DataValidator {
    
    
	public static String checkDota(double dot) {
        double dot = Integer.parseInt(dot);
        if(dot >300 || dot<0)
        return "[ERROR] Dotación: Número de litros por día que consume una persona.\n Debe de ser un valor entre 0 y 300 litros.";
        else 
        	return "";
    }
	public static String checkPrecClor(double soles) {
        double soles = Integer.parseInt(soles);
        if(soles > 50.00 || soles<0.01)
        return "[ERROR] Precio del kg de cloro en soles. Debe de ser un valor entre 0.01 y 50.00.";
        else 
        	return "";
    }
	public static String checkFami(int num) {
        double num = Integer.parseInt(num);
        if(num > 600 || num<1)
        return "[ERROR] Número de familias";
        else 
        	return "";
    }
	public static String checkCaud(double num) {
        double num = Integer.parseInt(num);
        if(num > 500.00 || num<0.01)
        return "[ERROR] Cuadal en litros por segundo ";
        else 
        	return "";
    }
}