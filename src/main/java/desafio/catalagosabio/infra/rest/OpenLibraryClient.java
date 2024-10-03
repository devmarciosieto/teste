package desafio.catalagosabio.infra.rest;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.util.List;

@Component
public class OpenLibraryClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://openlibrary.org/search";

    public List<Book> fetchBooks() {
        String url = BASE_URL + ".json";

        var booksList = restTemplate.getForObject(url, Book.class);
        assert booksList != null;
        return List.of(booksList);
    }
}
