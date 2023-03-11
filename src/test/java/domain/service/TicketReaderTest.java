package domain.service;

import domain.model.Ticket;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketReaderTest {

    @Test
    void readTickets_okCase() throws IOException {
        final String filename = Objects.requireNonNull(
                getClass().getClassLoader().getResource("tickets.json")
        ).getPath();
        final List<Ticket> tickets = new TicketReader().readTickets(filename);

        assertAll(
                () -> assertEquals(3, tickets.size()),
                () -> assertTrue(tickets.stream()
                                         .allMatch(ticket -> ticket.getArrivalCity().equals("Tel_Aviv")))
        );
    }

}