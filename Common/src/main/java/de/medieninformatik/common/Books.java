package de.medieninformatik.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse enthält mehrere Strings, die Informationen von Büchern enthalten, sowie Getter und Setter,
 * um diese Informationen einzufügen und abzurufen.
 */
public class Books implements Serializable{


    //Enthält die ISBN eines Buches
    @JsonProperty("isbn")
    private String isbn;
    //Enthält den Titel eines Buches
    @JsonProperty("title")
    private String title;
    //Enthält das Genre eines Buches
    @JsonProperty("genre")
    private String genre;
    //Enthält den Autor eines Buches
    @JsonProperty("authorName")
    private String authorName;
    @JsonProperty("authorSurname")
    private String authorSurname;
    //Enthält das Datum der Veröffentlichung eines Buches
    @JsonProperty("releaseDate")
    private String releaseDate;
    //Rueckgabedatum fuer das ausgeliehene Buch
    @JsonProperty("returnDate")
    private String returnDate;
    //Client der das Buch ausleiht
    @JsonProperty("borrower")
    private String borrower;

    /**
     * Setzen der ISBN
     * @param isbn ISBN Nummer
     */
    public void setIsbn(String isbn){this.isbn = isbn;}

    /**
     * Rueckgabe der ISBN bei Abfrage von anderen Klassen
     * @return ISBN Nummer
     */
    public String getIsbn(){return isbn;}

    /**
     * Setzen des Buchtitels
     * @param title Buchtitel
     */
    public void setTitle(String title){this.title = title;}

    /**
     * Rueckgabe des Buchtitels bei Abfrage von anderen Klassen
     * @return Buchtitel
     */
    public String getTitle(){return title;}

    /**
     * Setzen des Genres
     * @param genre Buchgenre
     */
    public void setGenre(String genre){this.genre = genre;}

    /**
     * Rueckgabe des Buchgenre bei Abfrage von anderen Klassen
     * @return Buchgenre
     */
    public String getGenre(){return genre;}

    /**
     * Setzen des Autor-Vornamens eines Buches
     * @param authorName Vorname des Buchautors
     */
    public void setAuthorName(String authorName){this.authorName = authorName;}

    /**
     * Rueckgabe des Autor-Vornamens bei Abfrage von anderen Klassen
     * @return authorName Vorname des Buchautors
     */
    public String getAuthorName(){return authorName;}

    /**
     * Setzen des Autor-Nachnamens eines Buches
     * @param authorSurname Nachname des Buchautors
     */
    public void setAuthorSurName(String authorSurname){this.authorSurname = authorSurname;}

    /**
     * Rueckgabe des Autor-Nachnamens bei Abfrage von anderen Klassen
     * @return authorSurname Nachname des Buchautors
     */
    public String getAuthorSurName(){return authorSurname;}

    /**
     * Setzen des Datums der Buchveröffentlichung
     * @param releaseDate Datum der Buchveröffentlichung
     */
    public void setReleaseDate(String releaseDate){this.releaseDate = releaseDate;}

    /**
     * Rueckgabe des Datums der Buchveröffentlichung bei Abfrage von anderen Klassen
     * @return Datum der Buchveröffentlichung
     */
    public String getReleaseDate(){return releaseDate;}


    /**
     * Setzen des Datums fuer das Rueckgabedatum nach dem Ausleihen
     * @param returnDate Rueckgabedatum
     */
    public void setReturnDate(String returnDate){this.returnDate = returnDate;}

    /**
     * Rueckgabe des Rueckgabedatums nach dem Ausleihen des Buches
     * @return Rueckgabedatum
     */
    public String getReturnDate(){return returnDate;}

    /**
     * Setzen des Ausleihers des Buches
     * @param borrower Client, der das Buch ausleiht
     */
    public void setBorrower(String borrower){this.borrower = borrower;}

    /**
     * Rueckgabe des Ausleihers des Buches
     * @return Client der das Buch ausleiht
     */
    public String getBorrower(){return this.borrower;}

    /**
     * ToString Methode zum Anzeigen aller Daten des "Books" Objekts.
     * @return Buchtitel, Autorenname, Genre, ISBN-Nummer und Veroeffentlichungsdatum
     */
    @Override
    public String toString(){

        return "Title: " + title + " | " + "Author: " + authorSurname + " " + authorName + " | " + "Genre: " +
                genre + " | " + "ISBN: " + isbn + " | " + "Release Date: " + releaseDate;
    }
}