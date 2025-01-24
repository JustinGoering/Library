package de.medieninformatik.client;

import de.medieninformatik.client.gui.AdminDecorator;
import de.medieninformatik.client.gui.User;

import javafx.stage.Stage;
/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse prüft den eingegebenen Nutzernamen, aus dem Textfeld der LoginGUI Klasse und
 * ruft dementsprechend die Klasse für einen normalen Nutzer oder den Admin auf.
 */
public class ProveUserName {

    private final String  ADMINNAME = "admin";

    /**
     * Default-Konstruktor der "ProveUserName" Klasse
     */
    public ProveUserName(){}

    /**
     * Methode zum Prüfen des Nutzernamens, und aufrufen eines "User" oder
     * einem mit "AdminDecorator" dekorierten "User".
     * @param userName eingegebener Nutzername des Clients.
     * @param stage Stage aus der "LoginGUI" Klasse.
     */
    public void prove(String userName, Stage stage){

        //Aufrufen der GUI des Admins
        if(userName.equals(ADMINNAME)){
            //Speichern des Nutzernamens in "UserName" Klasse
            UserName userNameClass = UserName.getInstance();
            userNameClass.setUserName(ADMINNAME);
            AdminDecorator admin = new AdminDecorator(new User());
            admin.userGUI(stage);
        }
        else {
            //Speichern des Nutzernamens in "UserName" Klasse
            UserName userNameClass = UserName.getInstance();
            userNameClass.setUserName(userName);
            //Aufrufen der GUI der Clients
            User user = new User();
            user.userGUI(stage);
        }
    }
}