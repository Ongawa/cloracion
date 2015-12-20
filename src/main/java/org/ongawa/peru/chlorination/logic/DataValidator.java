package org.ongawa.peru.chlorination.logic;

public class DataValidator {
    
    public static String getInhabitantsFromFamilies(String familiesCount) {
        int nFamilies = Integer.parseInt(familiesCount);
        int nInhabitants = nFamilies*5;
        return String.valueOf(nInhabitants);
    }
}
