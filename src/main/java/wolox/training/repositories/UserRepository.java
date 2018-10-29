package wolox.training.repositories;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

  Page<User> findByBirthDateBetweenAndUserNameContainingIgnoreCase(LocalDate birthDate1,
      LocalDate birthDate2, String userName, Pageable pageable);

  User findByUserName(String userName);
}
