package wolox.training.controllers;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;


@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;


  @GetMapping("/allUsers")
  public List<User> getallUsers() {
    return (List<User>) userRepository.findAll();
  }

  @GetMapping("/findAllUsers/bdn/{birthDate1}/{birthDate2}/{userName}")
  public List<User> findByBirthDateBetweenAndUserNameContaining(@PathVariable String birthDate1,
      @PathVariable String birthDate2, @PathVariable String userName) {
    return userRepository
        .findByBirthDateBetweenAndUserNameContainingIgnoreCase(LocalDate.parse(birthDate1),
            LocalDate.parse(birthDate2), userName,
            PageRequest.of(0, 4, Sort.Direction.ASC, "userName")).getContent();
  }

  @GetMapping("findUser/{id}")
  public User findOne(@PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(RuntimeException::new);
  }

  @PostMapping("create")
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody User user) {
    return userRepository.save(user);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("admin/delete/{id}")
  public void delete(@PathVariable Long id) {
    userRepository.deleteById(id);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("admin/update/{id}")
  public User updateUser(@RequestBody User user, @PathVariable Long id) {
    if (user.getId() != id) {
      throw new RuntimeException();
    }
    userRepository.findById(id)
        .orElseThrow(RuntimeException::new);
    return userRepository.save(user);
  }


  @PutMapping("addBooks/{idUser}/{idBook}")
  public User addBooks(@PathVariable("idUser") Long idUser, @PathVariable("idBook") Long idBook)
      throws BookAlreadyOwnedException {

    User userAux = userRepository.findById(idUser).orElseThrow(RuntimeException::new);
    Book bookAux = bookRepository.findById(idBook).orElseThrow(RuntimeException::new);

    for (Book b : userAux.getBooks()) {

      if (bookAux.getIsbn() == b.getIsbn()) {
        throw new BookAlreadyOwnedException(
            "Error: The book you are trying to add already exists.");
      }
    }

    userAux.addBook(bookAux);
    return userRepository.save(userAux);

  }

  @PutMapping("deleteBooks/{idUser}/{idBook}")
  public User deleteBooks(@PathVariable("idUser") Long idUser,
      @PathVariable("idBook") Long idBook) {

    User userAux = userRepository.findById(idUser).orElseThrow(RuntimeException::new);
    Book bookAux = bookRepository.findById(idBook).orElseThrow(RuntimeException::new);

    for (Book b : userAux.getBooks()) {

      if (bookAux.getIsbn() == b.getIsbn()) {
        userAux.deleteBook(bookAux);
        return userRepository.save(userAux);

      }
    }

    throw new RuntimeException();

  }

  @RequestMapping(value = "/username", method = RequestMethod.GET)
  @ResponseBody
  public String currentUserName(Principal principal) {
    return "User Name:" + principal.getName();
  }


}
