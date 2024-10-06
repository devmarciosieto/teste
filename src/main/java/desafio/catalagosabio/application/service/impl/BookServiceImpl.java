package desafio.catalagosabio.application.service.impl;


import desafio.catalagosabio.application.dto.BookDto;
import desafio.catalagosabio.application.service.BookService;
import desafio.catalagosabio.domain.exception.NotFoundException;
import desafio.catalagosabio.infra.model.Book;
import desafio.catalagosabio.infra.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static desafio.catalagosabio.domain.exception.ExceptionEnum.NOT_FOUND;


@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
//    @Transactional(readOnly = true)
    @Cacheable(value = "books", keyGenerator = "keyGenerator", unless = "#result == null or #result.empty")
    public Page<Book> findAllBooks(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "books", keyGenerator = "keyGenerator", unless = "#result == null or #result.empty")
    public Page<Book> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "bookById", key = "#id", unless = "#result == null")
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Cacheable(value = "booksByGenre", key = "#genre", unless = "#result == null or #result.empty")
    public List<Book> getBooksByGenre(String genre) {
        List<Book> byGenre = bookRepository.findByGenre(genre);
        verifyListNotEmpty(byGenre);
        return byGenre;
    }

    @Override
    @Cacheable(value = "booksByAuthor", key = "#author", unless = "#result == null or #result.empty")
    public List<Book> getBooksByAuthor(String author) {
        List<Book> byAuthor = bookRepository.findByAuthor(author);
        verifyListNotEmpty(byAuthor);
        return byAuthor;
    }

    private void verifyListNotEmpty(List<Book> books) {
        if (books.isEmpty()) {
            throw new NotFoundException(NOT_FOUND.getMessage(), NOT_FOUND.getStatusCode());
        }
    }

    //Metodo para transformar a lista paginada após o mapper
    private Page<BookDto> getPaginatedBooks(List<BookDto> books, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        List<BookDto> paginatedBooks = books.subList(start, end);

        return new PageImpl<>(paginatedBooks, pageable, books.size());
    }
}
