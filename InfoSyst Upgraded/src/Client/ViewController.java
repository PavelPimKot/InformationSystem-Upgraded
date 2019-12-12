package Client;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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


    /*
    сообщения о какой либо ошибке на сервере принимаем как строку начинающуся с "EXP"
    сообщение об ошибке вывести в отдельном окне
     */

    @FXML
    void AddPressed(ActionEvent event) throws IOException {
        /*
        отправить запрос на сервев - ответ идекс куда нужно добавить нужный нам объект
         */
        String input = addInfoField.getText();
        int blocCount = input.split(" +").length;
        switch (blocCount) {
            case 4: {
                outputStream.writeUTF("addbook " + addInfoField.getText());
                break;
            }
            case 5: {
                outputStream.writeUTF("addbookinst " + addInfoField.getText());
                break;
            }
            default: {

            }
        }
        viewTableInfo.add(addInfoField.getText());
    }

    @FXML
    void DeletePressed(ActionEvent event) throws IOException {
        /*
        удаление по выбранной строке + удаление по заданному шаблону , при удалении по индексу
        просто удаляем из таблицы + запрос на модель для удаления
        если шаблонный - тоже самое только использую поиск
         */
        if (deleteInfoField.getText().length() != 0) {
            outputStream.writeUTF("delete " + addInfoField.getText());
        } else {
            outputStream.writeUTF("delete " + mainTable.getSelectionModel().getFocusedIndex());
        }

    }

    @FXML
    void SearchPressed(ActionEvent event) throws IOException {
        outputStream.writeUTF("search " + templateSearchField.getText());
        /*
        получить от сервера список индексов подходящих под нужный запрос , вывести их в отдельном окне
         */
    }

    @FXML
    void MainStageClose(WindowEvent event) throws IOException {
        outputStream.writeUTF("exit ");
        /*
        сервер увидев данное сообщение тоже завершает свою работу
         */
        mainStage.close();
    }

    @FXML
    void columnEditCommit(TableColumn.CellEditEvent event) throws IOException {
        int index = event.getTableView().getSelectionModel().getFocusedIndex();
        String  input = event.getNewValue().toString();
        int blocCount = input.split(" +").length;
        switch (blocCount) {
            case 4: {
                outputStream.writeUTF("setbook " + input);
                break;
            }
            case 5: {
                outputStream.writeUTF("setbookinst " + input);
                break;
            }
            default: {

            }
        }
        viewTableInfo.set(index, input);
    }



    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        ArrayList<Object> getted = (ArrayList<Object>) inputStream.readObject();
        viewTableInfo = FXCollections.observableArrayList();
        for(  Object o :getted){
            viewTableInfo.add(o.toString());
        }
        infoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()));
        infoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        mainTable.setItems(viewTableInfo);
    }
}
