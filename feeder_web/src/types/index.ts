export interface Person {
    name: string;
    uri: string;
    email: string;
}

export interface FeedEntryContent {
    type: string;
    value: string;
}

export interface Enclosure {
    url: string;
    length: number;
    type: string;
}

export interface UserFeedEntry {
    userId: string;
    feedUri: string;
    feedEntryUri: string;
    title: string;
    link: string;
    description: string;
    publishedDate: string;
    updatedDate: string;
    authors: Person[];
    categories: string[];
    comments: string;
    contents: FeedEntryContent[];
    contributors: Person[];
    enclosures: Enclosure[];
    read: boolean;
}

export interface UserFeedEntries {
    page: number;
    size: number;
    items: UserFeedEntry[];
}
