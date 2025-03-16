package io.github.bonigarcia;

import java.time.LocalDateTime;

public class Book {
    LocalDateTime published;
    String author;
    String title;
    public Book(String author, String title, LocalDateTime published) {
        this.author = author;
        this.title = title;
        this.published = published;
    }

    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public LocalDateTime getPublished() {
        return published;
    }

    @Override
    public String toString() {
        return "Book [published=" + published + ", author=" + author + ", title=" + title + "]";
    }
}
