package wolox.training.models;

import java.time.LocalDate;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Table(name = "\"User\"")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
  @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
  private long id;

  @Column(unique = true)
  private String userName;

  //@JsonIgnore
  @Column(unique = true)
  private String password;

  @Column
  private String name;

  @Column
  private LocalDate birthDate;

  @Column
  private boolean isAdmin;


  @ManyToMany(cascade = CascadeType.ALL)
  private Collection<Book> books;

  public User() {
  }


  public User(String userName, String password, String name, LocalDate birthDate, boolean isAdmin) {
    this.userName = userName;
    this.password = encryptPassword(password);
    this.name = name;
    this.birthDate = birthDate;
    this.isAdmin = isAdmin;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public Collection<Book> getBooks() {
    return books;
  }

  public void setBooks(Collection<Book> books) {
    this.books = books;
  }

  public void addBook(Book book) {
    this.books.add(book);
  }

  public void deleteBook(Book book) {
    this.books.remove(book);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = encryptPassword(password);
  }

  public String encryptPassword(String pass) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(pass);
  }

  public boolean getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", userName='" + userName + '\'' +
        ", name='" + name + '\'' +
        ", birthDate=" + birthDate +
        '}';
  }
}
