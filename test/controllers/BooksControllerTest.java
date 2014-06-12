package controllers;

import actors.BookAkka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Book;
import models.BookOperations;
import org.junit.Before;
import org.junit.Test;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import play.mvc.Result;
import play.test.FakeRequest;

import java.util.List;

import static play.test.Helpers.*;
import static play.mvc.Http.MimeTypes.*;
import static play.mvc.Http.Status.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BooksControllerTest {

    private String book1Url = "/api/v1/books/1";
    Book book1 = new Book(1, "image1", "title1", "author1", 1.11, "/api/v1/books/1");
    JsValue book1Json;
    ObjectMapper mapper = new ObjectMapper();


    @Before
    public void beforeBooksControllerTest() throws JsonProcessingException {
        book1Json = Json.parse(mapper.writeValueAsString(book1));
        callAction(
                controllers.routes.ref.BooksController.save(),
                new FakeRequest(PUT, "/api/v1/books").withJsonBody(book1Json)
        );
    }

    @Test
    public void getRootShouldRespondWithHtml() {
        Result result = callAction(
                controllers.routes.ref.BooksController.index(),
                new FakeRequest(GET, "/")
        );
        assertThat(contentType(result), is(equalTo(HTML)));
        assertThat(status(result), is(equalTo(OK)));
    }

    @Test
    public void getApiV1BooksShouldRespondWithJson() {
        Result result = callAction(
                controllers.routes.ref.BooksController.listAll(),
                new FakeRequest(GET, "/api/v1/books")
        );
        assertThat(contentType(result), is(equalTo(JSON)));
        assertThat(status(result), is(equalTo(OK)));
    }

    @Test
    public void getApiV1BooksShouldReturnAllBooksReduced() throws JsonProcessingException {
        Result result = callAction(
                controllers.routes.ref.BooksController.listAll(),
                new FakeRequest(GET, "/api/v1/books")
        );
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(BookOperations.getInstance().getListReduced());
        assertThat(contentAsString(result), is(equalTo(json)));
    }

    @Test
    public void getApiV1BooksIdShouldRespondWithJson() {
        Result result = callAction(
                controllers.routes.ref.BooksController.get(book1.getId()),
                new FakeRequest(GET, "/api/v1/books/" + book1.getId())
        );
        assertThat(contentType(result), is(equalTo(JSON)));
        assertThat(status(result), is(equalTo(OK)));
    }

    @Test
    public void getApiV1BooksIdShouldReturnSpecifiedBook() throws JsonProcessingException {
        Result result = callAction(
                controllers.routes.ref.BooksController.get(book1.getId()),
                new FakeRequest(GET, book1Url)
        );
        assertThat(status(result), is(equalTo(OK)));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(BookOperations.getInstance().get(1));
        assertThat(contentAsString(result), is(equalTo(json)));
    }

    @Test
    public void deleteApiV1BooksIdShouldRespondWithJson() {
        Result result = callAction(
                controllers.routes.ref.BooksController.delete(book1.getId()),
                new FakeRequest(DELETE, book1Url)
        );
        assertThat(contentType(result), is(equalTo(JSON)));
        assertThat(status(result), is(equalTo(OK)));
    }

    @Test
    public void deleteApiV1BooksIdShouldRemoveSpecifiedBook() {
        Result result = callAction(
                controllers.routes.ref.BooksController.delete(book1.getId()),
                new FakeRequest(DELETE, book1Url)
        );
        assertThat(status(result), is(equalTo(OK)));
        List<Book> books = BookOperations.getInstance().getList();
        assertThat(books, not(hasItem(book1)));
    }

    @Test
    public void putApiV1BooksShouldRespondWithJson() throws JsonProcessingException {
        Result result = callAction(
                controllers.routes.ref.BooksController.save(),
                new FakeRequest(PUT, "/api/v1/books").withJsonBody(book1Json)
        );
        assertThat(contentType(result), is(equalTo(JSON)));
        assertThat(status(result), is(equalTo(OK)));
    }

    @Test
    public void putApiV1BooksShouldAddNewBook() throws JsonProcessingException {
        Book unsavedBook = new Book(123, "image123", "title123", "author123", 123.45, "/api/v1/books/123");
        JsValue unsavedBookJson = Json.parse(mapper.writeValueAsString(unsavedBook));
        Result result = callAction(
                controllers.routes.ref.BooksController.save(),
                new FakeRequest(PUT, "/api/v1/books").withJsonBody(unsavedBookJson)
        );
        assertThat(status(result), is(equalTo(OK)));
        List<Book> books = BookOperations.getInstance().getList();
        assertThat(books, hasItem(unsavedBook));
    }

    @Test
    public void putApiV1BooksShouldUpdateExistingBook() throws JsonProcessingException {
        book1.setTitle("This is updated title");
        book1Json = Json.parse(mapper.writeValueAsString(book1));
        int size = BookOperations.getInstance().getList().size();
        Result result = callAction(
                controllers.routes.ref.BooksController.save(),
                new FakeRequest(PUT, "/api/v1/books").withJsonBody(book1Json)
        );
        assertThat(status(result), is(equalTo(OK)));
        List<Book> books = BookOperations.getInstance().getList();
        assertThat(books, hasItem(book1));
        assertThat(books, hasSize(size));
    }

    @Test
    public void putApiV1BooksShouldRespondWithBadRequestWhenJsonIsIncorrect() throws JsonProcessingException {
        int size = BookOperations.getInstance().getList().size();
        Result result = callAction(
                controllers.routes.ref.BooksController.save(),
                new FakeRequest(PUT, "/api/v1/books").withJsonBody(Json.parse("{\"xxx\": \"123\"}"))
        );
        assertThat(contentType(result), is(equalTo(JSON)));
        assertThat(status(result), is(equalTo(BAD_REQUEST)));
    }

}

