package Server;

import InformationClasses.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Controller - works with user, read and checks commands
 */
public class Controller {

    private static ServerSocket serverSocket;
    private static Socket client;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;

    public static void main(String[] args) {

        try {
            String clientCommand;
            serverSocket = new ServerSocket(1600);
            client = serverSocket.accept();
            Model.updateRuntimeDatabase();
            inputStream = new ObjectInputStream(client.getInputStream());
            outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.writeObject(Model.getRuntimeDatabase());
            while (!serverSocket.isClosed()) {
                if (inputStream.available() != 0) {
                    clientCommand = inputStream.readUTF();
                    getCommand(clientCommand);
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * This method is used to get info from library by index from user;
     * <If written by user info is not an integer - library will print;
     * <message about incorrect input;
     *
     * @param input - input stream(Console);
     * @throws IOException-
     */

    private static void getInfo(StreamTokenizer input) throws IOException {
        input.nextToken();
        int position = (int) input.nval;
        if (input.sval != null) {
            //viewConnection.notify(" The information entered is not an index ");
        } else
            Model.getInfFromBase(position);
    }


    /**
     * This method is used to set object by Book to the library;
     *
     * @param input - input stream(Console);
     * @throws IOException-
     */

    private static void setBook(StreamTokenizer input) throws IOException {
        String authors, title;
        int publishingYear, pagesNumber;
        int position;
        input.nextToken();
        position = (int) input.nval;
        input.nextToken();
        if (input.sval == null) {
            authors = Double.toString(input.nval);
        } else
            authors = input.sval;
        input.nextToken();
        if (input.sval == null) {
            title = Double.toString(input.nval);
        } else
            title = input.sval;
        input.nextToken();
        publishingYear = (int) input.nval;
        input.nextToken();
        pagesNumber = (int) input.nval;
        try {
            Model.setInfInBase(position, new Book(authors, title, publishingYear, pagesNumber));
        }
        catch (BadFieldsException e) {
            outputStream.writeUTF("EXP " + e.getMessage());
        }
    }


    /**
     * This method is used to set object by  BookInstance to the library;
     * <@see 108>
     *
     * @param input - input stream(Console);
     * @throws IOException-
     */

    private static void setBookInstance(StreamTokenizer input) throws IOException {
        String authors, title;
        int publishingYear, pagesNumber;
        int position;
        boolean issued = false;
        input.nextToken();
        position = (int) input.nval;
        input.nextToken();
        if (input.sval == null) {
            authors = Double.toString(input.nval);
        } else
            authors = input.sval;
        input.nextToken();
        if (input.sval == null) {
            title = Double.toString(input.nval);
        } else
            title = input.sval;
        input.nextToken();
        publishingYear = (int) input.nval;
        input.nextToken();
        pagesNumber = (int) input.nval;
        input.nextToken();
        if (input.sval.equals("true")) {
            issued = true;
        }
        try {
            Model.setInfInBase(position, new BookInstance(new Book(authors, title, publishingYear, pagesNumber), issued));
        }
        catch (BadFieldsException e) {
            outputStream.writeUTF("EXP " + e.getMessage());
        }
    }


    /**
     * This method is used to  delete info from library;
     *
     * @param input - input stream(Console);
     * @throws IOException-
     * @see 89-90
     */

    private static void deleteInfo(StreamTokenizer input) throws IOException {
        input.nextToken();
        int position = (int) input.nval;
        if (input.sval != null) {
            // viewConnection.notify(" The information entered is not an index ");
        } else
            Model.deleteInfFromBase(position);
    }


    /**
     * This method is used to add Book to the library;
     * <@see 108>
     *
     * @param input - input stream(Console);
     * @throws IOException-
     */

    private static void addBook(StreamTokenizer input) throws IOException {
        String authors, title;
        int publishingYear, pagesNumber;
        input.nextToken();
        if (input.sval == null) {
            authors = Double.toString(input.nval);
        } else
            authors = input.sval;
        input.nextToken();
        if (input.sval == null) {
            title = Double.toString(input.nval);
        } else
            title = input.sval;
        input.nextToken();
        publishingYear = (int) input.nval;
        input.nextToken();
        pagesNumber = (int) input.nval;
        try {
            Model.addInfToBase(new Book(authors, title, publishingYear, pagesNumber));
        }
        catch (BadFieldsException e) {
            outputStream.writeUTF("EXP " + e.getMessage());
        }

    }


    /**
     * This method is used to add BookInstance to the library;
     * <@see 108>
     *
     * @param input - input stream(Console);
     * @throws IOException-
     */

    private static void addBookInstance(StreamTokenizer input) throws IOException {
        String authors, title;
        int publishingYear, pagesNumber;
        boolean issued = false;
        input.nextToken();
        if (input.sval == null) {
            authors = Double.toString(input.nval);
        } else
            authors = input.sval;
        input.nextToken();
        if (input.sval == null) {
            title = Double.toString(input.nval);
        } else
            title = input.sval;
        input.nextToken();
        publishingYear = (int) input.nval;
        input.nextToken();
        pagesNumber = (int) input.nval;
        input.nextToken();
        if (input.sval.equals("true")) {
            issued = true;
        }
        try {
            Model.addInfToBase(new BookInstance(new Book(authors, title, publishingYear, pagesNumber), issued));
        }
        catch (BadFieldsException e) {
            outputStream.writeUTF("EXP " + e.getMessage());
        }
    }


    /**
     * This method is used to add information to the library from entered filename and path;
     *
     * @param input - input stream(Console);
     * @throws IOException-
     */

    private static void addInfFromFile(StreamTokenizer input) throws IOException, ClassNotFoundException {
        String filename;
        input.nextToken();
        filename = input.sval;
        Model.addInformationFromFile(new File(filename));
    }


    /**
     * This method is used to search info in library using template from user;
     *
     * @param input - input stream(Console)
     * @throws IOException-
     */

    private static void templateSearch(StreamTokenizer input) throws IOException {
        String template;
        input.nextToken();
        template = input.sval;
        if (input.sval == null) {
            template = Integer.toString((int) input.nval);
        }
        Model.search(template);
    }


    /**
     * This method is used to get command from user and process information ;
     *
     * @param in - input stream(Console)
     */

    private static void getCommand(String in) throws IOException, ClassNotFoundException {

        StreamTokenizer input = new StreamTokenizer(new StringReader(in));
        input.nextToken();
        String comm = input.sval;
        if (comm != null) {
            switch (comm.toLowerCase()) {
                case ("get"): {
                    getInfo(input);
                    break;
                }
                case ("setbook"): {
                    setBook(input);
                    break;
                }
                case ("setbookinst"): {
                    setBookInstance(input);
                    break;
                }
                case ("delete"): {
                    deleteInfo(input);
                    break;
                }
                case ("clear"): {
                    Model.clear();
                    break;
                }
                case ("addbook"): {
                    addBook(input);
                    break;
                }
                case ("addbookinst"): {
                    addBookInstance(input);
                    break;
                }
                case ("addinffromfile"): {
                    addInfFromFile(input);
                    break;
                }
                case ("search"): {
                    templateSearch(input);
                    break;
                }
                case ("updateruntime"): {
                    Model.updateRuntimeDatabase();
                    break;
                }
                case ("update"): {
                    Model.updateDatabase();
                    break;
                }
                case ("exit"): {
                    Model.updateDatabase();
                }
                default: {
                    // viewConnection.notify(" The entered string is not a reference command ");
                }
            }
        } else {
            //viewConnection.notify(" The entered string is not a reference command ");
        }
    }

}