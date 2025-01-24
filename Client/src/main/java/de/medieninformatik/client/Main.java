package de.medieninformatik.client;

import javafx.application.Application;

import de.medieninformatik.client.gui.LoginGUI;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Main Klasse der CLients, beinhaltet die "main" Methode.
 */

public class Main {

    /**
     * Die Methode ruft die Klasse zum Start der GUI für das Login Fenster auf.
     * @param args
     */
    public static void main(String[] args) {Application.launch(LoginGUI.class);}
}