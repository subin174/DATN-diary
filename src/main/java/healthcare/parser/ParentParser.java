package healthcare.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ParentParser implements Parser {
    private final List<Parser> parsers;

    public ParentParser() {
        this.parsers = new ArrayList<>();
        this.parsers.addAll(Arrays.asList(DefaultParser.values()));
    }

    public void register(Parser parser) {
        this.parsers.add(parser);
    }

    @Override
    public boolean canParse(Class clazz) {
        return findParser(clazz).isPresent();
    }

    @Override
    public Object parse(Class outClazz, String value) {
        try {
            return findParser(outClazz)
                    .orElse(null)
                    .parse(outClazz, value);
        } catch (Exception e) {
            return null;
        }
    }

    private Optional<Parser> findParser(Class clazz) {
        return this.parsers.stream().filter(parser -> parser.canParse(clazz)).findAny();
    }
}
