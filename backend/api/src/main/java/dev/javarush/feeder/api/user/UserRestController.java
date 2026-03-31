package dev.javarush.feeder.api.user;

import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import dev.javarush.feeder.user.use_case.UserRegistrationAction;
import java.net.URI;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

  private final UserRegistrationAction userRegistrationAction;
  private final FeedSubscriptionAction subscriptionUseCase;
  private final UserService userService;

  public UserRestController(
      UserRegistrationAction userRegistrationAction,
      FeedSubscriptionAction subscriptionUseCase,
      UserService userService
  ) {
    this.userRegistrationAction = userRegistrationAction;
    this.subscriptionUseCase = subscriptionUseCase;
    this.userService = userService;
  }

  @GetMapping
  User get(Authentication authentication) {
    return this.userService.getUser(authentication.getName());
  }

  @PostMapping("/subscribe")
  void subscribe(@RequestParam URI feedUri, Authentication authentication) {
    this.subscriptionUseCase.execute(authentication.getName(), feedUri);
  }

  @PostMapping("/register")
  void register(Authentication authentication) {
    this.userRegistrationAction.execute(authentication.getName());
  }
}
