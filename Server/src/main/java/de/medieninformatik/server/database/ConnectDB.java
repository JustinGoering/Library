package de.medieninformatik.server.database;

import de.medieninformatik.common.Books;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse stellt eine Verbindung zur Datenbank "library" her, gibt Informationen aus der Datenbank an den
 * Server weiter und gibt neue, vom Server erhaltene Elemente, an andere Klassen weiter, um Aktionen in der
 * Datenbank auszuführen.
 */
public class ConnectDB {

    //Instanz der Klasse "ConnectDB"
    private static ConnectDB instance;

    //Statement Attribut, um SQL-Befehle an die Datenbank zu senden
    private Statement statement;

    //Connection Attribut, stellt Verbindung zur Datenbank her
    private Connection connection;

    /**
     * Privater Konstruktor, um keine Instanz von außerhalb der Klasse erzeugen zu können
     */
    private ConnectDB(){}

    /**
     * Die Methode erstellt, eine Instanz der Klasse "ConnectDB", wenn noch keine vorhanden und übergibt sie
     * an die Klasse, die die Methode aufruft.
     *
     * @return Instanz der Klasse "ConnectDB"
     */
    public static ConnectDB getInstance(){

        //Erzeugt neue Instanz der Klasse, wenn "instance" null ist
        if(instance == null){

            instance = new ConnectDB();
        }
        return instance;
    }

    /**
     * Die Methode ruft die Methode "getRessources()" auf, um von außerhalb der Klasse eine
     * Verbindung mit der Datenbank herzustellen.
     */
    public void startDB(){
        getRessources();
    }

    /**
     * Die Methode holt die Informationen aus der Datei "Select.properties" in den "resources",
     * um sie an die Methode "getConnection()" weiterzugeben.
     * Zu den Informationen gehoeren der JDBC Driver, die URL, der Nutzername und das Passwort fuer
     * die Datenbank.
     */
    private void getRessources(){

        //Aufrufen der "Select.properties" Datei, um auf die dort gespeicherten Daten zuzugreifen
        ResourceBundle rb = ResourceBundle.getBundle("Select");
        //Speichert Treiber der Datenbank in "driver"
        String driver = rb.getString("Driver");
        //Speichert URL der Datenbank in "url"
        String url = rb.getString("URL");
        //Speichert Nutzernamen der Datenbank in "user"
        String user = rb.getString("User");
        //Speichert Passwort der Datenbank in "password"
        String password = rb.getString("Password");
        //Gibt aus "Select.properties" erhaltene Informationen an "getConnection()" weiter
        connect(driver, url, user, password);
    }

    /**
     * Die Methode stellt eine Verbindung zur Datenbank "library" her
     * @param driver Treiber der Datenbank
     * @param url URL der Datenbank
     * @param user Nutzername der Datenbank
     * @param password Passwort der Datenbank
     */
    private void connect(String driver, String url, String user, String password) {

        //try-catch-Block um mögliche Exceptions abzufangen
        try {
            //Gibt Class-Objekt für "driver" zurück
            Class.forName(driver);
            //Stellt eine Verbindung zur Datenbank her, übergibt "url", "user" und "password"
            connection = DriverManager.getConnection(url, user, password);
            //Erstellt neues "statement", um SQL-Befehle an die Datenbank zu senden.
            statement = connection.createStatement();

        }
        //Exception, falls der Datenbanktreiber nicht gefunden wurde
        catch (ClassNotFoundException e){

            System.err.println("Der Datenbanktreiber wurde nicht gefunden.");
            System.exit(-1);
        }
        //Exception, wenn Probleme, beim Versuch eine Verbindung zur Datenbank aufzubauen entstehen
        catch (SQLException e){

            System.err.println("Es konnte keine Verbindung zu der Datenbank hergestellt werden.");
            System.exit(-1);
        }
    }

    /**
     * Die Methode ruft den Inhalt der "library.sql" Datei auf und fuehrt dessen Inhalt in der Datenbank aus,
     * um Tabellen zu erstellen und diese mit Startwerten zu fuellen.
     * die Datenbank.
     */
    public void fillStartData(){

        //SQL Datei zum Erstellen von Tabellen und hinzufuegen von Werten
        String file = "src/main/resources/library.sql";
        String readFromFile;

        //try-catch-Block, um Fehler abzufangen
        try {
            /*Uebergabe des Pfades "file" an einen "FileReader"
            Einlesen der SQL Datei, zu der der Pfad fuehrt*/
            BufferedReader bReader = new BufferedReader(new FileReader(file));

            //Erstellen eines Strings aus dem Inhalt von "library.sql"
            StringBuilder builder = new StringBuilder();
            while ((readFromFile = bReader.readLine()) !=null){
                builder.append(readFromFile).append("\n");
            }
            //Speichern jedes SQL Befehls an einer Stelle des String Arrays "arr"
            String[] arr = builder.toString().split(";");

            for(String getFromArr : arr){
                //Entfernen von Leerzeichen am Anfang und Ende jedes Strings aus "arr"
                String trim = getFromArr.trim();
                if (!trim.isEmpty()){
                    //Ausfuehren des aktuellen SQL-Befehls aus "arr"
                    statement.execute(trim);
                }
            }
        }
        //Werfen einer RuntimeException
        catch (IOException | SQLException e) {throw new RuntimeException(e);}
    }

    /**
     * Die Methode erstellt eine Instanz der Klasse "UserNames" und ruft deren Methode "prove()" auf,
     * um die Existenz des vom Client eingegebenen Nutzernamens zu ueberpruefen.
     * @param userName Vom Client eingegebener Nutzername.
     * @return Boolean = true, wenn der Nutzername frei ist, Boolean = false, wenn er vergeben ist.
     */
    public boolean proveUserNames(String userName){

        //Erstellt "Usernames" Instanz
        UserNames userNames = new UserNames();
        //Uebergibt "connection" an Setter der "UserNames" Klasse
        userNames.setConnection(connection);
        //Uebergibt "userName" an Setter der "UserNames" Klasse
        userNames.setUserName(userName);
        //Aufruft der "prove()" Methode aus "UserNames" erhalten von Booleanwert
        boolean isInUse = userNames.prove();

        return isInUse;
    }

    /**
     * Die Methode ruft "LendBooks" und dessen "getBooksFromUser()" Methode auf, um die ausgeliehenen Buecher
     * des Clients aus der Datenbank zu erhalten.
     * @param userName Nutzername des Clients.
     * @return "List<Books>" mit ausgeliehenen Buechern.
     */
    public List<Books> getBooksFromUser(String userName){
        LendBooks lendBooks = new LendBooks(connection);
        return lendBooks.getBooksFromUser(userName);

    }

    /**
     * Die Methode ruft die "InsertBooks" Klasse auf, um ein neues Buch zur Datenbank hinzuzufuegen.
     * @param books Neues Buch, das zur Datenbank hinzugefuegt werden soll.
     * @return Antwort, ob das Buch in die Datenbank aufgenommen werden konnte.
     */
    public String addBooks(Books books){

        //Antwort, dass das Buch hinzugefuegt werden konnte
        String answer = "Das Buch wurde zur Datenbank hinzugefuegt.";
        //Uebergabe von "connection" und "books" an "InsertBooks", um das Buch in die Datenbank einfuegen zu koennen
        InsertBooks insertBooks = new InsertBooks(connection, books);
        return answer;
    }

    /**
     * Uebergabe des vom Nutzer gesuchten Buches an die Klasse "SearchBooks", um nach Buecher zu suchen,
     * die die Suchkriterien im "Books" Objekt erfuellen.
     * @param books Gesuchtes Buch als "Books" Objekt
     * @return "List" der auf die Suche zutreffenden Buecher
     */
    public List<Books> searchBooks(Books books){

        //Neue "SearchBooks" Instanz erhaelt "connection" und "books" uebr ihren Konstruktor
        SearchBooks searchBooks = new SearchBooks(connection, books);
        //Rueckgabe der Suchergebnisse
        return searchBooks.searchInDatabase();
    }

    /**
     * Die Methode erstellt eine neue "LendBooks" Instanz und uebergibt ihrem Konstruktor "connection",
     * um ein ausgeliehenes Buch zurueck zu geben.
     * @param books Zurueck gegebenes Buch des Clients.
     */
    public void returnBook(Books books){

        //Neue "LendBooks" Instanz, uebergabe von "connection" an den Konstruktor
        LendBooks lendBooks = new LendBooks(connection);
        //aufrufen der Methode in "LendBooks", um das Buch zurueck zu geben
        lendBooks.returnBook(books);
    }

    /**
     * Die Methode Erstellt eine neue "LendBooks" Instanz und uebergibt dem Konstruktor der Klasse "connection",
     * um das die gleichnamige Methode der "LendBooks" Klasse aufzurufen und das Buch, wenn moeglich, auszuleihen.
     * @param books vom Client ausgewaehltes Buch
     * @return Status, ob das Buch ausgeliehen werden konnte (0) oder nicht (1)
     */
    public int lendBook(Books books){

        //Neue "LendBooks" Instanz, uebergabe von "connection" an den Konstruktor
        LendBooks lendBooks = new LendBooks(connection);
        //Erhalten des Ausleihstatus aus "lendBooks()"
        int lendStatus = lendBooks.lendBook(books);
        return lendStatus;
    }

    /**
     * Die Methode uebergibt die ISBN-Nummer, die der Hauptnutzer eingegeben hat an die "remove()" Methode in
     * "RemoveBooks", um das Buch aus der Datenbank zu entfernen.
     * @param isbn ISBN-Nummer des zu loeschenden Buches.
     * @return Antwort ueber die erfolgreiche Loeschung.
     */
    public String removeBooks(String isbn){

        //Neue Instannz der "RemoveBooks" Klasse und Uebergabe von "connection" an den Konstruktor
        RemoveBooks removeBooks = new RemoveBooks(connection);
        //Erhalten der Antwort, ob das Buch geloescht wurde
        String answer = removeBooks.remove(isbn);
        return answer;
    }

    /**
     * Die Methode erstellt eine "DeleteTable" Instanz und uebergibt "statement", um ueber die gleichnamige Methode
     * in "DeleteTables" alle Tabellen der Datenbank zu loeschen.
     * @return Meldung, dass die Tabellen der Datenbank geloescht wurden.
     */
    public String deleteTables(boolean del) {

        //Erstellen der "DeleteTables" Instanz und Uebergabe von "statement"
        DeleteTables deleteTables = new DeleteTables(statement);
        //Erhalten der Antwort, ob Loeschung erfolgreich war
        String answer = deleteTables.deleteTables(del);
        return answer;
    }
}