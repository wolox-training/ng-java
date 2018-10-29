package wolox.training.testControllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import wolox.training.controllers.UserController;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;


@WebMvcTest(value = UserController.class, secure = false)
@RunWith(SpringRunner.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private BookRepository bookRepository;


  @Test
  public void getAllUSer_Test() throws Exception {

    User User1 = new User();
    User1.setUserName("user");
    User1.setPassword("user");
    User1.setName("user");
    User1.setIsAdmin(true);
    User1.setBirthDate(LocalDate.of(1995, 02, 11));

    User User2 = new User();
    User2.setUserName("user2");
    User2.setPassword("user2");
    User2.setIsAdmin(false);
    User2.setName("user2");
    User2.setBirthDate(LocalDate.of(1995, 02, 11));

    List allUser = Arrays.asList(User1, User2);

    given(userRepository.findAll()).willReturn(allUser);

    mvc.perform(get("/api/users/allUsers")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[1].name", is(User2.getName())));
  }


  @Test(expected = RuntimeException.class)
  public void deleteUser_Test() throws Throwable {
    User User1 = new User();
    User1.setUserName("user");
    User1.setPassword("user");
    User1.setName("user");
    User1.setIsAdmin(true);
    User1.setBirthDate(LocalDate.of(1995, 02, 11));

    List allUser = Arrays.asList(User1);

    given(userRepository.findAll()).willReturn(allUser);

    mvc.perform(delete("/api/users/admin/delete/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    try {

      mvc.perform(get("/api/users/findUser/1")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$", hasSize(0)));
    } catch (NestedServletException e) {
      throw e.getCause();
    }
  }


  @Test(expected = RuntimeException.class)
  public void updateUser_Test() throws Throwable {
    User User1 = new User();
    User1.setUserName("user");
    User1.setPassword("user");
    User1.setName("user");
    User1.setIsAdmin(true);
    User1.setBirthDate(LocalDate.of(1995, 02, 11));

    List allUser = Arrays.asList(User1);

    given(userRepository.findAll()).willReturn(allUser);

    String user =
        "{\"id\": 1,\n\"userName\": \"user1\",\"password\": \"user1\",\"name\": \"User1\",\n"
            +
            "\"birthDate\": \"1995-02-11\",\"isAdmin\": true,\"books\": null\"}";

    mvc.perform(put("/api/users/admin/update/1")
        .contentType(MediaType.APPLICATION_JSON).content(user));

    try {
      mvc.perform(get("/api/users/findUser/1")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[1].name", is("user1")));
    } catch (NestedServletException e) {
      throw e.getCause();
    }
  }


  @Test
  public void createUser_Test() throws Exception {

    User User1 = new User();
    User1.setUserName("user");
    User1.setName("user");
    User1.setBirthDate(LocalDate.of(1995, 02, 11));

    when(userRepository
        .save(new User("user", "password", "user", LocalDate.of(1995, 02, 11), false)))
        .thenReturn(User1);


  }

}