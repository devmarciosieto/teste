package desafio.catalagosabio.infra.repository;



import desafio.catalagosabio.infra.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select b from Book b where LOWER(b.genre) LIKE LOWER(CONCAT('%', REPLACE(:genre, ' ', '%'), '%'))\n")
    List<Book> findByGenre(@Param("genre") String genre);

    @Query(value = "select b from Book b where LOWER(b.author) LIKE LOWER(CONCAT('%', REPLACE(:author, ' ', '%'), '%'))\n")
    List<Book> findByAuthor(@Param("author") String author);
}
