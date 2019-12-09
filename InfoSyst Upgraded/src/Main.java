import MVS.InformationSystem;
import InformationClasses.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Main {
    public static void main(String[] args) {
        try {
            //Book x = new Book("1","1",1,40);
           // ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Datamase.txt"));
           // out.writeObject(x);
            InformationSystem.startLibrary();
        }
        catch (Exception e ) {
            System.out.println(e.getMessage());
        }

    }
}
