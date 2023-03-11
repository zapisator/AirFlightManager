package domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.model.Ticket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TicketReader {

    public List<Ticket> readTickets(String filename) throws IOException {
        final String jsonString = Files.readString(Path.of(filename));
        final ObjectMapper mapper = new ObjectMapper();
        final CollectionType listType = mapper
                .getTypeFactory()
                .constructCollectionType(List.class, Ticket.class);

        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(jsonString, listType);
    }

}
