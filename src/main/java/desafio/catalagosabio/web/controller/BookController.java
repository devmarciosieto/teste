package desafio.catalagosabio.web.controller;

import desafio.catalagosabio.application.service.BookService;
import desafio.catalagosabio.domain.exception.BusinessException;
import desafio.catalagosabio.infra.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static desafio.catalagosabio.domain.exception.ExceptionEnum.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookService.findAllBooks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Book>> getBooksByGenre(@RequestParam String genre) {
        if (genre == null || genre.isBlank()) {
            throw new BusinessException(GENRE_PARAMETER_NULL.getMessage(), GENRE_PARAMETER_NULL.getStatusCode());
        }
        List<Book> books = bookService.getBooksByGenre(genre);
        return books.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(books);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author) {
        if (author == null || author.isBlank()) {
            throw new BusinessException(AUTHOR_PARAMETER_NULL.getMessage(), AUTHOR_PARAMETER_NULL.getStatusCode());
        }
        List<Book> books = bookService.getBooksByAuthor(author);
        return books.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(books);
    }
}

