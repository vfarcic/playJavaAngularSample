package models;

public class BookReduced {

    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private String link;
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public BookReduced(int id, String title, String link) {
        this.setId(id);
        this.setTitle(title);
        this.setLink(link);
    }
}
