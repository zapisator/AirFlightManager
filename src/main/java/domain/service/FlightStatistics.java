package domain.service;

import domain.value.TicketValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightStatistics {

    @Setter(AccessLevel.PRIVATE)
    private DescriptiveStatistics statistics;

    public static FlightStatistics create(
            List<TicketValue> tickets,
            Function<TicketValue, Double> statisticsPropertyExtractor,
            Predicate<TicketValue> propertyFilter,
            String departure,
            String arrival
    ) {
        final FlightStatistics flightStatistics = new FlightStatistics();

        flightStatistics
                .defineStatistics(
                        tickets,
                        statisticsPropertyExtractor, propertyFilter,
                        departure, arrival
                );
        return flightStatistics;
    }

    public double average() {
        return statistics.getMean();
    }

    public double minimum() {
        return statistics.getMin();
    }

    public double percentile(double percentile) {
        return statistics.getPercentile(percentile);
    }

    private void defineStatistics(
            List<TicketValue> tickets,
            Function<TicketValue, Double> propertyExtractor, Predicate<TicketValue> propertyFilter,
            String origin, String destination
    ) {
        statistics = new DescriptiveStatistics();

        statistics.setPercentileImpl(new Percentile()
                .withEstimationType(Percentile.EstimationType.R_2)
        );
        tickets.stream()
                .filter(propertyFilter)
                .forEach(ticket -> statistics.addValue(propertyExtractor.apply(ticket)));
    }

}
