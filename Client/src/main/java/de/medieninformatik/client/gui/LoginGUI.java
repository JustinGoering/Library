package de.medieninformatik.client.gui;

import de.medieninformatik.client.ProveUserName;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

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
 * Die Klasse erbt von "Application" und implementiert die "start" Methode, um eine JavaFX GUI erzeugen zu können.
 */
public class LoginGUI extends Application {

    /**
     * Die Überschriebene "start" Methode von "Application", setzt die GUI für das Login Fenster.
     * @param stage Stage, auf die die Application Szene gesetzt wird.
     */
    @Override
    public void start(Stage stage) {

        //Fenstertitel
        stage.setTitle("Login");

        //Label für das Textfeld, in das der Benutzername eingegeben werden soll
        Label usernameLabel = new Label("Benutzername:");
        //Textfeld für den Benutzernamen
        TextField usernameTextField = new TextField();

        //Button zum Absenden des Benutzernamens
        Button submitButton = new Button("Submit");
        //Methode, die beim klicken auf den Button oder drücken von "Enter" aufgerufen wird
        buttonAction(usernameTextField, submitButton, stage);

        //Layout-Container
        VBox vbox = new VBox(10); // 10 Pixel Abstand zwischen den Elementen
        vbox.getChildren().addAll(usernameLabel, usernameTextField, submitButton);

        //Erstellen der Szene
        Scene scene = new Scene(vbox, 300, 150);

        //Hinzufügen der Szene zur Stage
        stage.setScene(scene);

        //Fenster anzeigen
        stage.show();
    }

    /**
     * Die Methode gibt den Nutzernamen und die Stage an "ProveUserName" weiter, sowie
     * implementiert die Möglichkeit, den "submitButton" mit der "Enter" Taste ausführen zu können.
     * @param userNameTextField Eingabe des Clients in das Textfeld für den Benutzernamen
     * @param submitButton Button aus der "start()" Methode
     * @param stage Stage aus der "start()" Methode
     */
    private void buttonAction(TextField userNameTextField, Button submitButton, Stage stage){

        //ProveUserName Instanz
        ProveUserName pun = new ProveUserName();
        //Übergabe an die Methode "prove()" Methode beim Auslösen des "submitButton"
        submitButton.setOnAction(e -> pun.prove(userNameTextField.getText(), stage));

        //KeyEvent-Handler um "submitButton" mit der "Enter" Taste auslösen zu können
        userNameTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                // Nur Buchstaben und Zahlen zulassen, andere Tasten (z.B., Enter) werden nicht bearbeitet
            } else if (event.getCode().getName().equals("Enter")) {
                // Wenn Enter gedrückt wird, die Aktion des Buttons auslösen
                submitButton.fire();
            } else {
                // Andere Tasten blockieren
                event.consume();
            }
        });
    }
}