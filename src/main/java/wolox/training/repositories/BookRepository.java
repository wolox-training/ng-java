package wolox.training.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Page<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year, Pageable pageable);

    Book findOneByAuthor(String author);

    Book findOneByIsbn(String isbn);
}
