package domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.Tickets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TicketReader {

    public Tickets readTickets(String filename) throws IOException {
        final String jsonString = Files.readString(Path.of(filename));

        return new ObjectMapper()
                .findAndRegisterModules()
                .readValue(jsonString, Tickets.class);
    }

}
