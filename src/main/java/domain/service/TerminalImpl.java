package domain.service;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TerminalImpl {
    public static CommandLine parseArgs(String... args) {
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
                                .required(true)
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
                                .required(true)
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
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("flight-stats", options);

            System.exit(1);
            return null;
        }
    }
}
