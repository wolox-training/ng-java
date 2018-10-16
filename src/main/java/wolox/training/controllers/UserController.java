package wolox.training.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;
import wolox.training.repositories.BookRepository;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;


    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new RuntimeException();
        }
        userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        return userRepository.save(user);
    }


    @PutMapping("/addBooks/{idUser}/{idBook}")
    public User addBooks(@PathVariable("idUser") Long idUser, @PathVariable("idBook") Long idBook) throws BookAlreadyOwnedException{

        User userAux= userRepository.findById(idUser).orElseThrow(RuntimeException::new);

        Book bookAux= bookRepository.findById(idBook).orElseThrow(RuntimeException::new);


        for (Book b: userAux.getBooks()) {

            if(bookAux.getIsbn() == b.getIsbn()){
                throw new BookAlreadyOwnedException("Error: The book you are trying to add already exists.");
            }
        }

        userAux.addBook(bookAux);
        return userRepository.save(userAux);

    }

    @PutMapping("/deleteBooks/{idUser}/{idBook}")
    public User deleteBooks(@PathVariable("idUser") Long idUser, @PathVariable("idBook") Long idBook){

        User userAux= userRepository.findById(idUser).orElseThrow(RuntimeException::new);
        Book bookAux= bookRepository.findById(idBook).orElseThrow(RuntimeException::new);


        for (Book b: userAux.getBooks()) {

            if(bookAux.getIsbn() == b.getIsbn()){
                userAux.deleteBook(bookAux);
                return userRepository.save(userAux);

            }
        }

        throw new RuntimeException();

    }

}
