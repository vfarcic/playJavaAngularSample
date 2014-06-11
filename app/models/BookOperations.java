package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookOperations {

    private Map<Integer, Book> booksMap;
    protected void setBooksMap(Map<Integer, Book> booksMap) {
        this.booksMap = booksMap;
    }
    protected Map<Integer, Book> getBooksMap() {
        if (booksMap == null) {
            booksMap = new HashMap<>();
            IntStream.range(10, 99).forEach(index -> {
                Book book = new Book(
                        index,
                        "image" + index,
                        "title" + index,
                        "author" + index,
                        index,
                        "/api/v1/books/" + index
                );
                booksMap.put(index, book);
            });
        }
        return booksMap;
    }

    public List<Book> getList() {
       return new ArrayList<>(this.getBooksMap().values());
    }

    public List<BookReduced> getListReduced() {
        return this.getList()
                .stream()
                .map(book -> new BookReduced(book.getId(), book.getTitle(), book.getLink()))
                .collect(Collectors.toList());
    }

    public void save(Book book) {
        this.getBooksMap().put(book.getId(), book);
    }

    public void delete(int id) {
        this.getBooksMap().remove(id);
    }

    public Book get(int id) {
        return this.getBooksMap().get(id);
    }

}
