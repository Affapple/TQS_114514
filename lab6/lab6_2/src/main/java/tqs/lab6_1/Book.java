package tqs.lab6_1;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_seq")
    @SequenceGenerator(name = "books_seq", sequenceName = "books_seq", allocationSize = 1)
    Integer id;

    String title;
    String author;
    LocalDate published;
    public Book(String name, String author, LocalDate published) {
        this.title = name;
        this.author = author;
        this.published = published;
    }
}
