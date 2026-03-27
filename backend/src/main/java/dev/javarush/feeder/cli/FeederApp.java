package dev.javarush.feeder.cli;

import dev.javarush.feeder.content.memory.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.content.event.FeedSubscriptionEventListener;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.memory.InMemoryFeedRepository;
import dev.javarush.feeder.feed.rome.RomeFeedFetcher;
import dev.javarush.feeder.user.exception.AlreadySubscribedException;
import dev.javarush.feeder.user.memory.InMemoryUserRepository;
import dev.javarush.feeder.user.exception.UserNotFoundException;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import dev.javarush.feeder.user.use_case.UserRegistrationAction;
import java.net.URI;
import java.util.Scanner;

public class FeederApp {

    private FeedSubscriptionAction feedSubscriptionAction;
    private FeedEntriesGetAction feedEntriesGetAction;
    private UserRegistrationAction userRegistrationAction;

    public static void main(String[] args) {
        new FeederApp().run();
    }

    private void run() {
        injectDependencies();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Java Feeder CLI!");
        System.out.println("Available commands: user-create, subscribe, list-subs, list-content, exit");

        while (scanner.hasNextLine()) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+", 3);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "user-create":
                        userRegistrationAction.execute(parts[1]);
                        break;

                    case "subscribe":
                        if (parts.length < 3) {
                            System.out.println("Usage: subscribe <userId> <feedURI>");
                        } else {
                            try {
                                feedSubscriptionAction.execute(parts[1], URI.create(parts[2]));
                                System.out.println("Subscribed successfully!");
                            } catch (Throwable t) {
                                System.out.println("Unable to subscribe to " + parts[2]);
                                System.out.println(t.getLocalizedMessage());
                            }
                        }
                        break;

//                    case "list-subs":
//                        if (parts.length < 2) {
//                            System.out.println("Usage: list-subs <userId>");
//                        } else {
//                            userRepository.findById(parts[1]).ifPresentOrElse(
//                                user -> {
//                                    System.out.println("Subscriptions for " + parts[1] + ":");
//                                    user.getSubscriptions().forEach(sub -> System.out.println("- " + sub.feedUri()));
//                                },
//                                () -> System.out.println("User not found.")
//                            );
//                        }
//                        break;

                    case "list-content":
                        if (parts.length < 2) {
                            System.out.println("Usage: list-content <userId>");
                        } else {
                            var entries = feedEntriesGetAction.execute(parts[1]);
                            System.out.println("Content for " + parts[1] + " (" + entries.size() + " items):");
                            entries.forEach(e -> System.out.println("- [" + (e.isRead() ? "X" : " ") + "] " + e.getTitle() + " (" + e.getLink() + ")"));
                        }
                        break;

                    case "exit":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Unknown command: " + command);
                }
            } catch (UserNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (AlreadySubscribedException e) {
                System.err.println("Warning: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Critical Error: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void injectDependencies() {
        final FeedService feedService = new FeedService( new InMemoryFeedRepository(), new RomeFeedFetcher());
        final UserFeedEntryService userFeedEntryService = new UserFeedEntryService(new InMemoryUserFeedEntryRepository());
        final UserService userService = new UserService(new InMemoryUserRepository());
        FeedSubscriptionEventListener feedSubscriptionEventListener =
            new FeedSubscriptionEventListener(userFeedEntryService, feedService);
        this.feedSubscriptionAction = new FeedSubscriptionAction(
            userService,
            feedService,
            feedSubscriptionEventListener::handleFeedSubscription
        );
        this.feedEntriesGetAction = new FeedEntriesGetAction(userFeedEntryService);
        this.userRegistrationAction = new UserRegistrationAction(userService);
    }
}
