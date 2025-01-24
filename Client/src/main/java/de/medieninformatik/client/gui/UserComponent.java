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
 * Interface für das Dekoratormuster, um den "User" um die Rechte des Admins erweitern zu können.
 */
public interface UserComponent {

    /**
     * Methode, um die GUI nach dem Login eines Clients zu aktualisieren.
     * @param stage Stage aus die "LoginGUI" Klasse
     */
    void userGUI(Stage stage);
}
