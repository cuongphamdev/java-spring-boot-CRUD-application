package com.example.learn.services;

import com.example.learn.daos.UserDAO;
import com.example.learn.models.User;
import com.example.learn.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

  @Mock
  private UserDAO userDAO;

  @InjectMocks
  private UserService userService = new UserServiceImpl();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  private void setupEach() {
    when(userDAO.findById(1)).thenReturn(ServiceDataTest.dummyUser);
    when(userDAO.findById(2)).thenReturn(null);
    when(userDAO.create(any())).thenAnswer(
            invocation -> {
              User arguments = (User) invocation.getArgument(0);
              if (!arguments.getEmail().equals(ServiceDataTest.dummyUser.getEmail())) {
                return ServiceDataTest.dummyUser;
              }
              return null;
            }
    );
    when(userDAO.findByEmailAndPassword(anyString(), anyString())).thenAnswer(
            invocation -> {
              String email = invocation.getArgument(0);
              String password = invocation.getArgument(1);
              for (User user : ServiceDataTest.dummyUserList) {
                if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                  return user;
                }
              }
              return null;
            }
    );

    when(userDAO.searchUserByNameOrEmail(anyString())).thenAnswer(invocation -> {
      String searchQuery = invocation.getArgument(0);
      List<User> userList = new ArrayList<>();
      for (User user : ServiceDataTest.dummyUserList) {
        if (user.getEmail().contains(searchQuery)) {
          userList.add(user);
        }
      }
      return userList;
    }
    );

    when(userDAO.update(any())).thenAnswer(invocation -> {
              User user = invocation.getArgument(0);
              if (user.getEmail().equals(ServiceDataTest.dummyUser.getEmail())) {
                return ServiceDataTest.dummyUser;
              }
              return null;
            }
    );

    when(userDAO.findAll()).thenReturn(Arrays.asList(ServiceDataTest.dummyUserList));
  }

  @DisplayName("Find user by valid id and return valid user data")
  @Test
  void findUserByIdTest() {
    User result = userService.findUserById(1);
    assertSame(ServiceDataTest.dummyUser, result);
    verify(userDAO).findById(1);
  }

  @DisplayName("Find user by invalid id and return null")
  @Test
  void findUserByInvalidIdTest() {
    User result = userService.findUserById(2);
    assertSame(null, result);
    verify(userDAO).findById(2);
  }

  @DisplayName("Create new User by valid data")
  @Test
  void createNewUserWithValidDataTest() {
    User result = userService.createNewUser(ServiceDataTest.dummyUserList[1].getName(), ServiceDataTest.dummyUserList[1].getEmail(), ServiceDataTest.dummyUserList[1].getPassword(), 1);
    assertEquals(ServiceDataTest.dummyUser, result);
    verify(userDAO).create(argThat((aUser) -> aUser.getEmail().equals(ServiceDataTest.dummyUserList[1].getEmail())));
  }

  @DisplayName("Create new User by invalid data - existed email")
  @Test
  void createNewUserWithInvalidDataTest() {
    User result = userService.createNewUser("Cuong", "cuongpham.dev@gmail.com", "123456", 1);
    assertEquals(null, result);
    verify(userDAO).create(argThat((aUser) -> aUser.getEmail().equals(ServiceDataTest.dummyUser.getEmail())));
  }

  @DisplayName("loginByEmailAndPassword return valid user data")
  @Test
  void loginWithValidEmailAndPassword () {
    User result = userService.loginByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail(), ServiceDataTest.dummyUserList[1].getPassword());
    assertEquals(ServiceDataTest.dummyUserList[1], result);
    verify(userDAO).findByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail(), ServiceDataTest.dummyUserList[1].getPassword());
  }

  @DisplayName("loginByEmailAndPassword return invalid user data")
  @Test
  void loginWithInvalidEmailAndPassword () {
    User result = userService.loginByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail() + "wrong", ServiceDataTest.dummyUserList[1].getPassword());
    assertEquals(null, result);
    verify(userDAO).findByEmailAndPassword(ServiceDataTest.dummyUserList[1].getEmail() + "wrong", ServiceDataTest.dummyUserList[1].getPassword());
  }

  @DisplayName("searchUserByNameOrEmail valid search query")
  @Test
  void searchUserByNameOrEmailValidQuery () {
    String queryString = "test";
    List<User> result = userService.searchUserByNameOrEmail(queryString);
    assertNotEquals(null, result);
    verify(userDAO).searchUserByNameOrEmail(queryString);
  }

  @DisplayName("searchUserByNameOrEmail valid search query")
  @Test
  void searchUserByNameOrEmailInvalidQuery () {
    String searchString = "wrong data";
    List<User> result = userService.searchUserByNameOrEmail(searchString);
    assertEquals(0, result.size());
    verify(userDAO).searchUserByNameOrEmail(searchString);
  }

  @DisplayName("update success")
  @Test
  void update () {
    User result = userService.updateUser(ServiceDataTest.dummyUser);
    assertEquals(ServiceDataTest.dummyUser, result);
  }

  @DisplayName("update fail")
  @Test
  void updateFail () {
    User user = new User(ServiceDataTest.dummyUser.getName(), "wrongemail@dot.com", ServiceDataTest.dummyUser.getPassword());
    User result = userService.updateUser(user);
    assertEquals(null, result);
  }

  @DisplayName("findAllUser success")
  @Test
  void findAll () {
    List<User> result = userService.findAllUser();
    assertEquals(Arrays.asList(ServiceDataTest.dummyUserList), result);
  }

}
