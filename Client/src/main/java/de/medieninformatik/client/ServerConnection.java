package de.medieninformatik.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import de.medieninformatik.common.Books;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Justin Göring, m30108
 * @date 2023-11-29
 * @version 1.0
 * Programmierung 03 Hausarbeit
 * Thema: Implementierung einer REST-Anwendung für eine Bibliothek mit eigener Datenbank und Klienten, die
 * Bücher ausleihen und zurückgeben können, sowie ein Admin-Klient, der Bücher zur Datenbank hinzufügen kann.
 */

/**
 * Die Klasse stellt eine Verbindung zwischen den CLients und dem Server her, um Daten an diesen zu senden
 * und empfangen zu können.
 */
public class ServerConnection {

    private static ServerConnection instance;
    //Server URI
    private final String URI = "http://127.0.0.1:8080";
    //Pfad zur Endpoint Klasse auf der Serverseite
    private final String ENDPOINTCLASS = "/connect";

    /**
     * Privater Konstruktor fuer Singleton - Muster
     */
    private ServerConnection(){}

    public static ServerConnection getInstance(){

        //Erzeugt neue Instanz der Klasse, wenn "instance" null ist
        if(instance == null){
            instance = new ServerConnection();
        }
        return instance;
    }

    /**
     * Die Klasse uebergibt den vom Client angegebenen Nutzernamen an die Methode "getBufferedReader()" und
     * wandelt den Rueckgabewert in einen String um.
     * @return Antwort vom Server, ob der Nutzername vorhanden ist oder nicht als String.
     */
    public String proverUserName(){

        //Pfad zur Methode in der Klasse "Endpoint", die den Nutzernamen prueft
        String methodPath = "/proveUser";

        try {
            //Erstellen der URL aus URI und den Pfaden zur Klasse "Endpoint" und zur Methode "userName"
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);

            //Aufrufen der "getBufferedReader()" Methode und Uebergabe der URL
            BufferedReader br = getBufferedReader(url);
            StringBuilder response = new StringBuilder();
            String responseLine;

                //Einfuegen des Inhalts aus "br" in "responseLine"
                while ((responseLine = br.readLine()) != null) {

                    //Entfernen von fuehrenden und abschließenden Leerzeichen und Uebergabe an "response"
                    response.append(responseLine.trim());
                }
                return response.toString();
        }
        catch (IOException e) {throw new RuntimeException(e);}
    }

    /**
     * Senden des Nutzernamens als JSON an den Server, um zu ueberpruefen, ob dieser
     * schon vorhanden ist oder nicht.
     * @param url URL zur Methode in der "Entpoint" Klasse
     * @return Antwort vom Server
     * @throws IOException
     */
    private BufferedReader getBufferedReader(URL url) throws IOException {

        //Stellt Verbindung zum Server mit der URL "url" her
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();

        //Anfrage an "POST" Methode
        connect.setRequestMethod("POST");
        //Laesst Output zu
        connect.setDoOutput(true);
        //Senden als JSON Datei
        connect.setRequestProperty("Content-Type", "application/json");
        UserName userNameClass = UserName.getInstance();
        //Erstellt ein JSON Objekt aus dem Nutzernamen
        ObjectMapper om = new ObjectMapper();
        String jsonInputString = om.writeValueAsString(Map.of("userName", userNameClass.getUserName()));

        //Senden an den Server mit einem "OutputStream"
        OutputStream os = connect.getOutputStream();
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);

        //Erhalten der Antwort vom Server mit "InputStream"
        BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(), "utf-8"));
        return br;
    }

    /**
     * Die Methode erhaehlt ein "Books" Objekt, dass ein Client mit seiner Suche erstellt und sendet
     * dieses an den Server.
     * @param books vom CLient gesuchtes Buch.
     * @return "List" mit allen Buechern aus der Datenbank, die auf das Objekt "books" zutreffen.
     */
    public List<Books> searchBook(Books books){

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/search";
        List<Books> booksList = new ArrayList<>();

        try {
            //Erstellen der URL
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //"books" in String umwandeln
            ObjectMapper om = new ObjectMapper();
            String jsonBook = om.writeValueAsString(books);
            //senden von "jsonBook" als JSON
            OutputStream ops = connect.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            bw.write(jsonBook);
            bw.flush();
            //Erhalten von ResponseCode, um zu wissen, ob eine Verbindung aufgebaut werden konnte
            int responseCode =connect.getResponseCode();
            //Verbindung erfolgreich, wenn "responseCode" = 200
            if(responseCode == HttpURLConnection.HTTP_OK){
                //Lesen der vom Server erhaltenen JSON
                InputStream ips = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                //Umwandeln in List
                CollectionType list = om.getTypeFactory().constructCollectionType(List.class,Books.class);
                //Speichern der Daten vom Server in "booksList"
                booksList = om.readValue(br, list);
            }
        }
        catch (IOException e) {throw new RuntimeException(e);}
        return booksList;
    }

    /**
     * Die Methode erhaelt eine "List<Books>" als JSON vom Server, mit den ausgeliehenen Buechern des Clients.
     * @return "List<Books>" mit ausgeliehenen Buechern.
     */
    public List<Books> getUsersBooks(){

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/getBooks";
        List<Books> booksList = new ArrayList<>();

        try {
            //Erstellen der URL
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //Erhalten des Nutzernamens des Clients aus "UserName" Klasse
            UserName userNameClass = UserName.getInstance();
            //"Username" in als JSON Datei senden
            ObjectMapper om = new ObjectMapper();
            String jsonUserName = om.writeValueAsString(Map.of("userName", userNameClass.getUserName()));
            OutputStream ops = connect.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            bw.write(jsonUserName);
            bw.flush();
            //Erhalten von ResponseCode, um zu wissen, ob eine Verbindung aufgebaut werden konnte
            int responseCode = connect.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //Lesen der vom Server erhaltenen JSON
                InputStream ips = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                //Umwandeln in List
                CollectionType list = om.getTypeFactory().constructCollectionType(List.class,Books.class);
                //Speichern der Daten vom Server in "booksList"
                booksList = om.readValue(br, list);
            } else {
                System.out.println("Fehler beim Abrufen der Bücher. Response Code: " + responseCode);
            }
            connect.disconnect();
        } catch (IOException e) {e.printStackTrace();}
        return booksList;
    }

    /**
     * Die Methode sendet ein "Books" Objekt als JSON an der Server, um die Rueckgabe des Buches vom Client
     * in der Datenbank zu verzeichnen.
     * @param books Vom Client zurueck gegebenes Buch als "Books" Objekt
     */
    public void returnBook(Books books) {

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/return";
        try {
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //"books" in String umwandeln
            ObjectMapper om = new ObjectMapper();
            String jsonBook = om.writeValueAsString(books);
            //senden von "jsonBook" als JSON
            OutputStream ops = connect.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            bw.write(jsonBook);
            bw.flush();
            //Schließen des "OutPutStream"
            ops.close();
            //Schließen des "BufferedWriter"
            bw.close();
            //Erhalten der Antwort vom Server
            int responseCode = connect.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //Lesen der Antwort
                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                //Erstellen eines Strings aus der empfangenen Serverantwort
                StringBuilder sb = new StringBuilder();
                String read;

                while ((read = br.readLine()) != null) {
                    sb.append(read);
                }
                String serverAnswer = sb.toString();
                System.out.println(serverAnswer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Die Methode stellt eine Verbindung zum Server her und sendet ein "Books" Objekt als JSON an diesen, um
     * es in die Datenbank aufnehmen zu koennen.
     * @param books Buch, das in die Datenbank aufgenommen werden soll.
     * @return Antwort, ob das Buch in die Datenbank aufgenommen werden konnte.
     */
    public String addBooks(Books books){

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/add";
        try {
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //"books" in String umwandeln
            ObjectMapper om = new ObjectMapper();
            String jsonBook = om.writeValueAsString(books);
            //senden von "jsonBook" als JSON
            OutputStream ops = connect.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            bw.write(jsonBook);
            bw.flush();
            //Erhalten der Antwort vom Server
            int responseCode = connect.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //Lesen der Antwort
                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                //Erstellen eines Strings aus der empfangenen Serverantwort
                StringBuilder sb = new StringBuilder();
                String read;
                while((read = br.readLine()) != null){
                    sb.append(read);
                }
                //Uebergabe des Inhalts vom "Stringbuilder" zu "answer" und Rueckgabe an "AdminDecorator"
                return sb.toString();
            }
            else {
                //Rueckgabe des Fehlercodes an "AdminDecorator"
                return "Fehler bei der Anfrage: " + responseCode;}

        } catch (IOException e) {throw new RuntimeException(e);}

    }

    /**
     * Die Methode sendet die vom Admin eingegebene ISBN-Nummer dem Server, um das zugehoerige Buch aus der
     * Datenbank zu entfernen.
     * @param isbn ISBN-Nummer des zu entfernenden Buches.
     */
    public String removeBook(String isbn){

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/remove";
        String answer = "Aktion wurde nicht ausgeführt";

        try {
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //String im JSON Format
            String json = "{\"ISBN\": " + "\"" + isbn + "\"" + "}";
            //Senden des Boolean
            OutputStream os = connect.getOutputStream();
            byte[] arr = json.getBytes(StandardCharsets.UTF_8);
            os.write(arr,0, arr.length);
            //Erhalten der Antwort vom Server
            int responseCode = connect.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //Lesen der Antwort
                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                //Erstellen eines Strings aus der empfangenen Serverantwort
                StringBuilder sb = new StringBuilder();
                String read;
                while((read = br.readLine()) != null){
                    sb.append(read);
                }
                //Uebergabe des Inhalts vom "Stringbuilder" zu "answer"
                answer = sb.toString();
                return answer;
            }
            else {System.out.println("Fehler bei der Anfrage: " + responseCode);}

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

    /**
     * Die Methode sendet ein "Books" Objekt als JSON zum Server und erhaehlt eine Antwort (0 oder 1) vom Server,
     * die besagt, ob das Buch ausgeliehen werden kann.
     * @param book Buch, das der Client ausleihen will.
     * @return Integer 0, wenn es ausgeliehen werden kann, Integer 1, wenn es bereits ausgeliehen wurde.
     */
    public int lendBook(Books book){

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/lend";
        //Rueckgabewert
        int lendAnswer = -1;

        try {
            //Erstellen der URL
            URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //"book" in String umwandeln
            ObjectMapper om = new ObjectMapper();
            String jsonBook = om.writeValueAsString(book);
            //senden von "jsonBook" als JSON
            OutputStream ops = connect.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            bw.write(jsonBook);
            bw.flush();
            //Erhalten von ResponseCode, um zu wissen, ob eine Verbindung aufgebaut werden konnte
            int responseCode =connect.getResponseCode();
            //Verbindung erfolgreich, wenn "responseCode" = 200
            if(responseCode == HttpURLConnection.HTTP_OK){
                //Lesen der vom Server erhaltenen JSON
                InputStream ips = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                //Erhalten des Integers aus dem erhaltenen JSON
                JsonNode jsonNode = om.readTree(br);
                lendAnswer = jsonNode.get("LEND").asInt();
            }
        }
        catch (IOException e) {throw new RuntimeException(e);}
        return lendAnswer;
    }

    /**
     * Die Methode sendet einen boolean Wert an den Server, um die Tabellen der Datenbank zu loeschen.
     * @return Antwort vom Server
     */
    public String deleteTablesInDB(){

        //Pfad zur Methode in "Endpoint" Klasse
        String methodPath = "/delete";
        //Booleanwert, der an den Server gesendet werden soll
        boolean del = true;
        //Erstellen der URL
        String answer = "Aktion wurde nicht ausgeführt";
        try {
           URL url = new URL(URI + ENDPOINTCLASS + methodPath);
            //Stellt Verbindung zum Server mit der URL "url" her
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            //Anfrage an "POST" Methode
            connect.setRequestMethod("POST");
            //Laesst Output zu
            connect.setDoOutput(true);
            //Senden als JSON Datei
            connect.setRequestProperty("Content-Type", "application/json");
            //String im JSON Format
            String json = "{\"del\": " + del + "}";
            //Senden des Boolean
            OutputStream os = connect.getOutputStream();
            byte[] arr = json.getBytes(StandardCharsets.UTF_8);
            os.write(arr,0, arr.length);
            //Erhalten der Antwort vom Server
            int responseCode = connect.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //Lesen der Antwort
                InputStream is = connect.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                //Erstellen eines Strings aus der empfangenen Serverantwort
                StringBuilder sb = new StringBuilder();
                String read;
                while((read = br.readLine()) != null){
                    sb.append(read);
                }
                //Uebergabe des Inhalts vom "Stringbuilder" zu "answer"
                answer = sb.toString();
                return answer;
            }
            else {System.err.println("Fehler bei der Anfrage: " + responseCode);}

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return answer;
    }
}
