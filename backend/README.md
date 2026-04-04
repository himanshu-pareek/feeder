# Backend for Feeder Application

## Feed Sync Flow

- Iterate over all the feeds in batches. For each feed:
  - Fetch and save the feed.
  - For each subscription for this feed
    - Create user feed entries

### Questions

**Who initiates the feed sync flow?**\
An endpoint `feeds/sync` is exposed, which can be triggered by making api call. The user must have `feeds.sync` permission.

**Who iterate over the feeds in batches?**\
A background job (`JobA`) is created when `feeds/sync` request is received, which iterate over all the feeds in batches.

**Who fetches the feed?**\
Background Job `JobA` fetches and saves the feeds. And then puts the id of the feed in a queue.

**Who creates user feed entries?**\
A consumer picks up the job from the queue and fetches the feed details from the queue. After that for each subscription of the feed, it creates user feed entries.
