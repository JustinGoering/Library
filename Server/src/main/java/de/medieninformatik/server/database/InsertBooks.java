package de.medieninformatik.server.database;

import de.medieninformatik.common.Books;

import java.sql.*;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse dient dem Einfuegen von Buechern in die Datenbank.
 */
public class InsertBooks {

    //Connecntion Instanz aus, um SQL-Befehle in der Datenbank ausfuehren zu koennen
    private Connection connection;

    //Objekt, dass Informationen zum Buch enthaelt, welches eingefuegt werden soll
    private Books books;

    /**
     * Der Konstruktor uebergibt die erhaltene "connection" und "books" an die eigenen Attribute und
     * ruft die Methode "fillData()" auf.
     * @param connection "Connection" Instanz aus "ConnectDB"
     * @param books "Books" Objekt mit Informationen zum Buch, das in die Datenbank aufgenommen werden soll.
     */
    public InsertBooks(Connection connection, Books books){
        this.connection = connection;
        this.books = books;
        fillData();
    }

    /**
     * Die Methode fuegt neue Buecher zur Datenbank hinzu ueber festgelegte SQL-Befehle.
     */
    private void fillData() {

        //SQL-Befehle, um Werte in die Tabellen einzufügen
        String fillBookData = "INSERT INTO BOOKS (ISBN, BOOK_NAME, GENRE_ID, AUTHOR_ID, RELEASE_DATE, LEND) " +
                "VALUES (?, ?, ?, ?, ?, 0)";

        //ID des Buchgenres in der Datenbank
        int genreID;
        //ID des Autors in der Datenbank
        int authorID;
        //ISBN des Buches
        String isbn;
        //Ueberpruefen, ob das Genre bereits in der Datenbank existiert
        genreID = checkGenre(books);
        //Ueberpruefen, ob der Autor bereits in der Datenbank existiert
        authorID = checkAuthor(books);
        //Ueberpruefen, ob die ISBN bereits in der Datenbank existiert
        isbn = checkISBN(books);

        //Hinzufuegen des Buches in die Datenbank, wenn die ISBN noch nicht vorhanden ist
        if(!isbn.equals("-1")){

            //try-catch-Block um Exceptions abzufangen
            try {
                //Einfuegen der Werte in den SQL-Befehl
                PreparedStatement psb = connection.prepareStatement(fillBookData);

                psb.setString(1, isbn);
                psb.setString(2, books.getTitle());
                psb.setInt(3, genreID);
                psb.setInt(4, authorID);
                psb.setString(5, books.getReleaseDate());
                //aufuehren des SQL-Befehls in der Datenbank
                psb.executeUpdate();
                //Bestaetigen der Transaktion in der Datenbank
                connection.commit();

            }catch (SQLException e) {throw new RuntimeException(e);}
        }
        //Fehlermeldung, wenn die ISBN bereits existiert
        else{
            System.err.println("FEHLER BEIM HINZUFÜGEN DER DATEN IN DIE DATENBANK.");
        }
    }

    /**
     * Die Methode ueberprueft, ob die angegebene ISBN in "book" bereits in der Datenbank ist.
     * @param book "Books" Objekt, dessen Inhalt in die Datenbank aufgenommen werden soll.
     * @return neue ISBN-Nummer oder -1 bei bereits existierender ISBN-Nummer.
     */
    private String checkISBN(Books book){

        //erhaelt den ISBN Rueckgabewert
        String proveISBN;
        //SQL-Befehl um ISBN zu suchen
        String checkISBN = "SELECT ISBN FROM BOOKS WHERE ISBN LIKE ?";

        //ausfuehren des SQL-Befehls
        try {
            PreparedStatement psci = connection.prepareStatement(checkISBN);

            psci.setString(1, "%" + book.getIsbn() + "%");
            ResultSet rs = psci.executeQuery();
            //"proveISBN" auf -1 setzen, falls ISBN bereits in der Datenbank ist
            if(rs.next()){proveISBN = "-1";}
            //Neue ISBN Nummer zurueck geben
            else {proveISBN = book.getIsbn();}
        } catch (SQLException e) {throw new RuntimeException(e);}
        return proveISBN;
    }

    /**
     * Die Methode prueft, ob das Genre des hinzugefuegten Buches bereits in der Datenbank existiert.
     * @param book Buch Objekt
     * @return Gibt ResultSet rs zurück
     */
    private int checkGenre(Books book){

        // erhaelt GenreID Rueckgabewert
        int id;
        //SQL-Befehl um Genre in der Datenbank zu suchen
        String checkGenre = "SELECT GENRE_ID FROM GENRES WHERE NAME LIKE ?";

        //ausfuehren des SQL-Befehls
        try {
            PreparedStatement pscg = connection.prepareStatement(checkGenre);

            pscg.setString(1, "%" + book.getGenre() + "%");
            ResultSet rs = pscg.executeQuery();
            //Erhalten der GenreID aus der Datenbank, wenn vorhanden
            if(rs.next()){id = rs.getInt("GENRE_ID");}
            //aufrufen von "insertGenre()", um neue GenreID zu erhalten
            else {id = insertGenre(book);}
        } catch (SQLException e) {throw new RuntimeException(e);}
        return id;
    }

    /**
     * Hinzufuegen eines neuen Genres, das noch nicht in der Datenbank ist.
     * @param book "Books" Objekt mit neuen Genre.
     * @return GenreID aus der Datenbank des neuen Genres.
     */
    private int insertGenre(Books book){

        // erhaelt GenreID Rueckgabewert
        int id = -1;
        //SQL-Befehl um Genre in der Datenbank einzufuegen
        String fillGenreData = "INSERT INTO GENRES (NAME) VALUES (?)";
        //ausfuehren des SQL-Befehls
        try {
            PreparedStatement pscg = connection.prepareStatement(fillGenreData, Statement.RETURN_GENERATED_KEYS);

            pscg.setString(1, book.getGenre());
            pscg.executeUpdate();
            //erhalten der neuen GenreID
            ResultSet key = pscg.getGeneratedKeys();
            if(key.next()) id = key.getInt(1);
        } catch (SQLException e) {throw new RuntimeException(e);}
        return id;
    }

    /**
     * Die Methode prueft, ob der Autor des hinzugefuegten Buches bereits in der Datenbank existiert.
     * @param book Buch Objekt
     * @return Gibt ResultSet rs zurück
     */
    private int checkAuthor(Books book){

        // erhaelt AuthorID Rueckgabewert
        int id;
        //SQL-Befehl um Autor in der Datenbank zu suchen
        String checkAuthor = "SELECT AUTHOR_ID FROM AUTHORS WHERE NAME LIKE ? AND SURNAME LIKE ?";
        //ausfuehren des SQL-Befehls
        try {
            PreparedStatement pscg = connection.prepareStatement(checkAuthor);

            pscg.setString(1, "%" + book.getAuthorName() + "%");
            pscg.setString(2, "%" + book.getAuthorSurName() + "%");
            ResultSet rs = pscg.executeQuery();
            //Erhalten der AuthorID aus der Datenbank, wenn vorhanden
            if(rs.next()){id = rs.getInt("AUTHOR_ID");}
            //aufrufen von "insertAuthor()", um neue AuthorID zu erhalten
            else {id = insertAuthor(book);}
        } catch (SQLException e) {throw new RuntimeException(e);}
        return id;
    }

    /**
     * Hinzufuegen eines neuen Autors, der noch nicht in der Datenbank ist.
     * @param book "Books" Objekt mit neuem Autor.
     * @return AuthorID aus der Datenbank des neuen Autors.
     */
    private int insertAuthor(Books book){

        // erhaelt AuthorID Rueckgabewert
        int id = -1;
        //SQL-Befehl um Autor in der Datenbank einzufuegen
        String fillAuthorData = "INSERT INTO AUTHORS (NAME, SURNAME) VALUES (?, ?)";
        //ausfuehren des SQL-Befehls
        try {
            PreparedStatement psca = connection.prepareStatement(fillAuthorData, Statement.RETURN_GENERATED_KEYS);

            psca.setString(1, book.getAuthorName());
            psca.setString(2, book.getAuthorSurName());
            psca.executeUpdate();
            //erhalten der neuen AuthorID
            ResultSet key = psca.getGeneratedKeys();
            if(key.next()) id = key.getInt(1);
        } catch (SQLException e) {throw new RuntimeException(e);}
        return id;
    }
}
