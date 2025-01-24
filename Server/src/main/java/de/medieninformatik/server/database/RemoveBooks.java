package de.medieninformatik.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Die Klasse ist zum Entfernen von Buechern aus der Datenbank zustaendig.
 */
public class RemoveBooks {

    //"Connection" Instanz
    private final Connection CONNECTION;

    /**
     * Konstruktor, erhaelt "connection" aus "ConnectDB".
     * @param connection "Connection" Instanz aus "ConnectDB".
     */
    public RemoveBooks(Connection connection){
        this.CONNECTION = connection;
    }

    /**
     * Die Methode ist zum Entfernen des Buches, dessen ISBN es als String erhaelt zustaendig.
     * @param isbn ISBN des Buches, das entfernt werden soll.
     * @return Antwort, ob das Buch entfernt werden konnte.
     */
    public String remove(String isbn){

        //Antwort vom Server an den Admin
        String answer;
        //SQL-Befehl um ein Buch aus der Datenbank zu loeschen
        String removeSQL = "DELETE FROM BOOKS WHERE ISBN LIKE ? ";
        //Ausfuehren des SQL-Befehls mit der vom Admin erhaltenen ISBN-Nummer
        try {
            PreparedStatement psb = CONNECTION.prepareStatement(removeSQL);
            psb.setString(1, isbn);
            //Ausfuehren des SQL-Befehls
            int deleted = psb.executeUpdate();
            //Bestaetigen der Transaktion in der Datenbank
            CONNECTION.commit();
            //Pruefen, ob mindestens eine Zeile aus "BOOKS" entfernt wurde und
            if(deleted > 0){
                answer = "Das Buch wurde entfernt.";
            }
            else {
                answer = "Das Buch wurde nicht entfernt.";
            }
            return answer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
