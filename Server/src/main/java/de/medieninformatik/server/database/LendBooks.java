package de.medieninformatik.server.database;

import de.medieninformatik.common.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
 * Die Klasse ist dafuer da um das Ausleihen und Zurueckgeben der Buecher in der Datenbank festzuhalten und
 * die ausgeliehenen Buecher der Clients abzufragen.
 */
public class LendBooks {

    //"Connection" Instanz
    private final Connection CONNECTION;

    /**
     * Konstruktor, der "connection" aus "ConnectDB" erhaelt und "CONNECTION" uebergibt.
     * @param connection "Connection" Instanz aus "ConnectDB".
     */
    public LendBooks(Connection connection){
        this.CONNECTION = connection;
    }

    /**
     * Die Methode prueft, ob das vom Client ausgewaehlt Buch ausgeliehen werden kann oder nicht und fuegt des den
     * ausgeliehenen Buechern des Clients in der Datenbank zu, wenn es moeglich ist.
     * @param books Buch das der Client ausleihen moechte.
     * @return Ausleihstatus 0 = kann ausgeliehen werden, 1 = wurde bereits ausgeliehen.
     */
    public int lendBook(Books books){

        //Integer, der zeigt, ob Buch ausgliehen ist (1) oder nicht (0)
        int lendStatus = 1;
        //SQL-Befehl zum pruefen, ob das Buch ausgeliehen wurde
        String proveLendStatus = "SELECT LEND FROM BOOKS WHERE ISBN LIKE ?";
        try {
            //Ausfuehren des SQL-Befehls zur Ueberpruefung, ob das Buch bereits ausgeliehen ist
            PreparedStatement ps = CONNECTION.prepareStatement(proveLendStatus);
            ps.setString(1, books.getIsbn());
            ResultSet rs = ps.executeQuery();
            //Wenn die Spalte mit dem Buch existiert
            if(rs.next()){
                //Erhalten des Ausleihstatus des Buches (0 oder 1)
                lendStatus = rs.getInt("LEND");
                //Wenn das Buch noch nicht ausgeliehen wurde
                if(lendStatus == 0){
                    //Aktualisieren des Ausleihstatus auf 1 und setzen des Rueckgabedatums auf in zwei Wochen
                    String setLendStatus = "UPDATE BOOKS SET LEND = 1, " +
                            "RETURN_DATE = DATE_ADD(NOW(), INTERVAL 2 WEEK) WHERE ISBN LIKE ?";
                    ps = CONNECTION.prepareStatement(setLendStatus);
                    ps.setString(1, books.getIsbn());
                    ps.executeUpdate();
                    //Bestaetigen der Transaktion
                    CONNECTION.commit();
                    //SQL-Befehl zum Hinzufuegen der ISBN in die USERS Tabelle
                    String sqlUsers = "INSERT INTO USERS (USERNAME, LEND_BOOK_ISBN) VALUES (?, ?)";
                    //Hinzufuegen des ausgeliehenen Buches zum Nutzernamen in der Datenbank
                    ps = CONNECTION.prepareStatement(sqlUsers);
                    ps.setString(1, books.getBorrower());
                    ps.setString(2, books.getIsbn());
                    ps.executeUpdate();
                    //Bestaetigen der Transaktion
                    CONNECTION.commit();
                    //Schließen von "ResultSet" und "PreparedStatement"
                    rs.close();
                    ps.close();
                    return lendStatus;
                }
            }
        } catch (SQLException e) {throw new RuntimeException(e);}
        //Rueckgabe von 1, Buch ist bereits ausgeliehen
        return lendStatus;
    }

    /**
     * Die Methode holt die vom Client ausgeliehen Buecher aus der Datenbank und gibt diese als "List" zurueck.
     * @param userName Nutzername des Clients.
     * @return Liste mit ausgeliehenen Buechern des Clients.
     */
    public List<Books> getBooksFromUser(String userName){

        //Erstellen einer "ArrayList"
        List<Books> usersBooks = new ArrayList<>();
        //SQL-Befehl zum Suchen nach den ausgeliehenen Buechern
        String sqlSearchUsersBooks = "SELECT BOOK_NAME, RETURN_DATE, ISBN, RELEASE_DATE, SURNAME, " +
                "a.NAME AS AUTHOR_NAME, g.NAME AS GENRE " +
                "FROM BOOKS b INNER JOIN USERS u " +
                "ON b.ISBN = u.LEND_BOOK_ISBN " +
                "INNER JOIN GENRES g ON b.GENRE_ID = g.GENRE_ID " +
                "INNER JOIN AUTHORS a ON b.AUTHOR_ID = a.AUTHOR_ID " +
                "WHERE u.USERNAME LIKE ?";

        try {
            //Ausfuehren des SQL-Befehls in der Datenbank
            PreparedStatement ps = CONNECTION.prepareStatement(sqlSearchUsersBooks);
            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();
            //Wenn Buecher in der Datenbank gefunden wurden
            while(rs.next()){
                //Erhalten des Buchtitels aus der Datenbank
                String title = rs.getString("BOOK_NAME");
                //Erhalten des Rueckgabedatums aus der Datenbank
                String returnDate = rs.getString("RETURN_DATE");
                //Erhalten der ISBN-Nummer aus der Dantenbank
                String isbn = rs.getString("ISBN");
                //Erhalten des Erscheinungsjahres des Buches aus der Datenbank
                String releaseDate = rs.getString("RELEASE_DATE");
                //Erhalten des Autorvornamens aus der Datenbank
                String authorSurname = rs.getString("SURNAME");
                //Erhalten des Autornachnamens
                String authorName = rs.getString("AUTHOR_NAME");
                //Erhalten des Genres
                String genre = rs.getString("GENRE");
                //Erstellen neuer "Books" instanz
                Books books = new Books();
                //Setzen des Buchtitels
                books.setTitle(title);
                //Setzen des Rueckgabedatums
                books.setReturnDate(returnDate);
                //Setzen der ISBN
                books.setIsbn(isbn);
                //Setzen des Datums der Veroeffentlichung
                books.setReleaseDate(releaseDate);
                //Setzen des Autovornamens
                books.setAuthorSurName(authorSurname);
                //Setzen des Autornachnamens
                books.setAuthorName(authorName);
                //Setzen des Genres
                books.setGenre(genre);
                //Hinzufuegen der "Books" Objekte zur "ArrayList"
                usersBooks.add(books);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usersBooks;
    }

    /**
     * Die Methode ist fuer die Rueckgabe von Buechern, die Ausgeliehen wurden zustaendig und setzt ihren Ausleihstatus
     * wieder auf 0.
     * @param books Buch, das der Client zurueck geben will.
     */
    public void returnBook(Books books){

        /*SQL-Befehle, um das Zurueckgeben des Buches in der Datenbank festzuhalten*/
        String sqlReturn = "DELETE FROM USERS WHERE LEND_BOOK_ISBN LIKE ?";
        String sqlResetLendStatus = "UPDATE BOOKS SET LEND = 0 WHERE ISBN LIKE ?";

        try {
            //Ausfuehren des SQL-Befehls, um Verbindung zwischen Client und Buch zu entfernen
            PreparedStatement ps = CONNECTION.prepareStatement(sqlReturn);
            ps.setString(1, books.getIsbn());
            ps.executeUpdate();
            //Bestaetigen der Transaktion
            CONNECTION.commit();
            //Ausfuehren des SQL-Befehls, um den Ausleihstatus wieder auf "0" zu setzen
            ps = CONNECTION.prepareStatement(sqlResetLendStatus);
            ps.setString(1, books.getIsbn());
            ps.executeUpdate();
            //Bestaetigen der Transaktion
            CONNECTION.commit();
        } catch (SQLException e) {
            try {
                //Ruecknahme der SQL Befehle, falls Fehler bei der Transaktion aufgetreten sind
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
