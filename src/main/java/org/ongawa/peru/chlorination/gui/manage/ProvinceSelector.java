package org.ongawa.peru.chlorination.gui.manage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataCalculator;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.elements.Community;
import org.ongawa.peru.chlorination.persistence.elements.SubBasin;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * Controller for the selection of the system to work on.
 * 
 * @author Alberto Mardomingo
 *
 */
public class ProvinceSelector implements Initializable{
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(ProvinceSelector.class);
	}
    
    private IDataSource ds;
    private SubBasin selectedSubBasin;
    private Community selectedCommunity;
    private WaterSystem selectedWaterSystem;

    /**
     * River basin selector
     */
    @FXML
    private ComboBox basinCombo;

    /**
     * Town selector
     */
    @FXML
    private ComboBox townCombo;

    /**
     * Specific System
     */
    @FXML
    private ComboBox systemCombo;

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
        String basinSelected = this.basinCombo.valueProperty().getValue().toString();
        
        this.townCombo.getItems().clear();
        this.selectedSubBasin = this.ds.getSubBasin(basinSelected);
        if(this.selectedSubBasin != null){
        	List<String> communityNames = new ArrayList<String>();
        	List<Community> communities = this.ds.getCommunities(this.selectedSubBasin);
        	for(Community community : communities)
        		communityNames.add(community.getName());
        	this.townCombo.setItems(FXCollections.observableList(communityNames));
        }
    }

    public void townSelected() {
        //Get townSelected
        String townSelected = this.townCombo.valueProperty().getValue().toString();
        
        this.systemCombo.getItems().clear();
        this.selectedCommunity = this.ds.getCommunity(this.selectedSubBasin, townSelected);
        if(this.selectedCommunity != null){
        	List<String> waterSystemNames = new ArrayList<String>();
        	List<WaterSystem> waterSystems = this.ds.getWaterSystems(this.selectedCommunity);
        	for(WaterSystem waterSystem : waterSystems){
        		waterSystemNames.add(waterSystem.getName());
        	}
        	this.systemCombo.setItems(FXCollections.observableArrayList(waterSystemNames));
        }
    }

    public void systemSelected() {
    	if(this.systemCombo.valueProperty().getValue()!=null){
	        String waterSystemSelected = this.systemCombo.valueProperty().getValue().toString();
	        this.selectedWaterSystem = this.ds.getWaterSystem(this.selectedCommunity, waterSystemSelected);
	        if(this.selectedWaterSystem != null){
	        	this.familiesCount.setText(String.valueOf(this.selectedWaterSystem.getFamiliesNum()));
	        	int population = this.selectedWaterSystem.getPopulation();
	        	this.inhabintantsCount.setText((population>0)?String.valueOf(population):DataCalculator.getInhabitantsFromFamilies(String.valueOf(this.selectedWaterSystem.getFamiliesNum())));
	        }
    	}
    	else{
    		this.familiesCount.setText("");
    		this.inhabintantsCount.setText("");
    	}
    }
    public void triggerBack() {
        
        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());


        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/ChlorinationDetails.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        ChlorinationWindow chlorinationController = loader.<ChlorinationWindow>getController();
        
        // Get and set the values for the next scene
        chlorinationController.setBasin(this.basinCombo.valueProperty().toString());
        chlorinationController.setTown(this.townCombo.valueProperty().toString());
        chlorinationController.setSystemName(this.systemCombo.valueProperty().toString());
        chlorinationController.setFamiliesCount(Integer.parseInt(this.familiesCount.textProperty().get()));
        chlorinationController.setInhabitants(Integer.parseInt(this.inhabintantsCount.textProperty().get()));
        
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Initialize list of provinces here.
        try {
			this.ds = DataSourceFactory.getInstance().getDefaultDataSource();
			List<String> subBasinNames = new ArrayList<String>();
			List<SubBasin> subBasins = this.ds.getSubBasins();
			for(SubBasin subBasin : subBasins){
				subBasinNames.add(subBasin.getName());
			}
			this.basinCombo.getItems().clear();
	        this.basinCombo.setItems(FXCollections.observableArrayList(subBasinNames));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.warn(e.toString());
		}
    }

}
