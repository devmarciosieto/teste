package desafio.catalagosabio.application.service.impl;


import desafio.catalagosabio.application.dto.BookDto;
import desafio.catalagosabio.application.service.BookService;
import desafio.catalagosabio.domain.exception.NotFoundException;
import desafio.catalagosabio.domain.mapper.BookMapper;
import desafio.catalagosabio.infra.model.Book;
import desafio.catalagosabio.infra.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static desafio.catalagosabio.domain.exception.ExceptionEnum.NOT_FOUND;


@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Cacheable(value = "books", keyGenerator = "keyGenerator", unless = "#result == null or #result.empty")
    public Page<BookDto> findAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        List<BookDto> bookDtoList = bookMapper.toDto(books.stream().toList());
        return new PageImpl(bookDtoList, pageable, books.getTotalElements());
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

}
