package org.ongawa.peru.chlorination.gui.desinfect;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.gui.ClAlert;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.DataValidator;
import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.logic.elements.Catchment;
import org.ongawa.peru.chlorination.logic.elements.ConductionPipe;
import org.ongawa.peru.chlorination.logic.elements.CubicReservoir;
import org.ongawa.peru.chlorination.logic.elements.DistributionPipe;
import org.ongawa.peru.chlorination.logic.elements.ReliefValve;
import org.ongawa.peru.chlorination.persistence.DataSourceFactory;
import org.ongawa.peru.chlorination.persistence.IDataSource;
import org.ongawa.peru.chlorination.persistence.db.DataSource;
import org.ongawa.peru.chlorination.persistence.elements.WaterSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SystemDetails implements Initializable {
	
	private static Logger log;
	static{
		log = LoggerFactory.getLogger(SystemDetails.class);
	}

    private ObservableList<SystemElement> elements;

    /**
     * The table with the elements
     */
    @FXML
    private TableView<SystemElement> elementsTable;

    /**
     * Column with the names
     */
    @FXML
    private TableColumn<SystemElement, String> nameColumn;

    /**
     * Column with the types
     */
    @FXML
    private TableColumn<SystemElement, String> typeColumn;

    /**
     * Loader tfor the fxml files
     */
    private FXMLLoader loader;

    /**
     * Pane with the op.
     */
    @FXML
    private AnchorPane editPane;

    private WaterSystem waterSystem;

    private SystemElement currentEditableElement;

    public void setWaterSystem(WaterSystem waterSystem) {
        this.waterSystem = waterSystem;
    }

    public void triggerBack() {
        // Add future
        Scene current =  MainApp.getStage().getScene();
        MainApp.pushFuture(this.getClass().getSimpleName(), current);
        

        Scene scene = MainApp.popHistory();
        if (scene != null)
            MainApp.getStage().setScene(scene);

    }

    /**
     * Create a new element
     */
    public void showAddElement() throws Exception {
        // Clear the editPane and set
        ObservableList<Node> children = this.editPane.getChildren();
        children.clear();

        Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/SelectElementType.fxml"));
        children.add(childNode);
        ((ComboBox<String>) childNode.lookup("#newElementCombo")).valueProperty()
                .addListener((observable, oldValue, newValue) -> showAddForElement(newValue));
    }

    /**
     * Add the New item menu
     */
    public void showAddForElement(String elementType) {
        AnchorPane elementPane = ((AnchorPane) this.editPane.lookup("#newElementPane"));
        ObservableList<Node> children = elementPane.getChildren();
        children.clear();

        // Show the appropriate view
        try {
            // Use the appropriate view for the type.

            if (elementType.equals(DistributionPipe.TYPE_NAME)) {
                // I have to use the static loader instead of the loader object,
                // to prevent loading the same file twice
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementDistributionPipe.fxml"));
                children.add(childNode);

                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(DistributionPipe.TYPE_NAME));
                ;
            } else if (elementType.equals(ConductionPipe.TYPE_NAME)) {
                // I have to use the static loader instead of the loader object,
                // to prevent loading the same file twice
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementConductionPipe.fxml"));
                children.add(childNode);

                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(ConductionPipe.TYPE_NAME));
                ;
            } else if (elementType.equals(CubicReservoir.TYPE_NAME)) {
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementReservoir.fxml"));
                children.add(childNode);
                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
                ;
            } else if (elementType.equals(Catchment.TYPE_NAME)) {
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementCatchment.fxml"));
                children.add(childNode);
                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(Catchment.TYPE_NAME));
                ;
            } else {
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementCPR.fxml"));
                children.add(childNode);
                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
                ;
            }
        } catch (Exception e) {
        	log.warn(e.toString());
        }
    }

    /**
     * Edit an existing element
     */
    public void showEditElement(SystemElement element) {
        // Clear the editPane and set
        ObservableList<Node> children = this.editPane.getChildren();
        children.clear();
        // Get the selected item
        SystemElement selected = this.elementsTable.getSelectionModel().getSelectedItem();
        this.currentEditableElement = selected;

        if (selected != null) {
            // Clear the editPane and set with the new person.
            children.clear();
            try {
                // Use the appropriate view for the type.
                String selectedType = selected.getTypeName().getValue();
                if (selectedType.equals(DistributionPipe.TYPE_NAME)) {
                    Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementDistributionPipe.fxml"));
                    children.add(childNode);

                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementDiameter"))
                            .setText(String.valueOf(((DistributionPipe) element).getDiameter()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((DistributionPipe) element).getLength()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(DistributionPipe.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((DistributionPipe) element).getConcentration()));
                    ((TextField) this.editPane.lookup("#cuantity")).setText(String.valueOf(element.getCount()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(DistributionPipe.TYPE_NAME));
                    ;
                }else if (selectedType.equals(ConductionPipe.TYPE_NAME)) {
                    Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementConductionPipe.fxml"));
                    children.add(childNode);

                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementDiameter"))
                            .setText(String.valueOf(((ConductionPipe) element).getDiameter()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((ConductionPipe) element).getLength()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(ConductionPipe.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((ConductionPipe) element).getConcentration()));
                    ((TextField) this.editPane.lookup("#cuantity")).setText(String.valueOf(element.getCount()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(ConductionPipe.TYPE_NAME));
                    ;
                } else if (selectedType.equals(CubicReservoir.TYPE_NAME)) {
                    Node childNode = FXMLLoader
                            .load(getClass().getResource("/fxml/desinfect/EditElementReservoir.fxml"));
                    children.add(childNode);
                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementWidth"))
                            .setText(String.valueOf(((CubicReservoir) element).getWidth()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((CubicReservoir) element).getLength()));
                    ((TextField) this.editPane.lookup("#elementHeight"))
                            .setText(String.valueOf(((CubicReservoir) element).getHeight()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(CubicReservoir.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((CubicReservoir) element).getConcentration()));
                    ((TextField) this.editPane.lookup("#cuantity")).setText(String.valueOf(element.getCount()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
                    ;
                }else if (selectedType.equals(Catchment.TYPE_NAME)) {
                    Node childNode = FXMLLoader
                            .load(getClass().getResource("/fxml/desinfect/EditElementCatchment.fxml"));
                    children.add(childNode);
                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementWidth"))
                            .setText(String.valueOf(((Catchment) element).getWidth()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((Catchment) element).getLength()));
                    ((TextField) this.editPane.lookup("#elementHeight"))
                            .setText(String.valueOf(((Catchment) element).getHeight()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(Catchment.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((Catchment) element).getConcentration()));
                    ((TextField) this.editPane.lookup("#cuantity")).setText(String.valueOf(element.getCount()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(Catchment.TYPE_NAME));
                    ;
                } else {
                    Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementCPR.fxml"));
                    children.add(childNode);
                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementWidth"))
                            .setText(String.valueOf(((ReliefValve) element).getWidth()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((ReliefValve) element).getLength()));
                    ((TextField) this.editPane.lookup("#elementHeight"))
                            .setText(String.valueOf(((ReliefValve) element).getHeigtht()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(ReliefValve.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((ReliefValve) element).getConcentration()));
                    ((TextField) this.editPane.lookup("#cuantity")).setText(String.valueOf(element.getCount()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
                    ;
                }
            } catch (Exception e) {
            	log.warn(e.toString());
            }
        }

    }

    private void showError(String message) {
        ClAlert alert = new ClAlert(message);
        try {
            alert.show();
        } catch (IOException e) {
        	log.warn(e.toString());
        }
    }

    /**
     * Save the current displayed data
     */
    public void saveCurrentData(String elementType) {
        // Edit the appropriate element
        if (elementType.equals(DistributionPipe.TYPE_NAME)) {
            // Save a pipe.
            // Get the fields by ID and set the values
            String name = ((TextField) this.editPane.lookup("#elementName")).getText();
            String diameterString = ((TextField) this.editPane.lookup("#elementDiameter")).getText();

            if (!DataValidator.isNumber(diameterString)) {
                // Show and error and finish
                showError("El diámetro debe ser un número");
                return;
            }
            double diameter = Double.valueOf(diameterString);

            String lengthString = ((TextField) this.editPane.lookup("#elementLength")).getText();
            if (!DataValidator.isNumber(lengthString)) {
                // Show and error and finish
                showError("La longitud debe ser un número");
                return;
            }
            double length = Double.valueOf(lengthString);
            String countString = ((TextField) this.editPane.lookup("#cuantity")).getText();
            if (!DataValidator.isNumber(countString)) {
                // Show and error and finish
                showError("La cantidad debe ser un número");
                return;
            }
            int count = Double.valueOf(countString).intValue();
            
            
            if (this.currentEditableElement != null){
                ((DistributionPipe)this.currentEditableElement).setElementName(name);
                ((DistributionPipe)this.currentEditableElement).setLength(length);
                ((DistributionPipe)this.currentEditableElement).setDiameter(diameter);
                ((DistributionPipe)this.currentEditableElement).setCount(count);
            } else {
                // Create the pipe and save it.
            	this.currentEditableElement = new DistributionPipe(diameter, length, this.waterSystem);
            	((DistributionPipe)this.currentEditableElement).setElementName(name);
            	((DistributionPipe)this.currentEditableElement).setCount(count);
                this.elements.add(this.currentEditableElement);
            }
            this.currentEditableElement.save();
        } else if (elementType.equals(ConductionPipe.TYPE_NAME)) {
            // Save a pipe.
            // Get the fields by ID and set the values
            String name = ((TextField) this.editPane.lookup("#elementName")).getText();
            String diameterString = ((TextField) this.editPane.lookup("#elementDiameter")).getText();

            if (!DataValidator.isNumber(diameterString)) {
                // Show and error and finish
                showError("El diámetro debe ser un número");
                return;
            }
            double diameter = Double.valueOf(diameterString);

            String lengthString = ((TextField) this.editPane.lookup("#elementLength")).getText();
            if (!DataValidator.isNumber(lengthString)) {
                // Show and error and finish
                showError("La longitud debe ser un número");
                return;
            }
            double length = Double.valueOf(lengthString);
            String countString = ((TextField) this.editPane.lookup("#cuantity")).getText();
            if (!DataValidator.isNumber(countString)) {
                // Show and error and finish
                showError("La cantidad debe ser un número");
                return;
            }
            int count = Double.valueOf(countString).intValue();
            
            
            if (this.currentEditableElement != null){
                ((ConductionPipe)this.currentEditableElement).setElementName(name);
                ((ConductionPipe)this.currentEditableElement).setLength(length);
                ((ConductionPipe)this.currentEditableElement).setDiameter(diameter);
                ((ConductionPipe)this.currentEditableElement).setCount(count);
            } else {
                // Create the pipe and save it.
            	this.currentEditableElement = new ConductionPipe(diameter, length, this.waterSystem);
            	((ConductionPipe)this.currentEditableElement).setElementName(name);
            	((ConductionPipe)this.currentEditableElement).setCount(count);
                this.elements.add(this.currentEditableElement);
            }
            this.currentEditableElement.save();
        } else if (elementType.equals(CubicReservoir.TYPE_NAME)) {
            String name = ((TextField) this.editPane.lookup("#elementName")).getText();

            String widthString = ((TextField) this.editPane.lookup("#elementWidth")).getText();
            if (!DataValidator.isNumber(widthString)) {
                // Show and error and finish
                showError("El ancho debe ser un número");
                return;
            }
            double width = Double.valueOf(widthString);
            String lengthString = ((TextField) this.editPane.lookup("#elementLength")).getText();
            if (!DataValidator.isNumber(lengthString)) {
                // Show and error and finish
                showError("La longitud debe ser un número");
                return;
            }
            double length = Double.valueOf(lengthString);
            String heightString = ((TextField) this.editPane.lookup("#elementHeight")).getText();
            if (!DataValidator.isNumber(heightString)) {
                // Show and error and finish
                showError("La altura debe ser un número");
                return;
            }
            double height = Double.valueOf(heightString);
            String countString = ((TextField) this.editPane.lookup("#cuantity")).getText();
            if (!DataValidator.isNumber(countString)) {
                // Show and error and finish
                showError("La cantidad debe ser un número");
                return;
            }
            int count = Double.valueOf(countString).intValue();
            
            if (this.currentEditableElement != null){
                ((CubicReservoir)this.currentEditableElement).setName(name);
                ((CubicReservoir)this.currentEditableElement).setLength(length);
                ((CubicReservoir)this.currentEditableElement).setHeight(height);
                ((CubicReservoir)this.currentEditableElement).setWidth(width);
                ((CubicReservoir)this.currentEditableElement).setCount(count);
            } else {
                // Create the pipe and save it.
                this.currentEditableElement = new CubicReservoir(name, length, width,height, this.waterSystem, count);
                this.elements.add(this.currentEditableElement);
            }
            this.currentEditableElement.save();

        } else if (elementType.equals(Catchment.TYPE_NAME)) {
            String name = ((TextField) this.editPane.lookup("#elementName")).getText();

            String widthString = ((TextField) this.editPane.lookup("#elementWidth")).getText();
            if (!DataValidator.isNumber(widthString)) {
                // Show and error and finish
                showError("El ancho debe ser un número");
                return;
            }
            double width = Double.valueOf(widthString);
            String lengthString = ((TextField) this.editPane.lookup("#elementLength")).getText();
            if (!DataValidator.isNumber(lengthString)) {
                // Show and error and finish
                showError("La longitud debe ser un número");
                return;
            }
            double length = Double.valueOf(lengthString);
            String heightString = ((TextField) this.editPane.lookup("#elementHeight")).getText();
            if (!DataValidator.isNumber(heightString)) {
                // Show and error and finish
                showError("La altura debe ser un número");
                return;
            }
            double height = Double.valueOf(heightString);
            String countString = ((TextField) this.editPane.lookup("#cuantity")).getText();
            if (!DataValidator.isNumber(countString)) {
                // Show and error and finish
                showError("La cantidad debe ser un número");
                return;
            }
            int count = Double.valueOf(countString).intValue();
            
            if (this.currentEditableElement != null){
                ((Catchment)this.currentEditableElement).setName(name);
                ((Catchment)this.currentEditableElement).setLength(length);
                ((Catchment)this.currentEditableElement).setHeight(height);
                ((Catchment)this.currentEditableElement).setWidth(width);
                ((Catchment)this.currentEditableElement).setCount(count);
            } else {
                // Create the pipe and save it.
            	this.currentEditableElement = new Catchment(width, length, height, this.waterSystem);
            	((Catchment)this.currentEditableElement).setElementName(name);
            	((Catchment)this.currentEditableElement).setCount(count);
                this.elements.add(this.currentEditableElement);
            }
            this.currentEditableElement.save();

        } else {
            // Relief Valve
            String name = ((TextField) this.editPane.lookup("#elementName")).getText();

            String widthString = ((TextField) this.editPane.lookup("#elementWidth")).getText();
            if (!DataValidator.isNumber(widthString)) {
                // Show and error and finish
                showError("El ancho debe ser un número");
                return;
            }
            double width = Double.valueOf(widthString);
            String lengthString = ((TextField) this.editPane.lookup("#elementLength")).getText();
            if (!DataValidator.isNumber(lengthString)) {
                // Show and error and finish
                showError("La longitud debe ser un número");
                return;
            }
            double length = Double.valueOf(lengthString);
            String heightString = ((TextField) this.editPane.lookup("#elementHeight")).getText();
            if (!DataValidator.isNumber(heightString)) {
                // Show and error and finish
                showError("La altura debe ser un número");
                return;
            }
            double height = Double.valueOf(heightString);
            
            String countString = ((TextField) this.editPane.lookup("#cuantity")).getText();
            if (!DataValidator.isNumber(countString)) {
                // Show and error and finish
                showError("La cantidad debe ser un número");
                return;
            }
            int count = Double.valueOf(countString).intValue();
            
            if (this.currentEditableElement != null){
                ((ReliefValve)this.currentEditableElement).setName(name);
                ((ReliefValve)this.currentEditableElement).setLength(length);
                ((ReliefValve)this.currentEditableElement).setHeigtht(height);
                ((ReliefValve)this.currentEditableElement).setWidth(width);
                ((ReliefValve)this.currentEditableElement).setCount(count);
            } else {
                // Create the pipe and save it.
                this.currentEditableElement = new ReliefValve(name, length, width,height, this.waterSystem, count);
                this.elements.add(this.currentEditableElement);
            }
            this.currentEditableElement.save();
        }
        // Clear the panel
        this.currentEditableElement = null;
        this.editPane.getChildren().clear();
    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        // TODO: Add cl purity textField
        
        // Set the data to pass ***before*** calling the class loader
        
        // TODO: Add a "look forward" history
        DataLoader.getDataLoader().setDesinfectResults(this.elements);
        
        Scene future = MainApp.popFuture(DesinfectionResults.class.getSimpleName());
        if (future != null) {
            stage.setScene(future);
            return;
        }
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/DesinfectionResults.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loader = new FXMLLoader();
        this.elements = FXCollections.observableArrayList();
        this.elementsTable.setItems(elements);

        DataLoader dataloader = DataLoader.getDataLoader();
        this.waterSystem = dataloader.getSelectedWaterSystem();

        try {
            IDataSource ds = DataSourceFactory.getInstance().getDefaultDataSource();
            for (org.ongawa.peru.chlorination.persistence.elements.DistributionPipe dbPipe : ds.getDistributionPipes(this.waterSystem)) {
                this.elements.add(new DistributionPipe(dbPipe));

            }
            for (org.ongawa.peru.chlorination.persistence.elements.ConductionPipe dbPipe : ds.getConductionPipes(this.waterSystem)) {
                this.elements.add(new ConductionPipe(dbPipe));

            }
            for (org.ongawa.peru.chlorination.persistence.elements.CubicReservoir dbReservoir : ds
                    .getCubicReservoirs(this.waterSystem)) {
                this.elements.add(new CubicReservoir(dbReservoir));
            }
            for (org.ongawa.peru.chlorination.persistence.elements.Catchment dbCatchment : ds
                    .getCatchments(this.waterSystem)) {
                this.elements.add(new Catchment(dbCatchment));
            }
            for (org.ongawa.peru.chlorination.persistence.elements.ReliefValve dbValve : ds
                    .getReliefValves(this.waterSystem)) {
                this.elements.add(new ReliefValve(dbValve));
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        	log.warn(e.toString());
        }
        
        // Add data sources
        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeName());

        // Add listener for selected element to edit
        this.elementsTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showEditElement(newValue));
    }

}
