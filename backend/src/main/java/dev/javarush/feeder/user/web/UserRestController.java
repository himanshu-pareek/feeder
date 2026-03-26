package dev.javarush.feeder.user.web;

import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import java.net.URI;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final FeedSubscriptionAction subscriptionUseCase;

  public UserRestController(FeedSubscriptionAction subscriptionUseCase) {
    this.subscriptionUseCase = subscriptionUseCase;
  }

  @PostMapping("/{userId}/subscribe")
    public void subscribe(@PathVariable String userId, @RequestParam String feedUri) {
       this.subscriptionUseCase.execute(userId, URI.create(feedUri));
    }
}
