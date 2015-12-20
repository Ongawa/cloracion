package org.ongawa.peru.chlorination.logic;

public class DataValidator {
    
    public static final int FAMILIES_TO_INHABITANTS = 5;
    
    public static String getInhabitantsFromFamilies(String familiesCount) {
        int nFamilies = Integer.parseInt(familiesCount);
        int nInhabitants = nFamilies*FAMILIES_TO_INHABITANTS;
        return String.valueOf(nInhabitants);
    }
}
