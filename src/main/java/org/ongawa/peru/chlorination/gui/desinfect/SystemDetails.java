package org.ongawa.peru.chlorination.gui.desinfect;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.DataLoader;
import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.logic.elements.CubicReservoir;
import org.ongawa.peru.chlorination.logic.elements.ReliefValve;
import org.ongawa.peru.chlorination.logic.elements.Pipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SystemDetails implements Initializable {

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

    public void triggerBack() {
        
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
        ((ComboBox<String>) childNode.lookup("#newElementCombo")).valueProperty().addListener((observable, oldValue, newValue) -> showAddForElement(newValue));
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
            
            if (elementType.equals(Pipe.TYPE_NAME)) {
                // I have to use the static loader instead of the loader object, to prevent loading the same file twice
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementPipe.fxml"));
                children.add(childNode);

                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(Pipe.TYPE_NAME));
                ;
            } else if (elementType.equals(CubicReservoir.TYPE_NAME)) {
                Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementReservoir.fxml"));
                children.add(childNode);
                // Add listener for click on the button.
                ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                        (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
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
            e.printStackTrace();
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
        if (selected != null) {
            // Clear the editPane and set with the new person.
            children.clear();
            try {
                // Use the appropriate view for the type.
                String selectedType = selected.getTypeName().getValue();
                if (selectedType.equals(Pipe.TYPE_NAME)) {
                    Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementPipe.fxml"));
                    children.add(childNode);

                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementDiameter"))
                            .setText(String.valueOf(((Pipe) element).getDiameter()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((Pipe) element).getLength()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(Pipe.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((Pipe) element).getConcentration()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(Pipe.TYPE_NAME));
                    ;
                } else if (selectedType.equals(CubicReservoir.TYPE_NAME)) {
                    Node childNode = FXMLLoader.load(getClass().getResource("/fxml/desinfect/EditElementReservoir.fxml"));
                    children.add(childNode);
                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementWidth"))
                            .setText(String.valueOf(((CubicReservoir) element).getWidth()));
                    ((TextField) this.editPane.lookup("#elementLength"))
                            .setText(String.valueOf(((CubicReservoir) element).getLength()));
                    ((TextField) this.editPane.lookup("#elementHeight"))
                            .setText(String.valueOf(((CubicReservoir) element).getHeigtht()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(Pipe.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((CubicReservoir) element).getConcentration()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
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
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(Pipe.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((ReliefValve) element).getConcentration()));
                    // Add listener for click on the button.
                    ((Button) this.editPane.lookup("#saveData")).addEventFilter(MouseEvent.MOUSE_CLICKED,
                            (event) -> saveCurrentData(CubicReservoir.TYPE_NAME));
                    ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Save the current displayed data
     */
    public void saveCurrentData(String elementType) {
        // TODO: save the displayed data and clear the pane.

        // clear
        this.editPane.getChildren().clear();
    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();
        MainApp.pushHistory(stage.getScene());

        // Set the data to pass ***before*** calling the class loader
        DataLoader.getDataLoader().setDesinfectResults(this.elements);
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/DesinfectionResults.fxml"));

        Scene scene = new Scene(rootNode, stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add("/styles/styles.css");
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loader = new FXMLLoader();
        // TODO Get system info from DataLoader
        this.elements = FXCollections.observableArrayList();
        this.elementsTable.setItems(elements);
        elements.add(new Pipe("Tubería de prueba", 200, 13.5));
        elements.add(new ReliefValve("CPR de prueba", 100, 200, 300));
        elements.add(new CubicReservoir("Reservorio de prueba", 100, 200, 300));

        // Add data sources
        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeName());

        // Add listener for selected element to edit
        this.elementsTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showEditElement(newValue));
    }

}
