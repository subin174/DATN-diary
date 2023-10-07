package healthcare.parser;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

            }catch (Exception e){
                log.info("Error parse req data");
            }
            return null;
        }
    }
}
