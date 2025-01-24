package de.medieninformatik.client.gui;

import de.medieninformatik.client.ServerConnection;
import de.medieninformatik.common.Books;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

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
 * Die Klasse ist fuer das zweite Fenster, zum Anzeigen der vom Client ausgeliehenen Buecher, zustaendig.
 */
public class BorrowedBooksGUI extends Stage {

    //"TableView" zum Erstellen einer Tabelle im zweiten Fenster
    private TableView<Books> tableView;
    //Erstellen neuer "ServerConnection" Instanz
    private ServerConnection serverConnection = ServerConnection.getInstance();

    /**
     * Konstruktor, um das Fenster zu erstellen und die Methode zur Erstellung der Tabelle aufzurufen.
     */
    public BorrowedBooksGUI(){

        StackPane stackPane = new StackPane();
        //Erstellen des Fensters
        Scene scene = new Scene(stackPane, 600, 500);
        //Erhalten der Tabelle
        tableView = createTable();
        //Hinzufügen der Tabelle zum Fenster
        stackPane.getChildren().add(tableView);
        setScene(scene);
        //Titel für das Fenster
        setTitle("My Books");
    }

    /**
     * Erstellen einer Tabelle mit den Spalten "Books" und "Return Date".
     * @return erstellte Tabelle als "TableView".
     */
    private TableView<Books> createTable(){

        //Erstellen einer neuen "TableView"
        TableView<Books> createTableView = new TableView<>();
        //Erstellen der Spalte fuer die Buchtitel
        TableColumn<Books, String> bookColumn = new TableColumn<>("Books");
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        //Erstellen der Spalte fuer das Rueckgabedatum
        TableColumn<Books, String> returnColumn = new TableColumn<>("Return Date");
        returnColumn.setCellValueFactory(new PropertyValueFactory<>("ReturnDate"));
        //Erstellen der Spalte fuer die ISBN
        TableColumn<Books, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("Isbn"));
        //Erstellen der Spalte fuer das Datum der Veroefentlichung
        TableColumn<Books, String> releaseColumn = new TableColumn<>("Release_Date");
        releaseColumn.setCellValueFactory(new PropertyValueFactory<>("ReleaseDate"));
        //Erstellen der Spalte fuer den Autorenvornamen
        TableColumn<Books, String> surNameColumn = new TableColumn<>("Surname");
        surNameColumn.setCellValueFactory(new PropertyValueFactory<>("AuthorSurName"));
        //Erstellen fuer die Spalte fuer den Autorennachnamen
        TableColumn<Books, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("AuthorName"));
        //Erstellen fuer die Spalte fuer das Genre
        TableColumn<Books, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        //Hinzufuegen der Spalten zur "TableView"
        createTableView.getColumns().add(bookColumn);
        createTableView.getColumns().add(returnColumn);
        createTableView.getColumns().add(isbnColumn);
        createTableView.getColumns().add(releaseColumn);
        createTableView.getColumns().add(surNameColumn);
        createTableView.getColumns().add(nameColumn);
        createTableView.getColumns().add(genreColumn);
        //Erstellen einer "ObservableList"
        ObservableList<Books> observableList = FXCollections.observableArrayList();
        //Hinzufuegen von "observableList" zur "TableView"
        createTableView.setItems(observableList);
        //Erhalten der Ausgeliehenen Buecher aus "getUsersBooks()"
        observableList.setAll(getUsersBooks());
        //"ChangeListener", um Buecher in der Tabelle, als Client, auswaehlen zu koennen
        createTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldBook, newBook) -> {
            //Pruefen, ob ausgewaehlte Zeile ein "Books" Objekt enthealt
            if(newBook != null){
                System.out.println(newBook.getTitle());
                /*Uebergrabe des ausgewaehlten Buches als "Books" Objekt an die "returnBook()"
                 Methode der Klasse "ServerConnection"*/
               serverConnection.returnBook(newBook);
            }
        });
        return createTableView;
    }

    /**
     * Die Methode erhaehlt eine "List<Books>" aus "ServerConnection", um die ausgeliehenen Buecher des Clients,
     * in der Tabelle anzeigen lassen zu koennen.
     * @return "List<Books>" mit ausgeliehenen Buechern des Clients.
     */
    private List<Books> getUsersBooks(){

        //Rueckgabe der List<Book>" aus "ServerConnection"
        return serverConnection.getUsersBooks();
    }
}
