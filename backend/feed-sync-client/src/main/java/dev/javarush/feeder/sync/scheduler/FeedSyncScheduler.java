package dev.javarush.feeder.sync.scheduler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FeedSyncScheduler {
    private final RestClient restClient;

    public FeedSyncScheduler(@Qualifier("resource") RestClient restClient) {
        this.restClient = restClient;
    }

    @Scheduled(cron = "${feeds.sync.schedule.cron}")
    public void triggerFeedSync() {
        System.out.println("FEED-SYNC: Sending /feeds/sync request to FeedAPI");
        var response = restClient.post().uri("http://localhost:8080/feeds/sync")
                .retrieve()
                .onStatus(res -> res.getStatusCode() != HttpStatusCode.valueOf(200))
                .toEntity(String.class);
        System.out.println("FEED-SYNC: Status code: " + response.getStatusCode());
        System.out.println("FEED-SYNC: Body: " + response.getBody());
    }
}
