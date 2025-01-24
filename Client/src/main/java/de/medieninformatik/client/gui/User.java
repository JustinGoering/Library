package de.medieninformatik.client.gui;

import de.medieninformatik.client.ServerConnection;
import de.medieninformatik.client.UserName;
import de.medieninformatik.common.Books;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

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
 * Klasse für den normalen Nutzer ohne Adminrechte.
 * Implementiert das "UserComponent" Interface für das Dekoratormuster.
 * Besitzt Methoden zur Erstellung der JavaFX GUI, sowie zum Aufruf der "ServerConnection" Klasse,
 * um eine Verbindung zum Server herzustellen und Daten senden und empfangen zu können.
 */
public class User implements UserComponent {

    //Books Objekt zum senden an den Server
    private Books book;
    /*Textfelder fuer die Sucheingaben des Clients fuer Buchname, Autorenname, Genre, ISBN und Datum der
    Veroeffentlichung*/
    private TextField nameField;
    private TextField authorField;
    private TextField genreField;
    private TextField isbnField;
    private TextField releaseDate;
    //"ListView" fuer die Suchergebnisse des Clients
    private ListView<Books> listResult;
    //ServerConnection Instanz
    private final ServerConnection SERVERCONNECTION = ServerConnection.getInstance();
    //List mit gesuchten Buechern
    private List<Books> booksList;

    /**
     * Default - Konstruktor
     */
    public User(){}

    /**
     * Die Methode uebergibt den vom Client eingegebenen Nutzernamen an die Klasse "ServerConnection", zur
     * Ueberpruefung, ob der Name schon auf dem Server gespeichert ist.
     * @return Antwort des Servers.
     */
    private String proveUserName(){
        //Aufruf der Methode zur Pruefung des Nutzernamens
        return SERVERCONNECTION.proverUserName();
    }

    /**
     * Überschriebene Methode aus dem "UserComponent" Interface.
     * GUI der Bibliothek für den normalen Nutzer
     * @param stage Stage aus der "LoginGUI" Klasse
     */
    @Override
    public void userGUI(Stage stage) {
        //Fenstertitel der GUI
        stage.setTitle("Bibliothek");
        //Erstellen einer "Borderpane" mit jeweiligen Padding von 10
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));
        //Ertellen eines "Label" fuer das Willkommen heißen des Nutzers
        Label welcomeLabel = new Label(proveUserName());
        borderPane.setTop(welcomeLabel);
        //Erstellen eines "GridPane" ueber die Methode "createInputGrid"
        GridPane inputGrid = createInputGrid();
        //Hinzufuegen des "SearchButton" under des "BorrowedWindowButton" zur HBOX "buttons" mit Abstand von "0"
        HBox buttons = new HBox(0,createSearchButton(),createBorrowedWindowButton());
        //Hinzufuegen des "inputGrids" der Textfelder und der HBOX "buttons" zur VBOX "userLayout" mit Abstand von "10"
        VBox userLayout = new VBox(10, inputGrid, buttons);
        //Hinzufuegen der "VBOX" zur "BorderPane"
        borderPane.setCenter(userLayout);
        //Erstellen einer "TextArea" fuer Anzeigen der Suchergebnisse
        listResult = new ListView<>();
        //Hinzufuegen der "TextArea" zur "BorderPane"
        borderPane.setBottom(listResult);
        //Hinzufeugen der "BorderPane" zur "Scene" und der "Scene" zur "Stage"
        Scene scene = new Scene(borderPane, 800, 500);
        stage.setScene(scene);
        //Anzeigen der "Stage"
        stage.show();
    }

    /**
     * Erstellt ein GridPane für die Suchtextfelder und übergibt es an "userGUI()".
     * @return GridPane für die Textfelder.
     */
    private GridPane createInputGrid() {

        /*
        Erstellen eines "GridPane" mit einem Vertikalen und Horizontalen Abstand von 10 und einem
        oberen Padding von 10.
         */
        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);
        inputGrid.setPadding(new Insets(10, 0, 0, 0));
        //"Label" um den Nutzer zu verdeutlichen, dass an der Stelle Buecher ausgeliehen werden koennen.
        inputGrid.add(new Label("LEND BOOKS"), 0, 0, 2, 1);
        //"Label" und "Textfield" fuer den Namen des Buches
        inputGrid.add(new Label("Name:"), 0, 1);
        nameField = new TextField();
        inputGrid.add(nameField, 1, 1);
        //"Label" und "Textfield" fuer den Autor des Buches
        inputGrid.add(new Label("Author (please enter name and surname with a white space!):"), 0, 2);
        authorField = new TextField();
        inputGrid.add(authorField, 1, 2);
        //"Label" und "Textfield" fuer das Genre des Buches
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
     * Button um das zweite Fenster fuer die vom Client ausgeliehenen Buecher anzuzeigen.
     * @return Button zum anzeigen des zweiten Fensters.
     */
    private Button createBorrowedWindowButton(){

        Button windowButton = new Button("My Books");
        windowButton.setOnAction(event -> showBorrowedBooksWindow());
        return windowButton;
    }

    /**
     * Die Methode erstellt eine "BorrowedBooksGUI" Instanz, die fuer das zweite Fenster zustaendig ist
     * und ruft die Methode "show()" auf, die diese Klasse von "Stage" erbt.
     */
    private void showBorrowedBooksWindow(){

        BorrowedBooksGUI bbGui = new BorrowedBooksGUI();
        bbGui.show();
    }

    /**
     * Erstellt einen Button, der die Methode "searchBooks()" beim klicken aufruft.
     * @return übergibt Button zurück an "userGUI()".
     */

    private Button createSearchButton() {

        //Name des "Button"
        Button searchButton = new Button("Search");
        //Uebergabe der Eingegebenen Daten des Nutzers an die Methode "createBook" beim Klicken des "Button"
        searchButton.setOnAction(event ->
                createBook(nameField.getText(), authorField.getText(), genreField.getText(), isbnField.getText(),
                        releaseDate.getText())
        );
        return searchButton;
    }

    /**
     * Erstellt ein "Books" Objekt, aus den Eingabgen des Clients über die Setter der "Books" Klasse.
     * @param title Buchtitel
     * @param author Autorenname
     * @param genre Buchgenre
     * @param isbn ISBN-Nummer des Buches-
     * @return "Books" Objekt mit gesetzten Inhalten.
     */
    private void createBook(String title, String author, String genre, String isbn, String releaseDate){

        //Array, um Autoren Vor- und Nachname separat zu speichern
        String[] name = new String[2];
        if(!author.isEmpty()){
            //Trennt den Autorennamen in zwei seperate Teile, um Vorname und Nachname extra zu speichern
            name = author.split("\\s+");
        }
        //Setzen der Werte im Array "name" auf null, wenn kein Autorname vom CLient angegeben wurde
        else {
            name[0] = null;
            name[1] = null;
        }
        /*
        Erstellen eines neuen "Books" Objekts und aufrufen der Setter, um die
        vom Nutzer eingegebenen Daten einzufuegen
         */
        book = new Books();
        book.setTitle(title);
        book.setAuthorName(name[0]);
        book.setAuthorSurName(name[1]);
        book.setGenre(genre);
        book.setIsbn(isbn);
        book.setReleaseDate(releaseDate);
        //Uebergabe des erstellten "Books" Objekts an "sendBookInformation()"
        sendBookInformations(book);
        //Anzeigen der Suchergebnisse im Textfeld
        displaySearchResults();
    }

    /**
     * Die Methode zeigt die Suchergebnisse an, nach den Suchanfragen des Clients und laesst dem Client die
     * Buecher zum Ausleihen auswaehlen.
     */
    private void displaySearchResults(){

        // Lösche zuerst alle vorhandenen Einträge
        listResult.getItems().clear();

        // Füge Bücher zur Liste hinzu
        for (Books books : booksList) {
            //Hinzufuegen der Suchergebnisse aus der "booksList" zu "listResult"
            listResult.getItems().add(books);
        }
        //EventHandler zum Ausleihen der Buecher in der "ListView" beim Anklicken des jeweiligen Buches
        listResult.setOnMouseClicked(mouseEvent -> {

            if(!listResult.getSelectionModel().isEmpty()){
                Books select = listResult.getSelectionModel().getSelectedItem();
                lendBook(select);
            }
        });
    }

    /**
     * Die Methode ruft "lendBook()" in der Klasse "ServerConnection" auf, um zu ueberpruefen, ob das
     * Buch bereits von einem anderen Client ausgeliehen wurde.
     * Die Methode erhaehlt einen Wert (0 oder 1) von "lendBook()" der besagt, ob das Buch ausgeliehen werden kann (0)
     * oder schon von einem anderen Client ausgeliehen wird (1).
     * @param book Buch, das der Client ausleihen will.
     */
    private void lendBook(Books book){

        UserName userNameClass = UserName.getInstance();
        book.setBorrower(userNameClass.getUserName());
        int lendStatus = SERVERCONNECTION.lendBook(book);
        if(lendStatus == 0){
            System.out.println("Das Buch wurde ausgeliehen.");
        }
        else if(lendStatus == 1){
            System.out.println("Das Buch kann derzeit nicht ausgeliehen werden.");
        }
        else {
            System.err.println("Fehler bei der Datenübertragung.");
        }
    }



    /**
     * Übergibt "Books" Objekt an die "ServerConnection" Klasse
     * @param book Objekt
     */
    private void sendBookInformations(Books book){

        booksList = SERVERCONNECTION.searchBook(book); }



}
