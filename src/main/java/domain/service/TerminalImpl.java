package domain.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Optional;

import static java.util.Objects.isNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TerminalImpl {
    private final CommandLine commandLine;
    private final CityNameMapper cityNameMapper = new CityNameMapper();

    public static TerminalImpl parseArgs(String... args) {
        final Options options = new Options()
                .addOption(
                        Option.builder("f")
                                .required(true)
                                .longOpt("file")
                                .hasArg()
                                .argName("file name")
                                .numberOfArgs(1)
                                .valueSeparator('=')
                                .desc("JSON file with flight tickets data")
                                .build()
                )
                .addOption(
                        Option.builder("d")
                                .required(false)
                                .hasArg()
                                .argName("departure city")
                                .numberOfArgs(1)
                                .valueSeparator('=')
                                .longOpt("departure")
                                .desc("departure city")
                                .build()
                )
                .addOption(
                        Option.builder("a")
                                .required(false)
                                .hasArg()
                                .argName("arrival city")
                                .numberOfArgs(1)
                                .valueSeparator('=')
                                .longOpt("arrival")
                                .desc("arrival city")
                                .build()
                );
        final CommandLineParser parser = new DefaultParser();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            final CommandLine commandLine1 = parser.parse(options, args);

            return new TerminalImpl(commandLine1);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("flight-stats", options);

            System.exit(1);
            return null;
        }
    }

    public String departureCity() {
        final String citySetOrDefault = Optional
                .ofNullable(commandLine.getOptionValue("d"))
                .orElse("Vladivostok");
        return cityNameMapper.correctCityOrThrow(citySetOrDefault);
    }

    public String arrivalCity() {
        final String citySetOrDefault = Optional
                .ofNullable(commandLine.getOptionValue("a"))
                .orElse("Tel_Aviv");
        return cityNameMapper.correctCityOrThrow(citySetOrDefault);
    }

    public String fileName() {
        final String fileName = commandLine.getOptionValue("f");

        if (!isNull(fileName)) {
            return fileName;
        }
        throw new RuntimeException("Требуется указать путь до файла");
    }

}
