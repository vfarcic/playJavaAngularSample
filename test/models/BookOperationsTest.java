package models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookOperationsTest {

    private BookOperations bookOperations = BookOperations.getInstance();
    private final Book book1 = new Book(1, "image1", "title1", "author1", 1.11, "/api/v1/books/1");
    private final Book book2 = new Book(2, "image2", "title2", "author1", 2.22, "/api/v1/books/2");
    private final Book unsavedBook = new Book(123, "myImage", "myTitle", "me, myself and I", 123.45, "google.com");

    @Before
    public void beforeBookOperationsTest() {
        Map<Integer, Book> booksMap = new HashMap<>();
        booksMap.put(2, book2);
        booksMap.put(1, book1);
        bookOperations.setBooksMap(booksMap);
    }

    @Test
    public void getInstanceReturnsAlreadyInstantiatedClass() {
        BookOperations anotherInstance = BookOperations.getInstance();
        assertThat(bookOperations, is(equalTo(anotherInstance)));
    }

    @Test
    public void getListShouldReturnListOfBooks() {
        List<Book> books = bookOperations.getList();
        assertThat(books, isA(List.class));
        assertThat(books, hasSize(2));
        assertThat(books.get(0), isA(Book.class));
    }

    @Test
    public void getListShouldReturnBookInAscendingOrder() {
        List<Book> books = bookOperations.getList();
        assertThat(books.get(0), equalTo(book1));
    }

    @Test
    public void getListReducedShouldReturnListOfBooks() {
        List<BookReduced> books = bookOperations.getListReduced();
        assertThat(books, isA(List.class));
        assertThat(books, hasSize(2));
        assertThat(books.get(0), isA(BookReduced.class));
    }

    @Test
    public void saveShouldSaveBook() {
        bookOperations.save(unsavedBook);
        assertThat(bookOperations.getList(), hasItem(unsavedBook));
    }

    @Test
    public void deleteShouldDeleteBook() {
        bookOperations.delete(book1.getId());
        assertThat(bookOperations.getList(), not(hasItem(book1)));
    }

    @Test
    public void getShouldRetrieveBook() {
        Book actual = bookOperations.get(book1.getId());
        assertThat(actual, is(equalTo(book1)));
    }

}
