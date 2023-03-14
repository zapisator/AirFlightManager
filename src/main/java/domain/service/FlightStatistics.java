package domain.service;

import domain.value.TicketValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightStatistics {

    private final TimeZoneResolver zoneResolver = new TimeZoneResolverImpl();
    @Setter(AccessLevel.PRIVATE)
    private DescriptiveStatistics statistics;

    public static FlightStatistics create(List<TicketValue> tickets, String departure, String arrival) {
        final FlightStatistics flightStatistics = new FlightStatistics();
        final DescriptiveStatistics statistics = flightStatistics.statistics(tickets, departure, arrival);

        flightStatistics.setStatistics(statistics);
        return flightStatistics;
    }

    public double averageFlightTimeInMinutes() {
        return statistics.getMean();
    }

    public double percentile90FlightTime() {
        return statistics.getPercentile(90);
    }

    private DescriptiveStatistics statistics(List<TicketValue> tickets, String origin, String destination) {
        final DescriptiveStatistics statistics = new DescriptiveStatistics();
        final ZoneId departureZone = zoneResolver
                .getTimeZoneForCity(origin);
        final ZoneId arrivalZone = zoneResolver
                .getTimeZoneForCity(destination);

        tickets.stream()
                .filter(ticket -> isThisFlight(origin, destination, ticket))
                .forEach(ticket -> {
                             final ZonedDateTime departure = ticket
                                     .getDepartureDate()
                                     .atZone(departureZone)
                                     .withZoneSameInstant(ZoneId.of("UTC"));
                             final ZonedDateTime arrival = ticket
                                     .getArrivalDate()
                                     .atZone(arrivalZone)
                                     .withZoneSameInstant(ZoneId.of("UTC"));
                             final Duration flightTime = Duration.between(departure, arrival);
                             statistics.addValue(flightTime.toMinutes());
                         }
                );
        return statistics;
    }

    private boolean isThisFlight(String origin, String destination, TicketValue ticket) {
        return ticket.getDepartureCity().equals(origin)
                && ticket.getArrivalCity().equals(destination);
    }

}
