package Client;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.*;


public class ViewController {

    private ObservableList<String> viewTableInfo;
    private ObjectOutputStream outputStream;

    void setViewTableInfo(ObservableList<String> viewTableInfo) {
        this.viewTableInfo = viewTableInfo;
        mainTable.setItems(viewTableInfo);
    }


    void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }


    void alertPain(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Exception");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }


    @FXML
    private TableColumn<String, String> infoColumn;

    @FXML
    private Stage mainStage;

    @FXML
    private TableView<String> mainTable;

    @FXML
    private TextField templateSearchField;

    @FXML
    private TextField addInfoField;

    @FXML
    private TextField deleteInfoField;


    void viewAdd(ObjectInputStream inputStream) throws IOException {
        String index = inputStream.readUTF();
        String info = inputStream.readUTF();
        viewTableInfo.add(Integer.parseInt(index), info);
    }


    @FXML
    void AddPressed(ActionEvent event) throws IOException {
        String input = addInfoField.getText();
        int blocCount = input.split(" +").length;
        switch (blocCount) {
            case 4: {
                outputStream.writeUTF("addbook " + addInfoField.getText());
                outputStream.flush();
                break;
            }
            case 5: {
                outputStream.writeUTF("addbookinst " + addInfoField.getText());
                outputStream.flush();
                break;
            }
            default: {
                alertPain(" Written text isn't LibraryInformation ");
            }
        }
    }


    void viewDelete(ObjectInputStream inputStream) throws IOException {
        String serverAnswer = inputStream.readUTF();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(serverAnswer));
        int biggest = 0;
        int index;
        for (int i = 0; true; i++) {
            tokenizer.nextToken();
            if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
                break;
            }
            index = (int) tokenizer.nval;
            if (index > biggest) {
                if (i != 0) {
                    --index;
                }
                biggest = index;
            }
            viewTableInfo.remove(index);
        }
    }

    @FXML
    void DeletePressed(ActionEvent event) throws IOException {
        if (deleteInfoField.getText().length() != 0) {
            outputStream.writeUTF("delete " + deleteInfoField.getText());
            outputStream.flush();
        } else {
            outputStream.writeUTF("delete " + mainTable.getSelectionModel().getFocusedIndex());
            outputStream.flush();
        }
    }


    void viewSearch(String serverAnswer) throws IOException {

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(serverAnswer));
        ObservableList<Pair<Integer, String>> searchResult = FXCollections.observableArrayList();
        for (int i = 0; true; i++) {
            tokenizer.nextToken();
            if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
                break;
            }
            searchResult.add(new Pair<Integer, String>((int) tokenizer.nval, viewTableInfo.get((int) tokenizer.nval)));
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewController.class.getResource("SearchTable.fxml"));
        Stage searchStage = loader.load();
        SearchTableController controller = loader.getController();
        controller.setSearchInfo(searchResult);
        searchStage.initModality(Modality.APPLICATION_MODAL);
        searchStage.show();
    }


    @FXML
    void SearchPressed(ActionEvent event) throws IOException {
        outputStream.writeUTF("search " + templateSearchField.getText());
        outputStream.flush();
    }

    @FXML
    void MainStageClose(WindowEvent event) throws IOException {
        outputStream.writeUTF("exit");
        outputStream.close();
        mainStage.close();
    }


    @FXML
    void columnEditCommit(TableColumn.CellEditEvent event) throws IOException {
        int focusedIndex = event.getTableView().getSelectionModel().getFocusedIndex();
        String input = event.getNewValue().toString();
        if (input.length() == 0) {
            outputStream.writeUTF("delete " + focusedIndex);
            outputStream.flush();
        } else {
            int blocCount = input.split(" +").length;
            switch (blocCount) {
                case 4: {
                    outputStream.writeUTF("setbook " + focusedIndex + " " + input);
                    outputStream.flush();
                    break;
                }
                case 5: {
                    outputStream.writeUTF("setbookinst " + focusedIndex + " " + input);
                    outputStream.flush();
                    break;
                }
                default: {

                }
            }
        }
    }


    @FXML
    void initialize() {
        infoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        infoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }


}
