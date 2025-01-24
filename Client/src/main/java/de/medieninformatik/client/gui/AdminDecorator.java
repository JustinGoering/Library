package de.medieninformatik.client.gui;

import de.medieninformatik.client.ServerConnection;
import de.medieninformatik.common.Books;

import javafx.geometry.Insets;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import javafx.stage.Stage;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Klasse, die von der abstrakten Klasse "UserDecorator" erbt und sich um weitere Eigenschaften erweitert.
 */
public class AdminDecorator extends UserDecorator {

    //Instanz fuer die "ServerConnection" Klasse zum Senden und Empfangen von Daten
    private final ServerConnection SERVERCONNECTION = ServerConnection.getInstance();
    /*Textfelder um Buecher zur Datenbank vom Admin hinzuzufuegen fuer: Titel, Autor, Genre, ISBN und Datum der
    Veroeffentlichung*/
    private TextField nameField;
    private TextField authorField;
    private TextField genreField;
    private TextField isbnField;
    private TextField releaseDate;


    /**
     * Konstruktor erhält eine "UserComponent" Instanz und übergibt sie an den Konstruktor der Basisklasse
     * "UserDecorator".
     * @param userComponent Instanz aus der ProveUserName Klasse.
     */
    public AdminDecorator(UserComponent userComponent){super(userComponent);}

    /**
     * Überschriebene Methode aus "UserDecorator" fügt weitere Eigenschaften der GUI zu.
     * @param stage Stage aus der "LoginGUI" Klasse
     */
    @Override
    public void userGUI(Stage stage) {

        super.userGUI(stage);

        VBox adminLayout = new VBox(10, createInputGrid(), adminButtons());

        ((BorderPane) stage.getScene().getRoot()).setLeft(adminLayout);
    }

    /**
     * Erstellen eines GridPane für Textfeldeingaben, um neue Bücher in die Datenbank einzufügen.
     * @return Gridpane für extra Textfelder.
     */
    private GridPane createInputGrid() {

        /*Erstellen eines "GridPane" mit vertikalen und horizontalen Abstand von 10,
        sowie einem oberen Padding von 10*/
        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);
        inputGrid.setPadding(new Insets(10, 0, 0, 0));
        //"Label", um dem Admin zu zeigen, wo Buecher hinzugefuegt werden koennen
        inputGrid.add(new Label("ADD/REMOVE BOOKS"), 0, 0, 2, 1);
        //"Label" und "Textfield" fuer den Buchnamen
        inputGrid.add(new Label("Name:"), 0, 1);
        nameField = new TextField();
        inputGrid.add(nameField, 1, 1);
        //"Label" und "Textfield" fuer den Autorennamen
        inputGrid.add(new Label("Author:"), 0, 2);
        authorField = new TextField();
        inputGrid.add(authorField, 1, 2);
        //"Label" und "Textfield" fuer das Buchgenre
        inputGrid.add(new Label("Genre:"), 0, 3);
        genreField = new TextField();
        inputGrid.add(genreField, 1, 3);
        //"Label" und "Textfield" fuer die ISBN des Buches
        inputGrid.add(new Label("ISBN:"), 0, 4);
        isbnField = new TextField();
        inputGrid.add(isbnField, 1, 4);
        inputGrid.add(new Label("Release Date:"), 0, 5);
        releaseDate = new TextField();
        inputGrid.add(releaseDate,1,5);

        return inputGrid;
    }

    /**
     * Erstellt eine HBOX aus den zusätzlichen Buttons des Admins.
     * @return übergibt HBOX an "userGUi()".
     */
    private HBox adminButtons(){

        return new HBox(createDeleteTablesButton(), createAddBooksButton(), createRemoveBooksButton());
    }

    /**
     * Erstellt Button, der die Methode "deleteTables()" aufruft.
     * @return Button zum Löschen aller Tabellen der Datenbank.
     */
    private Button createDeleteTablesButton(){

        Button deleteTablesButton = new Button("Delete Tables");
        deleteTablesButton.setOnAction(event -> deleteTables());
        return deleteTablesButton;
    }

    /**
     * Erstellt Button, der die Methode "addBooks()" aufruft.
     * @return Button, zum Hinzufügen von neuen Büchern zur Datenbank.
     */
    private Button createAddBooksButton() {

        Button addBooksButton = new Button("Add Books");
        addBooksButton.setOnAction(event -> addBooks(nameField.getText(), authorField.getText(), genreField.getText(),
                isbnField.getText(), releaseDate.getText()));
        return addBooksButton;
    }

    /**
     * Erstellt Button, der die Methode "removeBooks()" aufruft.
     * @return Button, zum Entfernen von Büchern aus der Datenbank.
     */
    private Button createRemoveBooksButton() {

        Button removeBooksButton = new Button("Remove Books");
        removeBooksButton.setOnAction(event -> removeBooks(isbnField.getText()));
        return removeBooksButton;
    }

    /**
     * Die Methode erlaubt dem Admin neue Buecher in die Datenbank aufzunehmen.
     * Sie prueft, ob alle Informationen vom Admin fuer die Aufnahme in die Datenbank erfuellt wurden
     * und erstellt, wenn dem so ist ein "Books" Objekt zur Uebergabe an "ServerConnection".
     * @param title Buchtitel
     * @param author Autorenname
     * @param genre Buchgenre
     * @param isbn ISBN-Nummer
     */
    private void addBooks(String title, String author, String genre, String isbn, String releaseDate) {

        //Fehlermeldung, falls Informationen fehlen, um das Buch zur Datenbank hinzuzufuegen
        String answer = "Es fehlen Informationen, um das Buch in die Datenbank aufzunehmen.";
        //Pruefen, ob alle Strings die noetigen Daten enthalten
        if(title != null && !title.isEmpty() &&
                author != null && !author.isEmpty() &&
                genre != null & !genre.isEmpty() &&
                isbn != null && !isbn.isEmpty() &&
                releaseDate != null && !releaseDate.isEmpty()) {
            //Array, um Autoren Vor- und Nachname separat zu speichern
            String[] name = new String[2];
            //Trennt den Autorennamen in zwei seperate Teile, um Vorname und Nachname extra zu speichern
            name = author.split("\\s+");

            //Erstellen eines "Books" Objekts mit allen vom Admin eingegeben Informationen
            Books books = new Books();
            books.setTitle(title);
            books.setAuthorSurName(name[0]);
            books.setAuthorName(name[1]);
            books.setGenre(genre);
            books.setIsbn(isbn);
            books.setReleaseDate(releaseDate);
            //Ubergabe an die Klasse "ServerConnection", um die Daten an den Server zu senden
            answer = SERVERCONNECTION.addBooks(books);
            System.out.println(answer);
        }
        else {
            System.err.println(answer);
        }
    }

    /**
     * Die Methode erlaubt dem Admin Buecher aus der Datenbank zu entfernen, mit Hilfe der ISBN-Nummer des Buches.
     * @param isbn ISBN- Nummer des zu entfernenden Buches
     */
    private void removeBooks(String isbn) {

        //Uebergabe der ISBN-Nummer an "ServerConnection", erhalten der Antwort, ob Aktion ausgefuehrt wurde
        String answer = SERVERCONNECTION.removeBook(isbn);
        System.out.println(answer);
    }



    /**
     * Diese Methode erlaubt dem Admin alle Tabellen der Datenbank zu löschen.
     */
    private void deleteTables() {

        String answer = SERVERCONNECTION.deleteTablesInDB();
        System.out.println(answer);
    }
}
