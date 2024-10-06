package desafio.catalagosabio.infra.cache;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
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
            return new ObjectMapper().readValue(bytes, Page.class);
        } catch (IOException e) {
            throw new SerializationException("Erro ao desserializar a página", e);
        }
    }

    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("content");
        provider.defaultSerializeValue(page.getContent(), gen);
        gen.writeNumberField("pageNumber", page.getNumber());
        gen.writeNumberField("pageSize", page.getSize());
        gen.writeNumberField("totalElements", page.getTotalElements());
        gen.writeNumberField("totalPages", page.getTotalPages());
        gen.writeBooleanField("isFirst", page.isFirst());
        gen.writeBooleanField("isLast", page.isLast());
        gen.writeEndObject();
    }
}
