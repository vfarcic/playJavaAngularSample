package controllers;

import actors.BookAkka;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Book;
import models.BookOperations;
import play.mvc.*;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static actors.BookAkka.*;

import static play.mvc.Http.MimeTypes.*;

public class BooksController extends Controller {

    final static ActorSystem system = ActorSystem.create("akkaSystem");
    final static ActorRef bookAkka = system.actorOf(Props.create(BookActor.class), "bookAkka");

    public static Result index() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("public/html/index.html")));
        return ok(content).as(HTML);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result listAll() throws JsonProcessingException {
        final Inbox inbox = Inbox.create(system);
        inbox.send(bookAkka, new ListBooksReduced());
        BooksReduced booksReduced = (BooksReduced) inbox.receive(Duration.create(10, TimeUnit.SECONDS));
        return getOkFromObject(booksReduced.books);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result save() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode bookJson = request().body().asJson();
        try {
            Book book = mapper.readValue(bookJson.toString(), Book.class);
            final Inbox inbox = Inbox.create(system);
            inbox.send(bookAkka, new SaveBook(book));
            BookAkkaStatus status = (BookAkkaStatus) inbox.receive(Duration.create(10, TimeUnit.SECONDS));
            return getOkFromObject(status);
        } catch(Exception e) {
            BookAkkaStatus status = new BookAkkaStatus("KO", e.getMessage());
            String json = mapper.writeValueAsString(status);
            return badRequest(json).as(JSON);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result get(int id) throws JsonProcessingException {
        return getOkFromObject(BookOperations.getInstance().get(id));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result delete(int id) throws JsonProcessingException {
        BookOperations.getInstance().delete(id);
        return getOkFromObject(new BookAkkaStatus("OK", "Book " + id + " has been removed"));
    }

    private static Status getOkFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        return ok(json).as(JSON);
    }

}
