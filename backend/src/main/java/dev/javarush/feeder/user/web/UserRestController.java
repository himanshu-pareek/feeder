package dev.javarush.feeder.user.web;

import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import dev.javarush.feeder.user.use_case.UserRegistrationAction;
import java.net.URI;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

  private final UserRegistrationAction userRegistrationAction;
  private final FeedSubscriptionAction subscriptionUseCase;

  public UserRestController(
      UserRegistrationAction userRegistrationAction,
      FeedSubscriptionAction subscriptionUseCase
  ) {
    this.userRegistrationAction = userRegistrationAction;
    this.subscriptionUseCase = subscriptionUseCase;
  }

  @PostMapping("/subscribe")
  void subscribe(@RequestParam String feedUri, Authentication authentication) {
    this.subscriptionUseCase.execute(authentication.getName(), URI.create(feedUri));
  }

  @PostMapping("/register")
  void register(Authentication authentication) {
    this.userRegistrationAction.execute(authentication.getName());
  }
}
