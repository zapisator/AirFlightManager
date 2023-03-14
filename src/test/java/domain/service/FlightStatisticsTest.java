package domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.Tickets;
import domain.value.TicketValue;
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
        final List<TicketValue> tickets = List.of(
                new TicketValue(
                        vladivostok,
                        telAviv,
                        LocalDateTime.parse("2022-08-01T10:30:00"),
                        LocalDateTime.parse("2022-08-01T14:10:00")
                ),
                new TicketValue(
                        vladivostok,
                        telAviv,
                        LocalDateTime.parse("2022-07-25T16:20:00"),
                        LocalDateTime.parse("2022-07-25T19:45:00")
                ),
                new TicketValue(
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

    @Test
    void averageFlightTimeInMinutes_realFile_ok() throws JsonProcessingException {
        final String ticketsJson =
                """
                        {
                          "tickets": [
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "16:20",
                              "arrival_date": "12.05.18",
                              "arrival_time": "22:10",
                              "carrier": "TK",
                              "stops": 3,
                              "price": 12400
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "17:20",
                              "arrival_date": "12.05.18",
                              "arrival_time": "23:50",
                              "carrier": "S7",
                              "stops": 1,
                              "price": 13100
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "12:10",
                              "arrival_date": "12.05.18",
                              "arrival_time": "18:10",
                              "carrier": "SU",
                              "stops": 0,
                              "price": 15300
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "17:00",
                              "arrival_date": "12.05.18",
                              "arrival_time": "23:30",
                              "carrier": "TK",
                              "stops": 2,
                              "price": 11000
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "12:10",
                              "arrival_date": "12.05.18",
                              "arrival_time": "20:15",
                              "carrier": "BA",
                              "stops": 3,
                              "price": 13400
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "9:40",
                              "arrival_date": "12.05.18",
                              "arrival_time": "19:25",
                              "carrier": "SU",
                              "stops": 3,
                              "price": 12450
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "17:10",
                              "arrival_date": "12.05.18",
                              "arrival_time": "23:45",
                              "carrier": "TK",
                              "stops": 1,
                              "price": 13600
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "6:10",
                              "arrival_date": "12.05.18",
                              "arrival_time": "15:25",
                              "carrier": "TK",
                              "stops": 0,
                              "price": 14250
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "16:50",
                              "arrival_date": "12.05.18",
                              "arrival_time": "23:35",
                              "carrier": "SU",
                              "stops": 1,
                              "price": 16700
                            },
                            {
                              "origin": "VVO",
                              "origin_name": "Владивосток",
                              "destination": "TLV",
                              "destination_name": "Тель-Авив",
                              "departure_date": "12.05.18",
                              "departure_time": "6:10",
                              "arrival_date": "12.05.18",
                              "arrival_time": "16:15",
                              "carrier": "S7",
                              "stops": 0,
                              "price": 17400
                            }
                          ]
                        }
                        """;
        final List<TicketValue> tickets = new ObjectMapper()
                .findAndRegisterModules()
                .readValue(ticketsJson, Tickets.class)
                .getTickets()
                .stream()
                .map(ticket -> new TicketValue(
                        "Vladivostok",
                        "Tel_Aviv",
                        LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime()),
                        LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime())))
                .toList();

                final double averageTimeInMinutes = FlightStatistics
                        .create(tickets, "Vladivostok", "Tel_Aviv")
                .averageFlightTimeInMinutes();

        assertThat(averageTimeInMinutes, closeTo(872.0, 0.5));

    }


}