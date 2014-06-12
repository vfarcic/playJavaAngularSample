package models;

public class Book {

    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String image;
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private String author;
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    private double price;
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    private String link;
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public Book() {}

    public Book(int id, String image, String title, String author, double price, String link) {
        this.setId(id);
        this.setImage(image);
        this.setTitle(title);
        this.setAuthor(author);
        this.setPrice(price);
        this.setLink(link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (Double.compare(book.price, price) != 0) return false;
        if (author != null ? !author.equals(book.author) : book.author != null) return false;
        if (image != null ? !image.equals(book.image) : book.image != null) return false;
        if (link != null ? !link.equals(book.link) : book.link != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
