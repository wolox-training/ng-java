package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByBirthDateBetweenAndUserNameContainingIgnoreCase(LocalDate birthDate1, LocalDate birthDate2, String userName);

    User findByUserName(String userName);
}
