package de.medieninformatik.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse speichert den von einem Client an den Server gesendeten Nutzernamen in der Datenbank, um sich
 * diesen für erneute Anmeldungen, in der Bibliothek, zu speichern.
 */
public class UserNames {

    //Zur Herstellung einer Verbindung mit der Datenbank
    private Connection connection;
    //Nutzername des Clients, der sich anmelden will
    private String userName;

    /**
     * Default-Konstruktor der Klasse "UserNames"
     */
    public UserNames(){}

    /**
     * Setter fuer die Connection Instanz, um eine Verbindung mit der Datenbank herstellen zu koennen
     * @param connection Instanz zur Datenbankverbindung
     */
    public void setConnection(Connection connection){this.connection = connection;}

    /**
     * Setter fuer den vom Client eingegebenen Nutzernamen
     * @param userName Nutzername des Clients
     */
    public void setUserName(String userName){this.userName = userName;}

    /**
     * Die Methode prueft, ob der vom Client eingegebene Nutzername bereits in der Datenbank ist.
     * @return Boolean false, wenn der Nutzername schon in der Datenbank ist, Boolean true, wenn der Nutzername
     * nicht vorhanden ist.
     */
    public boolean prove(){
        //SQL-Befehl, um eingegebenen Nutzernamen in der Datenbank zu suchen
        String sqlsearchUsername = "SELECT USERNAME FROM USERS WHERE USERNAME LIKE ?";
        try {
            //Ausfuehren des SQL-Befehls in der Datenbank
            PreparedStatement ps = connection.prepareStatement(sqlsearchUsername);
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();
            //Rueckgabe von false, wenn ein uebereinstimmender Nutzername in der Datenbank gefunden wurde
            if(rs.next()){
                return false;
            }
            else {
                //Aufrufen der Methode, um einen neuen Nutzernamen in der Datenbank zu speichern
                saveNewUserName();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Die Methode speichert einen neune Nutzernamen in der Datenbank.
     */
    private void saveNewUserName(){
        //SQL-Befehl, um den Nutzernamen in die Datenbank einzufuegen
        String sqlSetUsername = "INSERT INTO USERS (USERNAME) VALUES (?)";
        try {
            //Ausfuehren des SQL-Befehls in der Datenbank
            PreparedStatement ps = connection.prepareStatement(sqlSetUsername);
            ps.setString(1, userName);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}