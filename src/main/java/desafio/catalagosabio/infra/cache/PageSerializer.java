package desafio.catalagosabio.infra.cache;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PageSerializer extends StdSerializer<Page<?>> implements RedisSerializer<Page<?>> {

    public PageSerializer() {
        super((Class<Page<?>>) (Class<?>) Page.class);
    }

    @Override
    public byte[] serialize(Page<?> page) throws SerializationException {
        try {
            return new ObjectMapper().writeValueAsBytes(page);
        } catch (IOException e) {
            throw new SerializationException("Erro ao serializar a página", e);
        }
    }

    @Override
    public Page<?> deserialize(byte[] bytes) throws SerializationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Registre os módulos do Jackson para desserialização apropriada
            objectMapper.registerModule(new SimpleModule().addDeserializer(Page.class, new CustomPageDeserializer()));

            return objectMapper.readValue(bytes, Page.class);
        } catch (IOException e) {
            throw new SerializationException("Erro ao desserializar a página", e);
        }
    }


    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        // Serializa o conteúdo da página
        gen.writeFieldName("content");
        provider.defaultSerializeValue(page.getContent(), gen);

        // Serializa os dados de pageable
        gen.writeFieldName("pageable");
        gen.writeStartObject();
        gen.writeFieldName("org.springframework.data.domain.PageRequest");
        gen.writeStartObject();
        gen.writeNumberField("pageNumber", page.getNumber());
        gen.writeNumberField("pageSize", page.getSize());

        // Serializa os dados de sort
        gen.writeFieldName("sort");
        gen.writeStartObject();
        gen.writeFieldName("org.springframework.data.domain.Sort");
        gen.writeStartObject();
        gen.writeBooleanField("empty", page.getSort().isEmpty());
        gen.writeBooleanField("sorted", page.getSort().isSorted());
        gen.writeBooleanField("unsorted", page.getSort().isUnsorted());
        gen.writeEndObject(); // Fecha org.springframework.data.domain.Sort
        gen.writeEndObject(); // Fecha sort

        gen.writeNumberField("offset", page.getPageable().getOffset());
        gen.writeBooleanField("paged", page.getPageable().isPaged());
        gen.writeBooleanField("unpaged", page.getPageable().isUnpaged());
        gen.writeEndObject(); // Fecha org.springframework.data.domain.PageRequest
        gen.writeEndObject(); // Fecha pageable

        // Serializa os dados da página
        gen.writeBooleanField("last", page.isLast());
        gen.writeNumberField("totalPages", page.getTotalPages());
        gen.writeNumberField("totalElements", page.getTotalElements());
        gen.writeNumberField("size", page.getSize());
        gen.writeNumberField("number", page.getNumber());

        // Serializa novamente os dados de sort para a página
        gen.writeFieldName("sort");
        gen.writeStartObject();
        gen.writeFieldName("org.springframework.data.domain.Sort");
        gen.writeStartObject();
        gen.writeBooleanField("empty", page.getSort().isEmpty());
        gen.writeBooleanField("sorted", page.getSort().isSorted());
        gen.writeBooleanField("unsorted", page.getSort().isUnsorted());
        gen.writeEndObject(); // Fecha org.springframework.data.domain.Sort
        gen.writeEndObject(); // Fecha sort

        gen.writeBooleanField("first", page.isFirst());
        gen.writeNumberField("numberOfElements", page.getNumberOfElements());
        gen.writeBooleanField("empty", page.isEmpty());

        gen.writeEndObject(); // Fecha o objeto principal
    }

}
