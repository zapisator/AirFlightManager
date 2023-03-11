package domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
