package dev.javarush.feeder.cli;

import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.content.use_case.FeedEntriesGetAction;
import dev.javarush.feeder.content.use_case.UserFeedEntriesSyncAction;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.rome.RomeFeedFetcher;
import dev.javarush.feeder.feed.use_case.FeedSyncAction;
import dev.javarush.feeder.memory.content.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.memory.feed.InMemoryFeedRepository;
import dev.javarush.feeder.memory.user.InMemoryUserRepository;
import dev.javarush.feeder.subscription.Subscription;
import dev.javarush.feeder.user.exception.AlreadySubscribedException;
import dev.javarush.feeder.user.exception.UserNotFoundException;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.user.use_case.FeedSubscriptionAction;
import dev.javarush.feeder.user.use_case.UserRegistrationAction;
import java.net.URI;
import java.util.Scanner;

public class FeederCLI {

    private FeedSubscriptionAction feedSubscriptionAction;
    private FeedEntriesGetAction feedEntriesGetAction;
    private UserRegistrationAction userRegistrationAction;
    private UserService userService;
    private FeedSyncAction feedSyncAction;

    static void main() {
        new FeederCLI().run();
    }

    private void run() {
        injectDependencies();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Java Feeder CLI!");
        System.out.println("Available commands: user-create, subscribe, list-subs, list-content, sync, exit");

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

                    case "list-subs":
                        if (parts.length < 2) {
                            System.out.println("Usage: list-subs <userId>");
                        } else {
                            userService.getUser(parts[1]).getSubscriptions()
                                .stream()
                                .map(Subscription::feedUri)
                                .map(uri -> "- " + uri)
                                .forEach(System.out::println);
                        }
                        break;

                    case "list-content":
                        if (parts.length < 2) {
                            System.out.println("Usage: list-content <userId>");
                        } else {
                            var entries = feedEntriesGetAction.execute(parts[1]);
                            System.out.println("Content for " + parts[1] + " (" + entries.size() + " items):");
                            entries.items().forEach(e -> System.out.println("- [" + (e.isRead() ? "X" : " ") + "] " + e.getTitle() + " (" + e.getLink() + ")"));
                        }
                        break;

                    case "sync":
                        if (parts.length > 1) {
                            System.err.println("Usage: sync");
                        } else {
                            feedSyncAction.syncFeeds();
                            System.out.println(":) Feeds synced");
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
            }
        }
    }

    private void injectDependencies() {
        final FeedService feedService = new FeedService( new InMemoryFeedRepository(), new RomeFeedFetcher());
        final UserFeedEntryService userFeedEntryService = new UserFeedEntryService(new InMemoryUserFeedEntryRepository());
       userService = new UserService(new InMemoryUserRepository());
      this.feedSubscriptionAction = new FeedSubscriptionAction(
            userService,
            feedService,
            event -> {
                Feed feed = feedService.getFeed(event.feedUri());
                userFeedEntryService.createEntriesForUser(event.userId(), feed);
            }
        );
        this.feedEntriesGetAction = new FeedEntriesGetAction(userFeedEntryService);
        this.userRegistrationAction = new UserRegistrationAction(userService);
        UserFeedEntriesSyncAction userFeedEntriesSyncAction = new UserFeedEntriesSyncAction(
            userService,
            userFeedEntryService,
            feedService
        );
        this.feedSyncAction = new FeedSyncAction(
            feedService,
            event -> {
                userFeedEntriesSyncAction.execute(event.feedUri());
            }
        );
    }
}
