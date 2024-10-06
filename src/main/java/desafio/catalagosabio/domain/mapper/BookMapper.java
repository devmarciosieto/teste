package desafio.catalagosabio.domain.mapper;

import desafio.catalagosabio.application.dto.BookDto;
import desafio.catalagosabio.infra.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> books);
}