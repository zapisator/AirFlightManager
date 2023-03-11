import domain.model.Ticket;
import domain.service.FlightStatistics;
import domain.service.TerminalImpl;
import domain.service.TicketReader;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.List;

public class FlightManager {
    public static void main(String[] args) throws IOException {
        final CommandLine commandLine = TerminalImpl.parseArgs(args);
        final String departure = commandLine.getOptionValue("d");
        final String arrival = commandLine.getOptionValue("a");
        final String filaName = commandLine.getOptionValue("f");
        final List<Ticket> tickets = new TicketReader().readTickets(filaName);
        final FlightStatistics statsCalculator = FlightStatistics.create(tickets, departure, arrival);

        System.out.printf(
                """
                        Среднее время полета между городами Владивосток и Тель-Авив: %s
                        90-й процентиль времени Владивосток и Тель-Авив: %s
                        """,
                statsCalculator.averageFlightTimeInMinutes(),
                statsCalculator.percentile90FlightTime());
    }
}
