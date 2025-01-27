package de.medieninformatik.server;

import de.medieninformatik.server.database.ConnectDB;

/*
  @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Main Klasse auf der Server-Seite, sie beinhaltet die main-Methode zum Starten des Programms.
 */
public class Main {

    /**
     * Die main-Methode ruft die Klasse "ConnectDB" auf, die für die Datenbank zuständig ist,
     * sie ruft die Methode "startDB()" auf um eine Verbindung zur Datenbank herzustellen,
     * die Methode "deleteTables()", um Tabellen in der Datenbank zu löschen, die Methode "createTables()",
     * um neue Tabellen in der Datenbank zu erstellen und die Tabelle "fillData()" um Anfangswerte in die Tabelle
     * einzufügen.
     *
     * Sie ruft die Klasse "Server" auf um, den Server mit der Methode "startServer()" zu starten, und Verbindungen
     * mit diesem Möglich zu machen.
     * @param args
     */
    public static void main(String[] args) {

        //Holt Instanz der "ConnectDB" Klasse
        ConnectDB cdb = ConnectDB.getInstance();
        //Stellt Verbindung zur Datenbank her
        cdb.startDB();
        //Füllt die Datenbank mit Anfangswerten
        cdb.fillStartData();
        //Holt Instanz der Klasse "Server"
        Server server = Server.getInstance();
        //Startet den Server
        server.startServer();

    }
}