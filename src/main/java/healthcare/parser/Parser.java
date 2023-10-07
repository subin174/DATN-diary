package healthcare.parser;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public interface Parser {

    List<DateTimeFormatter> dateTimeFormatters = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    );

    boolean canParse(Class clazz);
    Object parse(Class clazz, String value);
}
