package dev.javarush.feeder.user.use_case;

import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserService;

public class UserRegistrationAction {
  private final UserService userService;

  public UserRegistrationAction(UserService userService) {
    this.userService = userService;
  }

  public void execute(String userId) {
    this.userService.createUser(new User(userId));
  }
}
