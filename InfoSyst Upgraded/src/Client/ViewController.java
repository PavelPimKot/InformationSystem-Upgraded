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
import java.net.Socket;
import java.util.ArrayList;


public class ViewController extends Application {

    private static Socket clientSocket;
    private static ObservableList<String> viewTableInfo;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        clientSocket = new Socket("localhost", 1600);
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        launch(args);
    }

    private void alertPain (String info){
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
        String index = inputStream.readUTF();
        if (index.contains("EXP")) {
           alertPain(index);
        } else {
            String serverAnswer = inputStream.readUTF();
            if (serverAnswer.contains("EXP")) {
                alertPain(serverAnswer);
            }
            else {
                viewTableInfo.add(Integer.parseInt(index), serverAnswer);
            }
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
        String serverAnswer = inputStream.readUTF();
        if (serverAnswer.contains("EXP")) {
            alertPain(serverAnswer);
        } else {

            StreamTokenizer parce = new StreamTokenizer(new StringReader(serverAnswer));
            int biggest = 0;
            int index;
            for (int i = 0; true; i++) {
                parce.nextToken();
                if (parce.ttype == StreamTokenizer.TT_EOF) {
                    break;
                }
                index = (int) parce.nval;
                if (index > biggest) {
                    if (i != 0) {
                        --index;
                    }
                    biggest = index;
                }
                viewTableInfo.remove(index);
            }


        }

    }

    @FXML
    void SearchPressed(ActionEvent event) throws IOException {
        outputStream.writeUTF("search " + templateSearchField.getText());
        outputStream.flush();
        String serverAnswer = inputStream.readUTF();
        if (serverAnswer.contains("EXP")) {
            alertPain(serverAnswer);
        } else {
            StreamTokenizer parce = new StreamTokenizer(new StringReader(serverAnswer));
            ObservableList<Pair<Integer, String>> searchResult = FXCollections.observableArrayList();
            for (int i = 0; true; i++) {
                parce.nextToken();
                if (parce.ttype == StreamTokenizer.TT_EOF) {
                    break;
                }

                searchResult.add(new Pair<Integer, String>((int) parce.nval, viewTableInfo.get((int) parce.nval)));
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SearchTable.fxml"));
            Stage searchStage = loader.load();
            SearchTableController controller = loader.getController();
            controller.setSearchInfo(searchResult);
            searchStage.initModality(Modality.APPLICATION_MODAL);
            searchStage.show();
        }
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
            viewTableInfo.remove(focusedIndex);
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
            String index = inputStream.readUTF();
            if (index.contains("EXP")) {
                alertPain(index);
            } else {
                String serverAnswer = inputStream.readUTF();
                if (serverAnswer.contains("EXP")) {
                    alertPain(serverAnswer);
                }
                else {
                    viewTableInfo.remove(focusedIndex);
                    viewTableInfo.add(Integer.parseInt(index), serverAnswer);
                }
            }
        }
    }


    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        ArrayList<Object> getted = (ArrayList<Object>) inputStream.readObject();
        viewTableInfo = FXCollections.observableArrayList();
        for (Object o : getted) {
            viewTableInfo.add(o.toString());
        }
        infoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        infoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        mainTable.setItems(viewTableInfo);
    }


}
