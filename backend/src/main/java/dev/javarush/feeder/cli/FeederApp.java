package dev.javarush.feeder.cli;

import dev.javarush.feeder.content.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.InMemoryFeedRepository;
import dev.javarush.feeder.feed.RomeFeedFetcher;
import dev.javarush.feeder.user.AlreadySubscribedException;
import dev.javarush.feeder.user.InMemoryUserRepository;
import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserNotFoundException;
import dev.javarush.feeder.user.UserRepository;
import dev.javarush.feeder.user.UserService;
import java.net.URI;
import java.util.Scanner;

public class FeederApp {

    private static final UserRepository userRepository = new InMemoryUserRepository();
    private static final FeedRepository feedRepository = new InMemoryFeedRepository();
    private static final UserFeedEntryRepository userFeedEntryRepository = new InMemoryUserFeedEntryRepository();
    private static final FeedService feedService = new FeedService(feedRepository, new RomeFeedFetcher());
    private static final UserService userService = new UserService(userRepository, feedService, userFeedEntryRepository);

    public static void main(String[] args) {
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
                        if (parts.length < 2) {
                            System.out.println("Usage: user-create <userId>");
                        } else {
                            userRepository.save(new User(parts[1]));
                            System.out.println("User created: " + parts[1]);
                        }
                        break;

                    case "subscribe":
                        if (parts.length < 3) {
                            System.out.println("Usage: subscribe <userId> <feedURI>");
                        } else {
                            userService.subscribe(parts[1], URI.create(parts[2]));
                            System.out.println("Subscribed successfully!");
                        }
                        break;

                    case "list-subs":
                        if (parts.length < 2) {
                            System.out.println("Usage: list-subs <userId>");
                        } else {
                            userRepository.findById(parts[1]).ifPresentOrElse(
                                user -> {
                                    System.out.println("Subscriptions for " + parts[1] + ":");
                                    user.getSubscriptions().forEach(sub -> System.out.println("- " + sub.feedUri()));
                                },
                                () -> System.out.println("User not found.")
                            );
                        }
                        break;

                    case "list-content":
                        if (parts.length < 2) {
                            System.out.println("Usage: list-content <userId>");
                        } else {
                            var entries = userFeedEntryRepository.findByUserId(parts[1]);
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
                e.printStackTrace(); // Optional for debugging
            }
        }
    }
}
