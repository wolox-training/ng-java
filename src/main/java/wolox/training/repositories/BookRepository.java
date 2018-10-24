package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);

    Book findOneByAuthor(String author);

    Book findOneByIsbn(String isbn);
}
