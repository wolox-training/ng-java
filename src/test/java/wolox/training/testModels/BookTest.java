package wolox.training.testModels;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookTest {

  @Autowired
  private BookRepository bookrepository;
  @Autowired
  private TestEntityManager entityManager;

  private Book book;

  @Before
  public void setUp() {
    book = new Book();
    book.setTitle("Title");
    book.setIsbn("ISBN");
    book.setAuthor("Author");
    book.setGenre("Genre");
    book.setImage("Image");
    book.setPages("Pages");
    book.setPublisher("Publisher");
    book.setSubtitle("Subtitle");
    book.setYear("Year");

    entityManager.persist(book);
    entityManager.flush();
  }

  @Test

  public void findByAuthorTest() {
    Book found = bookrepository.findOneByAuthor(book.getAuthor());

    assertThat(found.getAuthor())
        .isEqualTo(book.getAuthor());
  }

  @Test
  public void findByIsbn_NotOk() {
    Book found = bookrepository.findOneByIsbn("ISBN");

    assertThat(found.getIsbn()).isNotEqualToIgnoringCase("12321424");
  }

  @Test
  public void findByIsbn_Ok() {
    Book found = bookrepository.findOneByIsbn("ISBN");

    assertThat(found.getIsbn()).isEqualToIgnoringCase("ISBN");
  }

  @Test
  public void findByPublisherAndGenreAndYear_Ok() {
    Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, "title");
    Page<Book> found = bookrepository
        .findByPublisherAndGenreAndYear(book.getPublisher(), book.getGenre(), book.getYear(),
            pageable);

    assertThat(found.getContent().get(0)).isEqualTo(book);
  }


}

