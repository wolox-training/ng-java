package wolox.training.controllers;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.dao.BookDao;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  private OpenLibraryService serviceLibrary = new OpenLibraryService();


  @GetMapping("allBooks")
  public List<Book> getAllBooks() {
    return (List<Book>) bookRepository.findAll();

  }


  @GetMapping("/findAllBooks/pgy/{publisher}/{genre}/{year}")
  public List<Book> findByPublisherAndGenreAndYear(@PathVariable String publisher,
      @PathVariable String genre, @PathVariable String year) {
    return bookRepository.findByPublisherAndGenreAndYear(publisher, genre, year,
        PageRequest.of(0, 4, Sort.Direction.ASC, "title")).getContent();
  }

  @GetMapping("findBook/{id}")
  public Book findOne(@PathVariable Long id) {
    return bookRepository.findById(id)
        .orElseThrow(RuntimeException::new);
  }

  @PostMapping("create")
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }


  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("admin/delete/{id}")
  public void delete(@PathVariable Long id) {
    bookRepository.findById(id)
        .orElseThrow(RuntimeException::new);
    bookRepository.deleteById(id);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("admin/update/{id}")
  public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
    if (book.getId() != id) {
      throw new RuntimeException();
    }
    bookRepository.findById(id)
        .orElseThrow(RuntimeException::new);
    return bookRepository.save(book);
  }

  @GetMapping("findLocal/{isbn}")
  public Book findOneWithISBN(@PathVariable String isbn) {
    return bookRepository.findOneByIsbn(isbn);
  }

  @PostMapping("findLibrary/{isbn}")
  public BookDao findOneInLibraryWithISBN(@PathVariable String isbn) throws JSONException {

    BookDao bookDao = this.serviceLibrary.bookInfo(isbn);

    return bookDao;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("admin/findLocalLibrary/{isbn}")
  public Book findOneInLocalOrLibraryWithISBN(HttpServletResponse sRes, @PathVariable String isbn)
      throws JSONException {

    Book book = findOneWithISBN(isbn);

    if (findOneWithISBN(isbn) != null) {
      sRes.setStatus(HttpStatus.OK.value());
    } else {
      BookDao bookDao = findOneInLibraryWithISBN(isbn);
      if (findOneInLibraryWithISBN(isbn) != null) {
        sRes.setStatus(HttpStatus.CREATED.value());
        book = new Book(bookDao);
        bookRepository.save(book);
      } else {
        sRes.setStatus(HttpStatus.NOT_FOUND.value());
      }
    }

    return book;
  }

}