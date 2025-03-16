package io.github.bonigarcia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
	private final List<Book> store = new ArrayList<>();

    public List<Book> findBooksByAuthor(String author) {
		return store.stream().filter(
            book -> book.getAuthor().equals(author)
		).collect(Collectors.toList());
    }
    
    public List<Book> findBooks(LocalDateTime from, LocalDateTime to) {
		return store.stream().filter(
            book -> book.getPublished().isAfter(from) && book.getPublished().isBefore(to)
		).sorted(Comparator.comparing(Book::getPublished).reversed()).collect(Collectors.toList());
    }

    public void addBook(Book book) {
        store.add(book);
    }
}
