package MVS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * View - console data output;
 */
class View implements EventListener {

    private EventManager controllerConnection = new EventManager(new Controller( new EventManager(this)));


    /**
     * This method is used to start work with library program, prints Library commands,
     * info about data in library consist of;
     */

    void startLibrary() {
        try {
            View.getCommandList();
            controllerConnection.notify("UpdateRuntime");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String info;
           do{
               info = in.readLine();
              controllerConnection.notify(info);
           }while(!info.toLowerCase().equals("exit"));
        }
        catch (IOException e) {
            update(" Invalid program end, data is saved in Database ");
            controllerConnection.notify("Update");
        }
    }



    /**
     * Library Command List::
     * Get - get information from library ;
     * SetBook - Change info in library by Book from user;
     * SetBookInst - Change info in library by BookInstance from user;
     * AddBook - Add Book to the library;
     * AddBookInstance - Add BookInstance to the library;
     * Delete - Delete info from library;
     * Clear - Delete all info from library;
     * AddInfFromFile - Add Info to the library from another file;
     * Search - template Search in library;
     * Show - View Library to the screen;
     * Exit - Exit from library program;
     */


    private static final String[] Commands =
            {"Get", "SetBook", "SetBookInst", "AddBook",
                    "AddBookInst", "Delete", "Clear", "AddInfFromFile",
                    "Search", "Show", "Exit"};


    /**
     * This method is used to print command list;
     */


    private static void getCommandList() {
        System.out.println();
        System.out.println(" Library :: ");
        System.out.println();
        System.out.println("Contents: Book (authors, title, year of publication, number of pages)," +
                " Book Instance (inventory number, Book, issued / not) ");
        System.out.println();
        System.out.println("Command List ::");
        System.out.println();
        System.out.println(Commands[0] + ": Retrieving Index Information ");
        System.out.println();
        System.out.println(Commands[1] + ": Replacing an object in a library (by index) with a book ");
        System.out.println();
        System.out.println(Commands[2] + ": Replacing an object in a library (by index) Book instance ");
        System.out.println();
        System.out.println(Commands[3] + ": Adding a book to the library ");
        System.out.println();
        System.out.println(Commands[4] + ": Adding a Book Instance to the library ");
        System.out.println();
        System.out.println(Commands[5] + ": Delete information by index ");
        System.out.println();
        System.out.println(Commands[6] + ": Delete all information from library");
        System.out.println();
        System.out.println(Commands[7] + ": Adding information to the library from a file");
        System.out.println();
        System.out.println(Commands[8] + ": Template search ");
        System.out.println();
        System.out.println(Commands[9] + ": Displaying the contents of the library on the screen ");
        System.out.println();
        System.out.println(Commands[10] + ": End current session ");
        System.out.println();
    }



    @Override
    public void update(String eventType) {
        System.out.println();
        System.out.println(eventType);
        System.out.println();
    }
}
