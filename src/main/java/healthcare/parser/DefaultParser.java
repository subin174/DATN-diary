package healthcare.parser;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public enum DefaultParser implements Parser {
    STRING {
        @Override
        public boolean canParse(Class clazz) {
            return String.class.isAssignableFrom(clazz);
        }

        @Override
        public Object parse(Class clazz, String value) {
            return value;
        }
    },
    ENUM {
        @Override
        public boolean canParse(Class clazz) {
            return Enum.class.isAssignableFrom(clazz);
        }

        @Override
        public Object parse(Class clazz, String value) {
            return Enum.valueOf(clazz, value);
        }
    },
    BOOLEAN {
        @Override
        public boolean canParse(Class clazz) {
            return Boolean.class.isAssignableFrom(clazz);
        }

        @Override
        public Object parse(Class clazz, String value){
            return value.equalsIgnoreCase("true");
        }
    },
    LONG {
        @Override
        public boolean canParse(Class clazz) {
            return Long.class.isAssignableFrom(clazz) || clazz.getName().equals("long");
        }

        @Override
        public Object parse(Class clazz, String value){
            return Long.valueOf(value);
        }

    },
    INTEGER {
        @Override
        public boolean canParse(Class clazz) {
            return Integer.class.isAssignableFrom(clazz) || clazz.getName().equals("int");
        }

        @Override
        public Object parse(Class clazz, String value){
            return Integer.valueOf(value);
        }
    },
    DOUBLE {
        @Override
        public boolean canParse(Class clazz) {
            return Double.class.isAssignableFrom(clazz) || clazz.getName().equals("double");
        }

        @Override
        public Object parse(Class clazz, String value){
            return Double.valueOf(value);
        }
    },
    FLOAT {
        @Override
        public boolean canParse(Class clazz) {
            return Float.class.isAssignableFrom(clazz);
        }

        @Override
        public Object parse(Class clazz, String value){
            return Float.valueOf(value);
        }
    },
    BIG_DECIMAL {
        @Override
        public boolean canParse(Class clazz) {
            return BigDecimal.class.isAssignableFrom(clazz);
        }

        @Override
        public Object parse(Class clazz, String value){
            return new BigDecimal(value);
        }
    },
    LOCAL_DATETIME {
        @Override
        public boolean canParse(Class clazz) {
            return LocalDateTime.class.isAssignableFrom(clazz);
        }

        @Override
        public Object parse(Class clazz, String value){
            try {
                for (Pattern r: patterns){
                    Matcher m = r.matcher(value);
                    if (!m.find())return null;
                    List<String> values = new ArrayList<>();
                    for (int i = 1; i <= m.groupCount(); i++) {
                        if (m.group(i).length() > 0)
                            values.add(m.group(i));
                    }
                    if (patterns.get(0).equals(r)){
                        List<Integer> ints = values.stream().map(Integer::parseInt).collect(Collectors.toList());
                        LocalDate date = LocalDate.of(ints.get(0), ints.get(1), ints.get(2));
                        LocalTime time = LocalTime.of(ints.get(3), ints.get(4), ints.get(5));
                        return  LocalDateTime.of(date,time);
                    }
                    if (patterns.get(1).equals(r)){
                        return LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(values.get(0))),
                                TimeZone.getDefault().toZoneId()
                        );
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                log.info("Error parse req data");
            }
            return null;
        }
    }
}
