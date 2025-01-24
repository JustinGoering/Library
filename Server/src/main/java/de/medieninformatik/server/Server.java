package de.medieninformatik.server;

import jakarta.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.UncheckedIOException;

import java.net.URI;

import java.util.Scanner;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die "Server" Klasse wird von der "Main" Klasse aus aufgerufen und startet den Server und und kann diesen auch
 * wieder herunterfahren.
 */
public class Server {

    //Instanz der "Server" Klasse
    private static Server instance;

    //URI des Servers
    private static final URI BASEURI = UriBuilder.newInstance()
            .scheme("http")
            .host("127.0.0.1")
            .port(8080)
            .build();

    /**
     * Privater Konstruktor, um Zugriff von Außen zu vermeiden
     */
    private Server(){}

    /**
     * Getter, der eine Instanz der Klasse "Server" erstellt und diese an andere Klassen uebergibt.
     * @return Instanz der Klasse "Server".
     */
    public static Server getInstance(){

        //Erstellt neue Instanz, wenn "instance" null ist
        if(instance == null){
            instance = new Server();
        }
        return instance;
    }

    /**
     * Methode zum Starten des Grizzly-Servers.
     */
    public void startServer(){

        try {
            HttpServer server = getConfig();
            server.start();
            System.out.println("REST-WEB-Anwendung wurde auf " + BASEURI + " gestartet.");
            shutDown(server);
        }
        catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Konfiguration des Servers und herstellen der Verbindung mit der Klasse "Entpoint".
     * @return HTTP Grizzly-Server mit der "BASEURI"
     */
    private HttpServer getConfig(){

        //Verbindung zur Klasse "Endpoint"
        ResourceConfig config = new ResourceConfig(Endpoint.class);

        return GrizzlyHttpServerFactory.createHttpServer(BASEURI, config);
    }

    /**
     * Die Methode dient dem Herunterfahren des Servers beim Schreiben von "exit" in die Konsole.
     * @param server
     */
    private void shutDown(HttpServer server){

        System.out.println("Schreibe \"exit\" in die Konsole, um den Server zu beenden.");
        //Scanner liest Eingaben von der Konsole ein
        try (Scanner scanner = new Scanner(System.in)) {
            //Endlosschleife, wartet auf Eingabe und bricht nach dieser ab
            while (true) {
                String input = scanner.nextLine();
                //Pruefen der Eingabe mit der geforderten Eingabe, ignorieren von Groß-/Kleinschreibung
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Der Server wird heruntergefahren.");
                    //Herunterfahren des Servers
                    server.shutdownNow();
                    break;
                }
            }
        }
    }
}
