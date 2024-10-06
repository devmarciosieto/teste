package desafio.catalagosabio.application.service;


import desafio.catalagosabio.application.dto.BookDto;
import desafio.catalagosabio.infra.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Page<BookDto> findAllBooks(Pageable pageable);

    Optional<Book> getBookById(Long id);

    List<Book> getBooksByGenre(String genre);

    List<Book> getBooksByAuthor(String author);
}
