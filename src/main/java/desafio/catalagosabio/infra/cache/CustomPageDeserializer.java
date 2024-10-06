package desafio.catalagosabio.infra.cache;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

public class CustomPageDeserializer extends JsonDeserializer<Page<?>> {

    @Override
    public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        // Deserializando o conteúdo da página
        List<?> content = p.getCodec().treeToValue(node.get("content"), List.class);

        // Deserializando o pageable
        JsonNode pageableNode = node.get("pageable").get("org.springframework.data.domain.PageRequest");
        int pageNumber = pageableNode.get("pageNumber").asInt();
        int pageSize = pageableNode.get("pageSize").asInt();

        // Deserializando o sort
        JsonNode sortNode = pageableNode.get("sort").get("org.springframework.data.domain.Sort");
        boolean sorted = sortNode.get("sorted").asBoolean();
        Sort sort = sorted ? Sort.by("id") : Sort.unsorted();  // Ajuste o campo conforme necessário

        // Recriando o objeto Pageable
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Recriando o objeto Page
        long total = node.get("totalElements").asLong();
        return new PageImpl<>(content, pageable, total);
    }
}

