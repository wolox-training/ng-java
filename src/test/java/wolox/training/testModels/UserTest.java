package wolox.training.testModels;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
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
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestEntityManager entityManager;

  private User user;

  @Before
  public void setUp() {

    user = new User();
    user.setUserName("user name");
    user.setName("name");
    user.setPassword("pass");
    user.setBirthDate(LocalDate.parse("1995-02-11"));
    user.setIsAdmin(true);

    entityManager.persist(user);
    entityManager.flush();
  }


  @Test
  public void findByUserName_NotOk() {
    User found = userRepository.findByUserName("user name");

    assertThat(found.getUserName()).isNotEqualToIgnoringCase("aadasdasd");
  }

  @Test
  public void findByUserName_Ok() {
    User found = userRepository.findByUserName("user name");

    assertThat(found.getUserName()).isEqualToIgnoringCase("user name");
  }

  @Test
  public void findByBirthDateBetweenAndUserNameContainingIgnoreCase_Test_OK() {
    Pageable pageable = PageRequest.of(0, 4, Sort.Direction.ASC, "userName");
    Page<User> found = userRepository
        .findByBirthDateBetweenAndUserNameContainingIgnoreCase(LocalDate.parse("1995-02-01"),
            LocalDate.parse("1995-02-13"), "us", pageable);

    assertThat(found.getContent().get(0)).isEqualTo(user);
  }
}
