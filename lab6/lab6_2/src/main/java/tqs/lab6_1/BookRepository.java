package tqs.lab6_1;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findById(Integer id);
    List<Book> findByTitle(String name);
    List<Book> findByAuthor(String author);
}
