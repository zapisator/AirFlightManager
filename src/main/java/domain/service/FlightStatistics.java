package domain.service;

import domain.model.Ticket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.LongSummaryStatistics;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightStatistics {

    private final TimeZoneResolver zoneResolver = new TimeZoneResolverImpl();
    @Setter(AccessLevel.PRIVATE)
    private LongSummaryStatistics statistics;

    public static FlightStatistics create(List<Ticket> tickets, String departure, String arrival) {
        final FlightStatistics flightStatistics = new FlightStatistics();
        final LongSummaryStatistics statistics = flightStatistics.statistics(tickets, departure, arrival);

        flightStatistics.setStatistics(statistics);
        return flightStatistics;
    }

    public double averageFlightTimeInMinutes() {
        return statistics.getAverage();
    }

    public double percentile90FlightTime() {
        return 0.9 * statistics.getCount();
    }

    private LongSummaryStatistics statistics(List<Ticket> tickets, String origin, String destination) {
        final LongSummaryStatistics statistics = new LongSummaryStatistics();

        tickets.stream()
                .filter(ticket -> isThisFlight(origin, destination, ticket))
                .forEach(ticket -> {
                             final ZoneId departureZone = zoneResolver.getTimeZoneForCity(origin);
                             final ZoneId arrivalZone = zoneResolver.getTimeZoneForCity(destination);
                             final ZonedDateTime departure = ticket
                                     .getDepartureDate()
                                     .atZone(departureZone)
                                     .withZoneSameInstant(ZoneId.of("UTC"));
                             final ZonedDateTime arrival = ticket
                                     .getArrivalDate()
                                     .atZone(arrivalZone)
                                     .withZoneSameInstant(ZoneId.of("UTC"));
                             final Duration flightTime = Duration.between(departure, arrival);

                             statistics.accept(flightTime.toMinutes());
                         }
                );
        return statistics;
    }

    private boolean isThisFlight(String origin, String destination, Ticket ticket) {
        return ticket.getDepartureCity().equals(origin)
                && ticket.getArrivalCity().equals(destination);
    }

}
