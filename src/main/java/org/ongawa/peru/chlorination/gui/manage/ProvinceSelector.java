package org.ongawa.peru.chlorination.gui.manage;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * 
 * Controller for the selection of the system to work on. 
 * 
 * @author Alberto Mardomingo
 *
 */
public class ProvinceSelector {
    
    /**
     * River basin selector 
     */
    @FXML
    private ComboBox<FXCollections> basinCombo; 
    
    /**
     * Town selector
     */
    @FXML
    private ComboBox<FXCollections> townCombo;
    
    /**
     * Specific System
     */
    @FXML
    private ComboBox<FXCollections> systemCombo;
    
    
    /**
     * Inhabitants count for the given town.
     * 
     */
    @FXML
    private TextField inhabintantsCount;
    
    /**
     * Families count for the given town.
     * 
     */
    @FXML
    private TextField familiesCount;
    
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button nextButton;
    
    
    
    public void basinSelected() {

    }
    
    public void townSelected() {
        
    }
    
    public void triggerBack() {
        
    }
    
    public void triggerNext() {
        System.out.println(nextButton.getText());
    }
    
}
