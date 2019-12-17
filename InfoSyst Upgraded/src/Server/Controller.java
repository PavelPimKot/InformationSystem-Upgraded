package Server;

import InformationClasses.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller - works with user, read and checks commands
 */
public class Controller implements EventListener {

    private static ServerSocket serverSocket;
    private static ExecutorService executeIt = Executors.newFixedThreadPool(5);
    private static ArrayList<Socket> clients;
    private static ArrayList<ObjectOutputStream> outputStreams;

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(1605);
            clients = new ArrayList<>();
            outputStreams = new ArrayList<>();
            Model.updateRuntimeDatabase();
            Model.setComparator(new Comparator<LibraryInfo>() {
                @Override
                public int compare(LibraryInfo o1, LibraryInfo o2) {
                    return o1.getBook().getTitle().compareTo(o2.getBook().getTitle());
                }
            });
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                clients.add(client);
                outputStreams.add(new ObjectOutputStream(client.getOutputStream()));
                executeIt.execute(new Mono(outputStreams.get(outputStreams.size() - 1), new ObjectInputStream(client.getInputStream())));
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void update(String eventType) {
        ObjectOutputStream outputStream = null;
        try {
            int i = 0;
            for (Socket client : clients) {
                if (client.isConnected()) {
                    outputStream = outputStreams.get(i);
                    i++;
                } else {
                    i--;
                    clients.remove(client);
                }
                outputStream.writeUTF(eventType);
                outputStream.flush();
            }
        }
        catch (IOException E) {
            E.printStackTrace();
        }
    }
}