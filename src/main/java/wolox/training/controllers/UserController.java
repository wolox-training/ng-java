package wolox.training.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

import java.security.Principal;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;


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
        userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
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
    public User addBooks(@PathVariable("idUser") Long idUser, @PathVariable("idBook") Long idBook) throws BookAlreadyOwnedException {

        User userAux = userRepository.findById(idUser).orElseThrow(RuntimeException::new);
        Book bookAux = bookRepository.findById(idBook).orElseThrow(RuntimeException::new);


        for (Book b : userAux.getBooks()) {

            if (bookAux.getIsbn() == b.getIsbn()) {
                throw new BookAlreadyOwnedException("Error: The book you are trying to add already exists.");
            }
        }

        userAux.addBook(bookAux);
        return userRepository.save(userAux);

    }

    @PutMapping("deleteBooks/{idUser}/{idBook}")
    public User deleteBooks(@PathVariable("idUser") Long idUser, @PathVariable("idBook") Long idBook) {

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
