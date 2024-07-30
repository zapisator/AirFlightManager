package domain.value;

import java.time.LocalDateTime;

public record TicketValue(
        String departureCity,
        String arrivalCity,
        String carrier,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        double price
) {
}
