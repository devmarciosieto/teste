package desafio.catalagosabio.application.service.impl;


import desafio.catalagosabio.application.service.BookService;
import desafio.catalagosabio.infra.model.Book;
import desafio.catalagosabio.infra.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "'allBooks'", unless = "#result == null or #result.empty")
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
        return bookRepository.findByGenre(genre.toUpperCase());
    }

    @Override
    @Cacheable(value = "booksByAuthor", key = "#author", unless = "#result == null or #result.empty")
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author.toUpperCase());
    }
}
