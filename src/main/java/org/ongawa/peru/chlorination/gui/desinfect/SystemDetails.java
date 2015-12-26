package org.ongawa.peru.chlorination.gui.desinfect;

import java.net.URL;
import java.util.ResourceBundle;

import org.ongawa.peru.chlorination.MainApp;
import org.ongawa.peru.chlorination.logic.SystemElement;
import org.ongawa.peru.chlorination.logic.elements.CubicReservoir;
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

    }

    /**
     * Create a new element
     */
    public void showAddElement() throws Exception {
        // Clear the editPane and set
        ObservableList<Node> children = this.editPane.getChildren();
        children.clear();

        Node childNode = this.loader.load(getClass().getResourceAsStream("/fxml/desinfect/SelectElementType.fxml"));
        children.add(childNode);
    }

    /**
     * Edit an existing element
     */
    public void showEditElement(SystemElement element) {
        // Clear the editPane and set
        ObservableList<Node> children = this.editPane.getChildren();

        // Get the selected item
        SystemElement selected = this.elementsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Clear the editPane and set with the new person.
            children.clear();
            try {
                // Use the appropriate view for the type.
                String selectedType = selected.getTypeName().getValue();
                if (selectedType.equals(Pipe.TYPE_NAME)) {
                    Node childNode = this.loader
                            .load(getClass().getResourceAsStream("/fxml/desinfect/EditElementPipe.fxml"));
                    children.add(childNode);
                    
                    // Get the fields by ID and set the values
                    ((TextField) this.editPane.lookup("#elementName")).setText(element.getName().getValue());
                    ((TextField) this.editPane.lookup("#elementDiameter"))
                            .setText(String.valueOf(((Pipe) element).getDiameter()));
                    ((TextField) this.editPane.lookup("#elementLenght"))
                            .setText(String.valueOf(((Pipe) element).getLength()));
                    ((TextField) this.editPane.lookup("#retentionTime")).setText(String.valueOf(Pipe.RETENTION_TIME));
                    ((TextField) this.editPane.lookup("#clConcetration"))
                            .setText(String.valueOf(((Pipe) element).getConcentration()));
                } else if (selectedType.equals(CubicReservoir.TYPE_NAME)) {
                    Node childNode = this.loader
                            .load(getClass().getResourceAsStream("/fxml/desinfect/SelectedReservoir.fxml"));
                    children.add(childNode);
                    
                    // TODO
                } else {
                    Node childNode = this.loader
                            .load(getClass().getResourceAsStream("/fxml/desinfect/SelectedCPR.fxml"));
                    children.add(childNode);
                    
                    // TODO
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    
    /**
     * Save the current displayed data
     */
    public void saveCurrentData() {
        // TODO: save the displayed data and clear the pane.
        String elementType = ((Label)this.editPane.lookup("#elementType")).getText();
        
        
        //clear
        this.editPane.getChildren().clear();
    }

    public void triggerNext() throws Exception {
        Stage stage = MainApp.getStage();

        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream("/fxml/waterProperties.fxml"));

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

        // Add data sources
        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeName());

        // Add listener for selected element to edit
        this.elementsTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showEditElement(newValue));
    }

}
