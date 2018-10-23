package wolox.training.services;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import wolox.training.dao.BookDao;


public class OpenLibraryService {

    private static final String TITLE = "title";
    private static final String SUBTITLE = "subtitle";
    private static final String PUBLISHERS = "publishers";
    private static final String PUBLISH_DATE = "publish_date";
    private static final String NUMBER_OF_PAGES = "number_of_pages";
    private static final String AUTHORS = "authors";
    private static final String NAME = "name";

    private JSONObject json;


    public String convertJson(String isbn, String label) throws JSONException {
        return (json.getJSONObject("ISBN:" + isbn).getString(label));
    }

    public String convertJsonArray(String isbn, String label1, String label2) throws JSONException {
        return (json.getJSONObject("ISBN:" + isbn).getJSONArray(label1).getJSONObject(0).getString(label2));
    }

    public BookDao bookInfo(String isbn) throws JSONException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String bookInfo = restTemplate.getForObject("https://openlibrary.org/api/books?bibkeys=ISBN:{isbn}&format=json&jscmd=data", String.class, isbn);
        json = new JSONObject(bookInfo);
        BookDao book;

        if (json.length() == 0) {
            book = null;
        } else {
            book = new BookDao(isbn, convertJson(isbn, TITLE), convertJson(isbn, SUBTITLE), convertJson(isbn, PUBLISHERS), convertJson(isbn, PUBLISH_DATE), convertJson(isbn, NUMBER_OF_PAGES));
            book.getAuthors().add(convertJsonArray(isbn, AUTHORS, NAME));
        }

        return book;
    }

}
