import domain.service.CityNameMapper;
import domain.service.FlightStatistics;
import domain.service.TerminalImpl;
import domain.service.TicketReader;
import domain.value.TicketValue;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightManager {
    public static void main(String[] args) throws IOException {
        final TerminalImpl terminal = TerminalImpl.parseArgs(args);
        final CityNameMapper cityNameMapper = new CityNameMapper();
        final String departure = terminal.departureCity();
        final String arrival = terminal.arrivalCity();
        final String filaName = terminal.fileName();
        final List<TicketValue> tickets = new TicketReader().readTickets(filaName).getTickets()
                .stream()
                .map(ticket -> new TicketValue(
                             cityNameMapper.correctCityOrThrow(ticket.getOrigin()),
                             cityNameMapper.correctCityOrThrow(ticket.getDestination()),
                             LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime()),
                             LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime())
                     )
                )
                .collect(Collectors.toList());
        final FlightStatistics statsCalculator = FlightStatistics.create(tickets, departure, arrival);

        System.out.printf(
                """
                        Среднее время полета между городами Владивосток и Тель-Авив: %s [минуты]
                        90-й процентиль времени Владивосток и Тель-Авив: %s [минуты]
                        """,
                statsCalculator.averageFlightTimeInMinutes(),
                statsCalculator.percentile90FlightTime()
        );
    }
}
