package desafio.catalagosabio.application.service.impl;


import desafio.catalagosabio.application.service.BookService;
import desafio.catalagosabio.domain.exception.BusinessException;
import desafio.catalagosabio.infra.model.Book;
import desafio.catalagosabio.infra.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Transactional(readOnly = true)
    @Cacheable(value = "books", keyGenerator = "keyGenerator", unless = "#result == null or #result.empty")
    public Page<Book> findAllBooks(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size));
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
            throw new BusinessException(NOT_FOUND.getMessage(), NOT_FOUND.getStatusCode());
        }
    }
}
