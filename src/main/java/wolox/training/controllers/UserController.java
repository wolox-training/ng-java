package wolox.training.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

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

    @RequestMapping(value = "/addBooks")
    public void addBooks(Book book, Long idUser){
        User userAux= findOne(idUser);
        Boolean finded=false;
        for (Book bookAux: userAux.getBooks()) {

            if(bookAux.getIsbn() == book.getIsbn()){
                finded = true;
                throw new RuntimeException();
            }
        }

        userAux.addBook(book);

    }

    @RequestMapping(value = "/deleteBooks")
    public void deleteBooks(Book book, Long idUser){
        User userAux= findOne(idUser);
        Boolean finded=true;
        for (Book bookAux: userAux.getBooks()) {

            if(bookAux.getIsbn() == book.getIsbn()){
                userAux.deleteBook(book);
            }
            else{
                finded=false;
            }
        }

        if(!finded){
            throw new RuntimeException();

        }
    }
}
