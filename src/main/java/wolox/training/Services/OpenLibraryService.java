package wolox.training.Services;


import org.json.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import wolox.training.dao.BookDao;


public class OpenLibraryService {

    private JSONObject  json;


    public BookDao bookInfo(String isbn) throws JSONException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String bookInfo = restTemplate.getForObject("https://openlibrary.org/api/books?bibkeys=ISBN:{isbn}&format=json&jscmd=data", String.class,isbn);
        json = new JSONObject(bookInfo);



        String title = json.getJSONObject("ISBN:"+isbn).getString("title");
        String subtitle = json.getJSONObject("ISBN:"+isbn).getString("subtitle");
        String publishers= json.getJSONObject("ISBN:"+isbn).getString("publishers");
        String publish_date = json.getJSONObject("ISBN:"+isbn).getString("publish_date");
        String number_of_pages= json.getJSONObject("ISBN:"+isbn).getString("number_of_pages");
        String authors = json.getJSONObject("ISBN:"+isbn).getJSONArray("authors").getJSONObject(0).getString("name");

        BookDao book = new BookDao(isbn,title, subtitle,publishers,publish_date,number_of_pages);
        book.getAuthors().add(authors);

        return book;

    }

}
