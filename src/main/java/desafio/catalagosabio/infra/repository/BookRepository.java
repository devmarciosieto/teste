package desafio.catalagosabio.infra.repository;



import desafio.catalagosabio.infra.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select b.genre from Book b where b.genre = :genre")
    List<Book> findByGenre(@Param("genre") String genre);

    @Query(value = "select b.author from Book b where b.author = :author")
    List<Book> findByAuthor(@Param("author") String author);
}
