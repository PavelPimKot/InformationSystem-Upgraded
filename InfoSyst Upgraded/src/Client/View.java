package Client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class View extends Application {

    private ViewController controller;

    public ViewController getController() {
        return controller;
    }

    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            for (int i = 0; i < 2; ++i) {

                clientSocket = new Socket("localhost", 1605);
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ViewController.class.getResource("View.fxml"));
                stage = loader.load();
                controller = loader.getController();


                ArrayList<Object> getted = (ArrayList<Object>) inputStream.readObject();
                ObservableList<String> viewTableInfo = FXCollections.observableArrayList();
                for (Object o : getted) {
                    viewTableInfo.add(o.toString());
                }


                Thread thread2 = new Thread(new ServerConnection(inputStream, controller));


                controller.setViewTableInfo(viewTableInfo);
                controller.setOutputStream(outputStream);

                stage.show();
                thread2.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

