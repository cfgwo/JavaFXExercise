/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxexercise;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.lucene.index.CorruptIndexException;

/**
 *
 * @author Administrator
 */
public class SearchMyFile extends Application {
   
            
    @Override
    public void start(Stage primaryStage)  throws CorruptIndexException, IOException {
         
    final Indexer indexer = new Indexer();
    final TextField txtIndexDir;
    ObservableList<MyDocument> resultDocs; // getAllCOA();
    final TableView resultTable = new TableView();
    Label resultText = new Label();
    final Map favorateMap = new HashMap();
    
    final String DEFAULT_INDEX_DIR = "c:/index_android";
    final String DEFAULT_DATA_DIR = "C:\\Users\\eweng\\Documents\\android_workspace";
    
        Scene scene = new Scene(new StackPane(), 1500, 900);
        primaryStage.setTitle("Search Moduel");
        
        // ----------------
        //   Grid
        //------------------
        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        
       
        Text header = new Text("Search");
        header.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        grid.add(header, 0, 0 );
        
        // ROW #1
        // search Row
        Label searchToken = new Label("Search String: ");
        final TextField txtSearchToken = new TextField("");
        Label lblSearchIIndexDir = new Label("Data Directory: ");
        final TextField txtSearchIndexDir = new TextField(DEFAULT_DATA_DIR);
        txtSearchIndexDir.setMinWidth(200);
        Button btnSearch = new Button("Search");
        
        grid.add(searchToken, 0, 1);
        grid.add(txtSearchToken, 1, 1);
        grid.add(lblSearchIIndexDir, 2, 1);
        grid.add(txtSearchIndexDir, 3, 1);
        grid.add(btnSearch, 4, 1);
        
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        sep.setPrefWidth(50);
        sep.setValignment(VPos.CENTER);
        grid.add(sep, 5, 1);
        
        // Index Row
        Label lblIndexDir = new Label("Index Directory: ");
        txtIndexDir = new TextField(DEFAULT_INDEX_DIR);
        txtIndexDir.setMinWidth(200);
        
        Label lblDataDir = new Label("Data Directory: ");
        final TextField txtDataDir = new TextField(DEFAULT_DATA_DIR);
        txtDataDir.setMinWidth(300);
        Button btnIndex = new Button("Index");
        btnIndex.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    indexer.index(txtIndexDir.getText(), txtDataDir.getText());
                } catch (CorruptIndexException ex) {
                } catch (IOException ex) {
                }
            }
        
        });
        
        grid.add(lblIndexDir, 6, 1);
        grid.add(txtIndexDir, 7, 1);
        grid.add(lblDataDir, 8, 1);     
        grid.add(txtDataDir, 9, 1);     
        grid.add(btnIndex, 10, 1);     
        
   
       
        // ----------------
        //   Table
        //------------------
        //resultTable = setupTable();
            TableColumn<MyDocument, Boolean> isFlaggedCol = new TableColumn<>();
            isFlaggedCol.setText("Flag");
            isFlaggedCol.setPrefWidth(50);
            //isFlaggedCol.setMinWidth(50);
            isFlaggedCol.setCellValueFactory(new PropertyValueFactory("isFlagged"));
            isFlaggedCol.setCellFactory(new Callback<TableColumn<MyDocument, Boolean>, TableCell<MyDocument, Boolean>>() {

                public TableCell<MyDocument, Boolean> call(TableColumn<MyDocument, Boolean> p) {
                    return new CheckBoxTableCell<>();
                }
            });
            //"Filename" column
            TableColumn filenameCol = new TableColumn();
            filenameCol.setText("Name");
            filenameCol.setMinWidth(260);
            filenameCol.setCellValueFactory(new PropertyValueFactory("filename"));
            
            //"Filepath" column
            TableColumn<MyDocument, String> pathCol = new TableColumn<>();
            pathCol.setText("Location");
            pathCol.setMinWidth(1024);
            pathCol.setCellValueFactory(new PropertyValueFactory("fullpath"));

            // Table
            resultTable.getColumns().addAll(isFlaggedCol, filenameCol, pathCol);
            resultTable.setMinHeight(740);
            resultTable.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<MyDocument>() {
                public void changed(ObservableValue<? extends MyDocument> ov, MyDocument old_val, MyDocument new_val) {
                        try {
                            StringProperty fullpath = new_val.fullpathProperty();
                            File file = new File(fullpath.getValue());
                            java.awt.Desktop.getDesktop().open(file);     
                        } catch (IOException e) {
                        }
                        System.out.println("new_val"+new_val);
                }
        });
        // ----------------
        //   Table Buttons
        //------------------
        HBox buttonBox = new HBox(5);
        buttonBox.setSpacing(10);
        Button flagBtn = new Button("Add To Favorate");
        HBox.setHgrow(flagBtn, Priority.ALWAYS);
        buttonBox.getChildren().addAll(flagBtn);
        
        flagBtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
               // addToFavorite(txtSearchToken.getText());
            }
        });
        
        // ----------------
        //   VBox
        //------------------
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15, 15, 15, 15));
    
        //final Text resultText = new Text();
        grid.add(resultText, 1, 1, 2, 1);
        btnSearch.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                String searchToken = txtSearchToken.getText();
//                if(resultTable.getItems()!=null){
//                   resultTable.
//                }
                ObservableList<MyDocument> resultDocs  = search(searchToken, txtIndexDir.getText());
                resultTable.setItems(resultDocs);
            }
        });
        
        vbox.getChildren().addAll(grid, resultTable, buttonBox);
        ((StackPane) scene.getRoot()).getChildren().addAll(vbox);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        // Load default list
        //search("");
    }
        /** 
         *  Search index folder based on the searchToken entered
         * @param searchToken 
         */
       ObservableList<MyDocument> search(String searchToken, String path){
            // ----------------
            //   VBox
            //------------------
            //To change body of generated methods, choose Tools | Templates.
                //(ObservableList) resultDocs.removeALl(resultDocs);
                ObservableList<MyDocument> resultDocs = SimpleSearcher.search(path, searchToken);
                return resultDocs;
        }
         
        private TableView setupTable() {
            
            TableColumn<MyDocument, Boolean> isFlaggedCol = new TableColumn<>();
            isFlaggedCol.setText("Flag");
            isFlaggedCol.setPrefWidth(50);
            //isFlaggedCol.setMinWidth(50);
            isFlaggedCol.setCellValueFactory(new PropertyValueFactory("isFlagged"));
            isFlaggedCol.setCellFactory(new Callback<TableColumn<MyDocument, Boolean>, TableCell<MyDocument, Boolean>>() {

                public TableCell<MyDocument, Boolean> call(TableColumn<MyDocument, Boolean> p) {
                    return new CheckBoxTableCell<>();
                }
            });
            //"Filename" column
            TableColumn filenameCol = new TableColumn();
            filenameCol.setText("Name");
            filenameCol.setMinWidth(260);
            filenameCol.setCellValueFactory(new PropertyValueFactory("filename"));
            
            //"Filepath" column
            TableColumn<MyDocument, String> pathCol = new TableColumn<>();
            pathCol.setText("Location");
            pathCol.setMinWidth(1024);
            pathCol.setCellValueFactory(new PropertyValueFactory("fullpath"));


            TableView<MyDocument> tableView = new TableView<>();
           // tableView.setEditable(true);
            tableView.getColumns().addAll(isFlaggedCol, filenameCol, pathCol);
            
            tableView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<MyDocument>() {
                public void changed(ObservableValue<? extends MyDocument> ov, MyDocument old_val, MyDocument new_val) {
                        try {
                            StringProperty fullpath = new_val.fullpathProperty();
                            File file = new File(fullpath.getValue());
                            java.awt.Desktop.getDesktop().open(file);     
                        } catch (IOException e) {
                        }
                        System.out.println("new_val"+new_val);
                }
        });
        return tableView;
    }
    //CheckBoxTableCell for creating a CheckBox in a table cell
    private static class CheckBoxTableCell<S, T> extends TableCell<S, T> {
        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public CheckBoxTableCell() {
            this.checkBox = new CheckBox();
            this.checkBox.setAlignment(Pos.CENTER);

            setAlignment(Pos.CENTER);
            setGraphic(checkBox);
        } 
        
        @Override public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(checkBox);
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());
                if (ov instanceof BooleanProperty) {
                    checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
                }
            }
        }
    }
   
    private Map<String, ? extends List<MyDocument>> addToFavorite(String searchString){
        ObservableList<MyDocument> list = FXCollections.observableArrayList();
//        for (MyDocument item : resultDocs){
//            if (item.isFlaggedProperty().getValue() == true){
//                list.add(item);
//            }
//        }
        final Map favorateMap = new HashMap();
        favorateMap.put(searchString, list);
        // loop through table's data
        return favorateMap;
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}