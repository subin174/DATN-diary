package healthcare.api.service;

import healthcare.api.data.FilterBase;
import healthcare.api.data.FilterReq;
import healthcare.parser.ParentParser;
import healthcare.specification.SpecificationBase;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService<T> implements SimpleService<T> {


//    @FunctionalInterface
//    public interface CriteriaBuilderConsumer {
//        Predicate accept(Expression<?> var1, Object var2);
//    }

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    protected final ModelMapper mapper = new ModelMapper();

    protected final ParentParser parentParser = new ParentParser();

    protected abstract <TRepository extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> TRepository getRepository();

    protected abstract Class<T> getType();

    private SpecificationBase<T> specification = new SpecificationBase<>();

    private RuntimeException notFoundException(long id) {
        return new RuntimeException(String.valueOf(id));
    }

    private Object castToRequiredType(Class fieldType, String value) {
        return parentParser.parse(fieldType, value);
    }

    private List<Object> castToRequiredType(Class fieldType, List<String> value) {
        try {
            return value.stream().map(v -> this.castToRequiredType(fieldType, v)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class getFieldType(String fieldStr ){
        Class<T> tClass = getType();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldStr.contains(fieldName)) {
                return field.getType();
            }
        }
        return null;
    }

    public List<FilterBase> filterToFilterBases(List<FilterReq> filters) {
        List<FilterBase> bases = filters.stream()
                .filter(filter -> Objects.nonNull(this.getFieldType(filter.getField())))
                .map(filter ->
                        FilterBase.builder()
                                .field(filter.getField())
                                .values(castToRequiredType(this.getFieldType(filter.getField()),filter.getValues()))
                                .operator(filter.getOperator())
                                .condition(filter.getCondition())
                                .build())
                .collect(Collectors.toList());
        return bases;
    }

    public Specification<T> buildSpecification(List<FilterReq> filters) throws Exception {
        if (Objects.isNull(filters) || filters.size() == 0){
            return null;
        }
        List<FilterBase> filterBases = filterToFilterBases(filters);
        return specification.getSpecificationFromFilters(filterBases);
    }

    @Override
    public T reqToEntity(Object req, T entity) throws Exception {
        Class<?> entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        Class<?> reqClass = req.getClass();
        List<String> reqFields = Arrays.stream(reqClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        for (Field field : fields) {
            String fieldName = field.getName();
            Field reqField = null;
            if (reqFields.contains(fieldName)) {
                reqField = reqClass.getDeclaredField(fieldName);
            }
            if (Objects.isNull(reqField)) {
                continue;
            }
            reqField.setAccessible(true);
            Object fieldValue = reqField.get(req);
            if (Objects.isNull(fieldValue)) {
                continue;
            }
            Object value = getFieldValue(field, reqField, fieldValue);
            field.setAccessible(true);
            field.set(entity, value);
        }
        return entity;
    }

    public <D> D entityToResp(Object source, Class<D> destinationType) {
        return this.mapper.map(source, destinationType);

    }

    private Object getFieldValue(Field entityField, Field reqField, Object value) {
        try {
            if (entityField.getType().isAssignableFrom(LocalDateTime.class)) {
                if (reqField.getType().isAssignableFrom(String.class)) {
                    return LocalDateTime.parse(value.toString(), this.dateTimeFormatter);
                }
                if (reqField.getType().isAssignableFrom(Long.class)) {
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(value.toString())),
                            TimeZone.getDefault().toZoneId());
                }
            }
            if (entityField.getType().isAssignableFrom(reqField.getType())){
                return value;
            }
            return castToRequiredType(entityField.getType(),value.toString());
        } catch (Exception e) {
            log.info("Error parse req data");
        }
        return null;
    }

    @Override
    public Page<T> getPaginated(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    public Page<T> getPaginated(Specification<T> specification, Pageable pageable) {
        return (Objects.isNull(specification))
                ? getRepository().findAll(pageable)
                : getRepository().findAll(specification, pageable);
    }

    @Override
    public List<T> getAll() {
        return getRepository().findAll();
    }

    @Override
    public List<T> getAll(Specification<T> specification) {
        return (Objects.isNull(specification))
                ? getRepository().findAll()
                : getRepository().findAll(specification);
    }

    @Override
    public T getById(Long id) {
        return getRepository().findById(id).orElseThrow(() -> notFoundException(id));
    }

    @Override
    public void deleteById(Long id) {
        getRepository().deleteById(id);
    }

    @Override
    public T save(T entity){
        return getRepository().save(entity);
    }

    @Override
    public List<T> saveAll(List<T> tList){
        return getRepository().saveAll(tList);
    }
}