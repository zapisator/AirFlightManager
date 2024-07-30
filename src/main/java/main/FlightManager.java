package main;

import domain.model.Ticket;
import domain.service.CityNameMapper;
import domain.service.FlightStatistics;
import domain.service.TerminalImpl;
import domain.service.TicketReader;
import domain.service.TimeZoneResolver;
import domain.service.TimeZoneResolverImpl;
import domain.value.TicketValue;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FlightManager {
    public static void main(String[] args) throws IOException {
        final TerminalImpl terminal = TerminalImpl.parseArgs(args);
        final CityNameMapper cityNameMapper = new CityNameMapper();
        final String departure = terminal.departureCity();
        final String arrival = terminal.arrivalCity();
        final String filaName = terminal.fileName();
        final Predicate<TicketValue> filterPredicate = ticket ->
                (ticket.departureCity().equals(departure) || ticket.departureCity().equals(arrival))
                        && (ticket.arrivalCity().equals(departure) || ticket.arrivalCity().equals(arrival));
        final List<Ticket> tickets = new TicketReader().readTickets(filaName).getTickets();
        final List<TicketValue> ticketValues = toTicketValues(tickets, cityNameMapper);
        final Function<TicketValue, Double> timeDurationExtractor = getTimeDurationExtractor(departure, arrival);
        final FlightStatistics flightDurationStatistics = FlightStatistics
                .create(ticketValues, timeDurationExtractor, filterPredicate, departure, arrival);

        System.out.println();
        tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getCarrier))
                .entrySet().stream()
                .map(entry -> new AbstractMap.SimpleImmutableEntry<>(
                        entry.getKey(),
                        toTicketValues(entry.getValue(), cityNameMapper)
                ))
                .map(entry -> new AbstractMap.SimpleImmutableEntry<>(
                        entry.getKey(),
                        FlightStatistics.create(
                                entry.getValue(),
                                timeDurationExtractor, filterPredicate,
                                departure, arrival)
                ))
                .forEach(entry -> System.out.printf("Авиаперевозчик: %s, минимальное время: %s [минуты]%n",
                        entry.getKey(),
                        entry.getValue().minimum())
                );
        System.out.println();
        final FlightStatistics priceStatistics = FlightStatistics
                .create(ticketValues, priceExtractor(), filterPredicate, departure, arrival);
        System.out.println();
        System.out.printf(
                """
                        Средня цена полета: %s [рубли]
                        Медиана цен полета: %s [рубли]
                        Разница между средней ценой и медианой: %s [рубли]
                        """,
                priceStatistics.average(),
                priceStatistics.percentile(50),
                priceStatistics.average() - priceStatistics.percentile(50)
        );
        System.out.println();
        System.out.printf(
                """
                        Среднее время полёта между городами Владивосток и Тель-Авив: %s [минуты]
                        90-й процентиль времени Владивосток и Тель-Авив: %s [минуты]
                        Минимальное время полёта между городами: %s [минуты]
                        Медиана времени полёта между городам: %s [минуты]
                        """,
                flightDurationStatistics.average(),
                flightDurationStatistics.percentile(90),
                flightDurationStatistics.minimum(),
                flightDurationStatistics.percentile(50)
        );

    }

    private static Function<TicketValue, Double> priceExtractor() {
        return ticket -> {
            System.out.printf("Авиаперевозчик: %s, из: %s, в: %s, цена: %s%n",
                    ticket.carrier(),
                    ticket.departureCity(),
                    ticket.arrivalCity(),
                    ticket.price()
            );
            return ticket.price();
        };
    }

    private static Function<TicketValue, Double> getTimeDurationExtractor(String departure, String arrival) {
        return ticket -> {
            final TimeZoneResolver zoneResolver = new TimeZoneResolverImpl();
            final ZoneId departureZone = zoneResolver
                    .getTimeZoneForCity(departure);
            final ZoneId arrivalZone = zoneResolver
                    .getTimeZoneForCity(arrival);

            final ZonedDateTime departureDateTime = ticket
                    .departureDate()
                    .atZone(departureZone)
                    .withZoneSameInstant(ZoneId.of("UTC"));
            final ZonedDateTime arrivalDateTime = ticket
                    .arrivalDate()
                    .atZone(arrivalZone)
                    .withZoneSameInstant(ZoneId.of("UTC"));
            final Duration flightTime = Duration.between(departureDateTime, arrivalDateTime);
            System.out.printf("Авиаперевозчик: %s, из: %s, в: %s, Время вылета: %s, "
                            + "Время прилета: %s, Минуты полета: %s%n",
                    ticket.carrier(),
                    ticket.departureCity(),
                    ticket.arrivalCity(),
                    departureDateTime.toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME),
                    arrivalDateTime.toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME),
                    flightTime.toMinutes());
            return (double) flightTime.toMinutes();
        };
    }

    private static List<TicketValue> toTicketValues(List<Ticket> tickets, CityNameMapper cityNameMapper) {
        return tickets
                .stream()
                .map(ticket -> new TicketValue(
                                cityNameMapper.correctCityOrThrow(ticket.getOrigin()),
                                cityNameMapper.correctCityOrThrow(ticket.getDestination()),
                                ticket.getCarrier(),
                                LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime()),
                                LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime()),
                                ticket.getPrice()
                        )
                )
                .toList();
    }
}
