package domain.service;

import domain.model.Ticket;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class FlightStatisticsTest {

    @Test
    void averageFlightTimeInMinutes_withCorrectSet_ok() {
        String vladivostok = "Vladivostok";
        String telAviv = "Tel_Aviv";
        final List<Ticket> tickets = List.of(
                new Ticket(
                        vladivostok,
                        telAviv,
                        LocalDateTime.parse("2022-08-01T10:30:00"),
                        LocalDateTime.parse("2022-08-01T14:10:00")
                ),
                new Ticket(
                        vladivostok,
                        telAviv,
                        LocalDateTime.parse("2022-07-25T16:20:00"),
                        LocalDateTime.parse("2022-07-25T19:45:00")
                ),
                new Ticket(
                        vladivostok,
                        telAviv,
                        LocalDateTime.parse("2022-09-12T08:45:00"),
                        LocalDateTime.parse("2022-09-12T19:30:00")
                )
        );
        final double averageTimeInMinutes = FlightStatistics.create(tickets, vladivostok, telAviv)
                .averageFlightTimeInMinutes();

        assertThat(averageTimeInMinutes, closeTo(776.6, 0.5));
    }


}