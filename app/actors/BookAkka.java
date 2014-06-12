package actors;

import akka.actor.UntypedActor;
import models.Book;
import models.BookOperations;
import models.BookReduced;

import java.io.Serializable;
import java.util.List;

public class BookAkka {

    public static class SaveBook implements Serializable {
        public final Book book;
        public SaveBook(Book book) {
            this.book = book;
        }
    }
    public static class ListBooks implements Serializable {}
    public static class ListBooksReduced implements Serializable {}
    public static class Books implements Serializable {
        public final List<Book> books;
        public Books(List<Book> books) {
            this.books = books;
        }
    }
    public static class BooksReduced implements Serializable {
        public final List<BookReduced> books;
        public BooksReduced(List<BookReduced> books) {
            this.books = books;
        }
    }
    public static class BookAkkaStatus {
        public final String status;
        public final String message;
        public BookAkkaStatus(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public static class BookActor extends UntypedActor {
        @Override
        public void onReceive(Object message) {
            BookOperations bookOperations = BookOperations.getInstance();
            if (message instanceof ListBooks) {
                getSender().tell(new Books(bookOperations.getList()), getSelf());
            } else if (message instanceof ListBooksReduced) {
                getSender().tell(new BooksReduced(bookOperations.getListReduced()), getSelf());
            } else if (message instanceof SaveBook) {
                SaveBook saveBook = (SaveBook) message;
                bookOperations.save(saveBook.book);
                getSender().tell(
                        new BookAkkaStatus("OK", "Book " + saveBook.book.getTitle() + " has been saved."),
                        getSelf()
                );
            }
        }
    }

}