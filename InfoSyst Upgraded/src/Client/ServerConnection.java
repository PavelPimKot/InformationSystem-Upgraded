package Client;


import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;


public class ServerConnection implements Runnable {

    private ObjectInputStream inputStream;
    private ViewController controller;

    ServerConnection(ObjectInputStream in, ViewController controller) {
        inputStream = in;
        this.controller = controller;
    }

    @Override
    public void run() {

        try {
            while (true) {
                if (inputStream.available() != 0) {
                    String serverAnswer = inputStream.readUTF();
                    switch (serverAnswer) {
                        case "ADD": {
                            controller.viewAdd(inputStream);
                            break;
                        }
                        case "DELETE": {
                            controller.viewDelete(inputStream);
                            break;
                        }
                        case "EXP": {

                            String info = inputStream.readUTF();
                            new Thread(() ->
                            {
                                Platform.runLater(() -> {
                                    controller.alertPain(info);
                                });

                            }).start();


                            break;
                        }
                        case "SEARCH": {
                            String info = inputStream.readUTF();
                            new Thread(() ->
                            {
                                Platform.runLater(() -> {


                                    try {
                                        controller.viewSearch(info);
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                });

                            }).start();
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

