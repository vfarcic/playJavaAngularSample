package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import models.Book;
import models.BookOperations;
import models.BookReduced;
import org.junit.*;
import scala.concurrent.duration.Duration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookAkkaTest {

    private static ActorSystem system;

    @Before
    public void beforeBookAkkaTest() {
        system = ActorSystem.create();
    }

    @After
    public void afterBookAkkaTest() {
        system.shutdown();
        system.awaitTermination(Duration.create("10 seconds"));
    }

    @Test
    public void listBooksShouldRespondWithListOfBooks() {
        new JavaTestKit(system) {{
            final ActorRef bookActor = system.actorOf(Props.create(BookAkka.BookActor.class), "bookActor");
            bookActor.tell(new BookAkka.ListBooks(), getTestActor());
            final BookAkka.Books books = expectMsgClass(BookAkka.Books.class);
            new Within(duration("10 seconds")) {
                protected void run() {
                    List<Book> booksList = books.books;
                    assertThat(booksList, isA(List.class));
                    assertThat(booksList, hasSize(greaterThan(0)));
                    assertThat(booksList.get(0), isA(Book.class));
                }
            };
        }};
    }

    @Test
    public void listBooksREducedShouldRespondWithReducedListOfBooks() {
        new JavaTestKit(system) {{
            final ActorRef bookActor = system.actorOf(Props.create(BookAkka.BookActor.class), "bookActor");
            bookActor.tell(new BookAkka.ListBooksReduced(), getTestActor());
            final BookAkka.BooksReduced books = expectMsgClass(BookAkka.BooksReduced.class);
            new Within(duration("10 seconds")) {
                protected void run() {
                    List<BookReduced> booksList = books.books;
                    assertThat(booksList, isA(List.class));
                    assertThat(booksList, hasSize(greaterThan(0)));
                    assertThat(booksList.get(0), isA(BookReduced.class));
                }
            };
        }};
    }

    @Test
    public void saveBookShouldSaveBook() {
        new JavaTestKit(system) {{
            String title = "myTestTitle";
            Book book = new Book(12345, "myTestImage", title, "myTestAuthor", 123.45, "myTestLink");
            final ActorRef bookActor = system.actorOf(Props.create(BookAkka.BookActor.class), "bookActor");
            bookActor.tell(new BookAkka.SaveBook(book), getTestActor());
            final BookAkka.BookAkkaStatus bookAkkaStatus = expectMsgClass(BookAkka.BookAkkaStatus.class);
            new Within(duration("10 seconds")) {
                protected void run() {
                    assertThat(BookOperations.getInstance().getList(), hasItem(book));
                }
            };
        }};
    }

    @Test
    public void saveBookShouldRespondWithStatus() {
        new JavaTestKit(system) {{
            String title = "myTestTitle";
            Book book = new Book(12345, "myTestImage", title, "myTestAuthor", 123.45, "myTestLink");
            final ActorRef bookActor = system.actorOf(Props.create(BookAkka.BookActor.class), "bookActor");
            bookActor.tell(new BookAkka.SaveBook(book), getTestActor());
            final BookAkka.BookAkkaStatus bookAkkaStatus = expectMsgClass(BookAkka.BookAkkaStatus.class);
            new Within(duration("10 seconds")) {
                protected void run() {
                    assertThat(bookAkkaStatus.status, is(equalTo("OK")));
                    assertThat(bookAkkaStatus.message, is(equalTo("Book " + title + " has been saved.")));
                }
            };
        }};
    }

}
