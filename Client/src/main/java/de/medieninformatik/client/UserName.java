package de.medieninformatik.client;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse speichert den Nutzernamen des Clients.
 */
public class UserName {

    //Instanz der "UserName" Klasse
    private static UserName instance;

    //Nutername des Clients
    private String userName;

    /**
     * Private Konstruktor, um nur eine Instanz zu erzeugen.
     */
    private UserName(){}

    /**
     * Getter, fuer die Instanz der Klasse "UserName".
     * Erstellt neue Instanz, wenn noch keine existiert.
     * @return Instanz der Klasse "UserName".
     */
    public static UserName getInstance(){

        //Erzeugt neue Instanz der Klasse, wenn "instance" null ist
        if(instance == null){
            instance = new UserName();
        }
        return instance;
    }

    /**
     * Setter, um den Nutzernamen des Clients zu speichern
     * @param userName Nutzername des Clients.
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Getter, um Nutzernamen des Clients an andere Klassen zu uebergeben.
     * @return Nutzername des Clients.
     */
    public String getUserName(){return this.userName;}
}
