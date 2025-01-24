package de.medieninformatik.server.database;

import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse beinhaltet nur eine Methode, um alle Tabellen aus der Datenbank zu loeschen.
 */
public class DeleteTables {

    //Statementattribut, um Befehl zur Loeschung ausfuehren zu koennen
    private final Statement STATEMENT;

    /**
     * Konstruktor erhaelt "statement" aus "ConnectDB"
     * @param statement "Statement" Instanz
     */
    public DeleteTables(Statement statement){
        this.STATEMENT = statement;
    }

    /**
     * Die Methode erstellt eine ArrayList mit Strings, die die Namen der Tabellennamen in der Datenbank beinhalten
     * und loescht diese in der Reihenfolge ueber eine for-each-Schleife.
     * @param del Booleanwert, der vom Hauptnutzer gesendet wurde.
     * @return Antwort, ob die Tabellen geloescht werden konnten.
     */
    public String deleteTables(boolean del){

        //Negative Antwort
        String answer = "Tabellen wurden nicht gelöscht.";
        //Nur Ausfuehren, wenn del == true
        if(del) {
            //ArrayList, die die Namen der Tabellen der Datenbank als Strings enthält
            List<String> tableNames = new ArrayList<>();
            //Hinzufügen der Tabellennamen
            tableNames.add("USERS");
            tableNames.add("BOOKS");
            tableNames.add("GENRES");
            tableNames.add("AUTHORS");
            //try-catch-Block um mögliche Exceptions abzufangen
            try {
                //for-each-Schleife, um die Einträge in "tableName" nacheinander einzufügen
                for (String tableName : tableNames) {
                    //SQL-Befehl, um die Tabellen aus der Datenbank zu entfernen
                    String deleteTable = "DROP TABLE IF EXISTS " + tableName;
                    STATEMENT.executeUpdate(deleteTable);
                }
            }catch (SQLException e) {throw new RuntimeException(e);}
            //Positive Antwort
            answer = "DIE TABELLEN WURDEN ENTFERNT.";
        }
        return answer;
    }
}
