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
 * Die Klasse dient dem Suchen von Buechern in der Datenbank auf Anfrage der Clients.
 */
public class SearchBooks {

    //Connection aus "ConnectDB" zur herstellung einer Verbindung mit der Datenbank
    private Connection connection;

    //"Books" Objekt mit Suchanfragen
    private Books book;

    /**
     * Konstruktor, um aus "ConnectDB" erhaltenes "Connection" Objekt "connection" und erhaltenes "Books" Objekt
     * "book" zu uebergeben.
     * @param connection
     */
    public SearchBooks(Connection connection, Books book){

        this.connection = connection;
        this.book = book;
    }

    /**
     * Die Methode nutze die durch den Konstruktor erhaltenen Objekte, um eine die gesuchten Werte in der Datenbank
     * mit einem SQL-Befehl zu suchen und die gefundenen Buecher als eine List<Books> zurueck zu geben.
     * @return "List" an "Books" Objekten, die auf die Suchanfrage zutreffen.
     */
    public List<Books> searchInDatabase(){
        //List, in die die Suchtreffer gespeichert werden sollen
        List<Books> results = new ArrayList<>();

        //Erhalt der Informationen ueber die Getter aus "Books"
        try {
            String title = book.getTitle();
            String genre = book.getGenre();
            String authorName = book.getAuthorName();
            String authorSurname = book.getAuthorSurName();
            String isbn = book.getIsbn();
            String releaseDate = book.getReleaseDate();
            //SQL-Befehle zum Suchen in der Datenbank
            StringBuilder sb = new StringBuilder("SELECT * FROM BOOKS b ");
            sb.append("JOIN AUTHORS a ON b.AUTHOR_ID = a.AUTHOR_ID ");
            sb.append("JOIN GENRES g ON b.GENRE_ID = g.GENRE_ID ");
            //Wahrheitsbedingung
            sb.append("WHERE 1=1 ");
            /*If-Bedingungen, um zu pruefen, welche Suchkriterien vom Client angegeben wurden, um
             nur nach diesen zu suchen*/
            if(title != null && !title.isEmpty()){
                sb.append(" AND b.BOOK_NAME LIKE ? ");
            }
            if(genre != null && !genre.isEmpty()){
                sb.append(" AND g.NAME LIKE ? ");
            }
            if(authorName != null && !authorName.isEmpty()){
                sb.append(" AND a.NAME LIKE ? ");
            }
            if(authorSurname != null && !authorSurname.isEmpty()){
                sb.append(" AND a.SURNAME LIKE ? ");
            }
            if(isbn != null && !isbn.isEmpty()){
                sb.append(" AND b.ISBN LIKE ? ");
            }
            if(releaseDate != null && !releaseDate.isEmpty()){
                sb.append(" AND b.RELEASE_DATE LIKE ? ");
            }
            //Zusammensetzen der fuer die Suche geforderten SQL-Befehle
            PreparedStatement psSearch = connection.prepareStatement(sb.toString());
            /*Index, um Suchwerte an die richtige Stelle des SQL-Befehls zu setzen
            wird nach jedem existierenden Suchwert erhoeht
             */
            int index = 1;
            //Einsetzen der Suchwerte, wenn sie nicht null oder leer sind
            if (title != null && !title.isEmpty()) {
                psSearch.setString(index++, "%" + title + "%");
            }
            if (genre != null && !genre.isEmpty()) {
                psSearch.setString(index++, "%" + genre + "%");
            }
            if (authorName != null && !authorName.isEmpty()) {
                psSearch.setString(index++, "%" + authorName + "%");
            }
            if (authorSurname != null && !authorSurname.isEmpty()) {
                psSearch.setString(index++, "%" + authorSurname + "%");
            }
            if (isbn != null && !isbn.isEmpty()) {
                psSearch.setString(index++, "%" + isbn + "%");
            }
            if (releaseDate != null && !releaseDate.isEmpty()) {
                psSearch.setString(index, "%" + releaseDate + "%");
            }

            //Ausfuehren des SQL-Befehls
            ResultSet rs = psSearch.executeQuery();
            /*Solange Ergebnisse gefunden wurden in der Datenbank, erstellen von "Books" Objekten mit den
            gefundenen Werten*/
            while (rs.next()){

                Books books = new Books();
                books.setIsbn(rs.getString("ISBN"));
                books.setTitle(rs.getString("BOOK_NAME"));
                books.setGenre(rs.getString("g.NAME"));
                books.setAuthorName(rs.getString("a.NAME"));
                books.setAuthorSurName(rs.getString("SURNAME"));
                books.setReleaseDate(rs.getString("RELEASE_DATE"));
                //Einfuegen der Ergebnisse in List
                results.add(books);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
