package Server;

import InformationClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;



/**
 * Model - works with data, performs writing \ reading \ deleting \ changing data in\to container
 */
class Model {

    private static EventManager controllerConnection ;

    public static void setControllerConnection(EventManager controllerConnection) {
        Model.controllerConnection = controllerConnection;
    }

    /**
     * Data is stored in serialized form;
     * DIRECTORY  И DATABASE -responsible for storing data in a text file;
     * runtimeDatabase - Data is read from DATABASE in before start , and written into DATABASE before end;
     * <p>
     * As a database while the Library is running used class from java.util -
     * ArrayList;
     */
    private static final File DIRECTORY = new File("C:\\Users\\HP\\Documents\\GitHub\\InformationSystem-Upgraded\\InfoSyst Upgraded\\src");
    private static final File DATABASE = new File(DIRECTORY, "Database.txt");
    private static ArrayList<LibraryInfo> runtimeDatabase = new ArrayList<>();


    public static ArrayList<LibraryInfo> getRuntimeDatabase() {
        return runtimeDatabase;
    }

    /**
     * This method is used to print Library;
     */
    static void showDatabase() {
        if (runtimeDatabase.size() == 0) {
            controllerConnection.notify(" Library is empty ");
        }
        for (Object object : runtimeDatabase) {
            controllerConnection.notify(object.toString());
        }
    }


    /**
     * This method is used to compare index with runtimeDatabase.size;
     *
     * @param index - checked index;
     * @return - true if index < size&&>0 ,else false;
     */

    private static boolean isIndexInRange(int index) {
        if (index > runtimeDatabase.size() || index <= 0) {
            controllerConnection.notify(" The index goes beyond the boundaries of the library ");
            return false;
        }
        return true;
    }


    /**
     * This method is used to add Information from another file;
     *
     * @param infBase- file with info that should be added;
     * @throws IOException-
     * @throws ClassNotFoundException-
     */

    static void addInformationFromFile(File infBase) throws IOException, ClassNotFoundException {
        if (infBase.length() != 0) {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(infBase));
            try {
                while (true) {
                    Object inf = input.readObject();
                    if (inf instanceof LibraryInfo ) {
                        addInfToBase((LibraryInfo)inf);
                    }
                }
            }
            catch (IOException e) {
                controllerConnection.notify(" File read successfully ");
            }
            input.close();
        }
        else{
            controllerConnection.notify(" Invalid filename ");
        }
    }


    /**
     * Template Search
     *
     * @param template-
     */

    static void search(String template) {
        controllerConnection.notify(" Searching results:: ");
        for (int i = 0; i < runtimeDatabase.size(); ++i) {
            if (runtimeDatabase.get(i).toString().contains(template)) {
                getInfFromBase(i);
            }
        }
    }


    /**
     * This method is used to get information from Database before
     * starting the library program;
     *
     * @throws IOException-
     * @throws ClassNotFoundException-
     */

    static void updateRuntimeDatabase() throws IOException, ClassNotFoundException {
        if (DATABASE.length() != 0) {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(DATABASE));
            runtimeDatabase = (ArrayList<LibraryInfo>) input.readObject();
            input.close();
        }
    }


    /**
     * This method is used to add Info in library;
     *
     * @param book- объект который нужно добавить в базу данных
     */

    static void addInfToBase(LibraryInfo book) {
        if (!runtimeDatabase.contains(book)) {
            runtimeDatabase.add(book);
            controllerConnection.notify(" Data added successfully ");
        } else {
            controllerConnection.notify(" Such data already exists, object not added ");
        }
    }


    /**
     * This method is used to get Object from library;
     *
     * @param index- index of Object that should be returned;
     */

    static void getInfFromBase(int index) {
        if (isIndexInRange(index))
            controllerConnection.notify(runtimeDatabase.get(index).toString());
    }


    /**
     * This method is used to delete information from library;
     *
     * @param index- index of Object that should be deleted;
     */

    static void deleteInfFromBase(int index) {
        if (isIndexInRange(index)) {
            if (runtimeDatabase.get(index) instanceof BookInstance) {
                BookInstance toDelete = (BookInstance) runtimeDatabase.get(index);
                runtimeDatabase.removeIf(i -> (toDelete.getBook().equals(i)));
                runtimeDatabase.remove(toDelete);
            } else {
                runtimeDatabase.remove(index);
            }
            controllerConnection.notify(" Data deleted successfully ");
        }
    }


    /**
     * This method is used to clear library(delete all elements);
     */
    static void clear() {
        controllerConnection.notify(" All data is deleted ");
        runtimeDatabase.clear();
    }


    /**
     * This method is used to set info in library;
     *
     * @param index-  index of Object in library which should be changed;
     * @param newInf- change by object;
     */

    static void setInfInBase(int index, LibraryInfo newInf) {
        if (!runtimeDatabase.contains(newInf) && isIndexInRange(index)) {
            runtimeDatabase.set(index, newInf);
            controllerConnection.notify(" Data changed successfully ");
        } else {
            controllerConnection.notify(" Such data already exists, object not changed ");
        }
    }


    /**
     * This method is used to update info in Database , call him before exit the library;
     */

    static void updateDatabase()  {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(DATABASE));
            output.writeObject(runtimeDatabase);
            output.close();
        }catch (IOException e){
            controllerConnection.notify(" Update Database Error ");
        }
    }


}
