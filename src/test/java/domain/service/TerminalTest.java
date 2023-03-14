package domain.service;

import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TerminalTest {

    @RepeatedTest(10)
    void correctArgumentsAtAnyOrder_ok() {
        final List<String> argsList = new ArrayList<>(
                List.of("-f=tickets.json", "-d=Владивосток", "-a=Тель-Авив")
        );

        Collections.shuffle(argsList);
        final String[] args = argsList.toArray(new String[]{});
        final TerminalImpl terminal = TerminalImpl.parseArgs(args);

        assertAll(
                () -> assertEquals("tickets.json", terminal.fileName()),
                () -> assertEquals("Vladivostok", terminal.departureCity()),
                () -> assertEquals("Tel_Aviv", terminal.arrivalCity())
        );
    }

}