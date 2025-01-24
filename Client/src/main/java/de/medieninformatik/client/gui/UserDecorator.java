package de.medieninformatik.client.gui;

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
 * Abstrakte Klasse für das Dekoratormuster.
 * Implementiert das "UserComponent" Interface.
 */
abstract class UserDecorator implements UserComponent {

    //UserComponent Referenz
    protected UserComponent userComponent;

    /**
     * Dekorator erhählt "UserComponent" Referenz und setzt diese auf "userComponent".
     * @param userComponent
     */
    public UserDecorator(UserComponent userComponent){this.userComponent = userComponent;}

    /**
     * Überschriebene Klasse des "UserComponent" Interfaces.
     * Ruft "userGUI()" Methode der "userComponent" Instanz auf
     * @param stage Stage aus der "LoginGUI" Klasse
     */
    @Override
    public void userGUI(Stage stage) {
        userComponent.userGUI(stage);
    }
}
