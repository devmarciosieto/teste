package desafio.catalagosabio.web.controller;

import desafio.catalagosabio.application.dto.BookDto;
import desafio.catalagosabio.application.service.BookService;
import desafio.catalagosabio.domain.exception.BusinessException;
import desafio.catalagosabio.domain.mapper.BookMapper;
import desafio.catalagosabio.infra.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    BookMapper bookMapper;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<BookDto>> getAllBooks(Pageable pageable) {
        Page<Book> books = bookService.findAllBooks(pageable);
        List<BookDto> bookDtoList = bookMapper.toDto(books.stream().toList());
        return ResponseEntity.ok(new PageImpl(bookDtoList, pageable, books.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        return book.map(ResponseEntity::ok)
                .orElseThrow(() -> new BusinessException(ID_NOT_FOUND.getMessage(), ID_NOT_FOUND.getStatusCode()));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Book>> getBooksByGenre(@RequestParam String genre) {
        if (genre == null || genre.isBlank()) {
            throw new BusinessException(GENRE_PARAMETER_NULL.getMessage(), GENRE_PARAMETER_NULL.getStatusCode());
        }
        List<Book> books = bookService.getBooksByGenre(genre);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author) {
        if (author == null || author.isBlank()) {
            throw new BusinessException(AUTHOR_PARAMETER_NULL.getMessage(), AUTHOR_PARAMETER_NULL.getStatusCode());
        }
        List<Book> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }
}

