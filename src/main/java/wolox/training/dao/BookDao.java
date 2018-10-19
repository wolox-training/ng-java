package wolox.training.dao;


import java.util.ArrayList;

public class BookDao {
    private String isbn;
    private String title;
    private String subtitle;
    private String publishers;
    private String publish_Date;
    private String numberOfPages;
    private ArrayList<String> authors = new ArrayList<String>();


    public BookDao(String isbn, String title, String subtitle, String publishers, String publish_Date, String numbreOfPages) {
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.publishers = publishers;
        this.publish_Date = publish_Date;
        this.numberOfPages = numbreOfPages;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

    public String getPublish_Date() {
        return publish_Date;
    }

    public void setPublish_Date(String publish_Date) {
        this.publish_Date = publish_Date;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numbreOfPages) {
        this.numberOfPages = numbreOfPages;
    }


    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }


    @Override
    public String toString() {
        return "BookDao{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", publishers='" + publishers + '\'' +
                ", publish_Date='" + publish_Date + '\'' +
                ", numberOfPages='" + numberOfPages + '\'' +
                ", authors=" + authors +
                '}';
    }
}
