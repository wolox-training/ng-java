package wolox.training.controllers;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wolox.training.Services.OpenLibraryService;
import wolox.training.dao.BookDao;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    private OpenLibraryService serviceLibrary = new OpenLibraryService();

    @GetMapping("/")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
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
    public Book findOneInLocalOrLibraryWithISBN(HttpServletResponse sRes, @PathVariable String isbn) throws JSONException {

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