package de.medieninformatik.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.medieninformatik.common.Books;
import de.medieninformatik.server.database.ConnectDB;
import de.medieninformatik.server.database.UserNames;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
 * Die Klasse fungiert als "Endpoint" für den Server, sie empfängt die JSON-Daten der Clients und sendet
 * andere JSON-Daten vom Server an die Clients zurück.
 */
@Path("/connect")
public class Endpoint {

    private ConnectDB instance = ConnectDB.getInstance();

    /**
     * Die Methode erhaehlt eine Json Datei und wandelt diese in einen String um, darauf wird der erhaltene Nutzername
     * an die Klasse "Usernames" uebergeben, um zu pruefen, ob dieser schon existiert.
     * @param userName vom Client eingebener Nutzername
     * @return Antwort vom Server
     */
    @POST
    @Path("/proveUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userName(String userName){

        //Wandelt Json on einen String um.
        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(userName);
            userName = jn.get("userName").asText();
        }
        catch (JsonProcessingException e) {throw new RuntimeException(e);}
        //Pruefen auf Existenz des Nutzernamens
        boolean newUser = instance.proveUserNames(userName);

        if(newUser){
            //Antwort fuer neue Nutzer
            return Response.status(Response.Status.OK).entity("Welcome in the library " + userName).build();
        }
        else {
            //Antwort fuer bestehende Nutzer
            return Response.status(Response.Status.OK).entity("Welcome back " + userName).build();
        }
    }

    /**
     * Die Methode erhaelt eine JSON Datei vom Client, die die Suchanfragen des Clients enthaelt
     * und wandelt diese wieder in ein "Books" Objekt um und gibt dieses weiter an "ConnectDB"
     * @param jsonBook gesuchtes Buch vom Client
     */
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearch(String jsonBook){

        //Umwandeln der JSON in ein "Books" Objekt
        try {
            ObjectMapper om = new ObjectMapper();
            Books books = om.readValue(jsonBook, Books.class);
            //Auf Suche zutreffende Buecher
            List<Books> found = instance.searchBooks(books);
            //Umwandeln von "found" in String
            String jsonFound = om.writeValueAsString(found);
            //Senden der Suchtreffer als JSON an den Client
            return Response.status(Response.Status.OK).entity(jsonFound).build();

        } catch (JsonProcessingException e) {
            //Errormeldung vom Server, wenn die Suchanfrage nicht beantwortet werden konnte
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Die Methode erhaehlt den Nutzernamen des Clients als JSON und gibt ihn an die Klasse "ConnectDB" weiter.
     * Sie erhaelt eine "List<Books>", die sie wieder zurueck an den CLient sendet.
     * @param userName Nutzername des Clients
     * @return "List<Books>" wird als JSON an den Client gesendet.
     */
    @POST
    @Path("/getBooks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersBooks(String userName){

        //Wandelt Json on einen String um.
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jn = om.readTree(userName);
            userName = jn.get("userName").asText();
        }
        catch (JsonProcessingException e) {throw new RuntimeException(e);}
        //Uebergabe an "ConnectDB", um die Buecher von der Datenbank abzufragen
        List<Books> userBooksList = instance.getBooksFromUser(userName);

        try {
            //Erstellen der JSON Datei
            om = new ObjectMapper();
            String jsonBook = om.writeValueAsString(userBooksList);
            //Senden der "List" an den Client
            return Response.ok(jsonBook, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {throw new RuntimeException(e);}
    }

    /**
     * Die Methode erhaelt ein "Books" Objekt als JSON vom Client und gibt es weiter an die Datenbank
     * zur Ueberpruefung, ob das Buch ausgeliehen werden kann oder nicht.
     * @param jsonBook "Books" Objekt vom Client ausgewaehltes Buch.
     * @return Status, ob das Buch ausgeliehen wurde oder schon vorher ausgeliehen war.
     */
    @POST
    @Path("/lend")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response lendBook(String jsonBook){
        //Umwandeln der JSON in ein "Books" Objekt
        try {
            ObjectMapper om = new ObjectMapper();
            Books books = om.readValue(jsonBook, Books.class);
            //Weitergabe des ausgewaehlten Buches an die Datenbank
            int lendstatus = instance.lendBook(books);
            //Erstellen von JSON zum zuruecksenden des "lendStatus" an den Client
            ObjectNode on = om.createObjectNode();
            on.put("LEND", lendstatus);
            //Umwandeln von "lendStatus" in String zum Zurueck senden als JSON
            String response = om.writeValueAsString(on);
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Die Methode erhealt ein "Books" Objekt als JSON Datei
     * vom Client, welches das Buch beinhaltet, dass dieser zurueck geben will.
     * @param jsonBook Buch das zurueckgegeben werden soll.
     */
    @POST
    @Path("/return")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnBook(String jsonBook) {

        //Umwandeln der JSON in ein "Books" Objekt
        try {
            ObjectMapper om = new ObjectMapper();
            Books books = om.readValue(jsonBook, Books.class);
            //Uebergabe des zurueckgegebenen Buches an die Datenbank
            instance.returnBook(books);
            String answer = "Das Buch " + books.getTitle() + " wurde zurueckgegeben.";
            return Response.status(Response.Status.OK).entity(answer).build();
        } catch (JsonProcessingException e) {
            //Errormeldung, wenn ein Fehler bei der Rueckgabe des Buches aufgetreten ist.
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }



        /**
     * Die Methode erhaelt vom Admin-Client ein "Books" Objekt als JSON, dessen Inhalt in die Datenbank aufgenommen
     * werden soll.
     * @param jsonBook String im JSON Format mit vom Admin gesendeten Daten.
     * @return Antwort, ob die Daten in die Datenbank aufgenommen werden konnten.
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(String jsonBook){

        //Umwandeln der JSON in ein "Books" Objekt
        try {
            ObjectMapper om = new ObjectMapper();
            Books books = om.readValue(jsonBook, Books.class);
            //Uebergabe des Buches an die Datenbank
            //Antwort ueber erfolgreiche Uebergabe
            String answer = instance.addBooks(books);
            return Response.status(Response.Status.OK).entity(answer).build();
        } catch (JsonProcessingException e) {
            //Errormeldung vom Server, wenn das Buch nicht hinzugefuegt werden konnte
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBooks(String json){

        //Erhalten der ISBN-Nummer vom Admin
        String isbn;
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jsonNode = om.readTree(json);
            isbn = jsonNode.get("ISBN").textValue();
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String answer = instance.removeBooks(isbn);
        return Response.status(Response.Status.OK).entity(answer).build();
    }

    /**
     * Die Methode erhält einen Boolean als JSON Datei vom Client, um alle Tabellen in der Datenbank zu löschen.
     * @param json erhaltene JSON als String.
     * @return Antwort vom Server, ob die Löschung erfolgreich war oder nicht.
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTablesInDB(String json){

        //Wird beim Erhalten der Json Datei vom Client auf true gesetzt
        boolean del = false;
        //Erhalten des Booleanwerts aus dem Json Format
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode jsonNode = om.readTree(json);
            del = jsonNode.get("del").asBoolean();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //Uebergabe von "del" an "ConnectDB"
        String answer = instance.deleteTables(del);
        return Response.status(Response.Status.OK).entity(answer).build();
    }
}