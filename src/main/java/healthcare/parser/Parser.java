package healthcare.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Parser {

    List<Pattern> patterns = Arrays.asList(Pattern.compile(
                    "^(2[0-9][0-9]{2})[-.](0[1-9]|1[0-2])[-.](0[1-9]|[12][0-9]|3[01])" +
                            "[T ](0[0-9]|1[0-9]|2[0-4])[-.]([0-5][0-9])[-.]([0-5][0-9]).*([0-9]*)$"),
            Pattern.compile("^([0-9]*$)")
    );

    boolean canParse(Class clazz);
    Object parse(Class clazz, String value);


}
