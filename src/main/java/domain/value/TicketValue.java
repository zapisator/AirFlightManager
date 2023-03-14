package domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TicketValue {
    private final String departureCity;
    private final String arrivalCity;

    private final LocalDateTime departureDate;
    private final LocalDateTime arrivalDate;
}
