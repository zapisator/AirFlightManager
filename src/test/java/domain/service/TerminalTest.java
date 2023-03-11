package domain.service;

import org.apache.commons.cli.CommandLine;
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
        final CommandLine commandLine = TerminalImpl.parseArgs(args);

        assertAll(
                () -> assertEquals("tickets.json", commandLine.getOptionValue("f")),
                () -> assertEquals("Владивосток", commandLine.getOptionValue("d")),
                () -> assertEquals("Тель-Авив", commandLine.getOptionValue("a"))
        );
    }

}