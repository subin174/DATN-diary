package healthcare.specification;
import healthcare.api.data.ConditionBase;
import healthcare.api.data.FilterBase;
import healthcare.api.data.OperatorBase;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpecificationBase<T> {
    public Specification<T> getSpecificationFromFilters(List<FilterBase> filters) {
        List<Specification<T>> specifications = new ArrayList<>();
        for (FilterBase filter : filters) {
            Specification<T> specification = createSpecification(filter.getOperator(), filter.getField(), filter.getValues());
            if (specifications.size() == 0 || filter.equals(filters.get(0))) {
                specifications.add(specification);
            } else if (ConditionBase.AND.equals(filter.getCondition()) || Objects.isNull(filter.getCondition())) {
                specifications.add(specification);
            } else if (ConditionBase.OR.equals(filter.getCondition())) {
                Specification<T> oldSpecification = specifications.get(specifications.size() - 1);
                specifications.set(specifications.size() - 1,oldSpecification.or(specification));
            }
        }
        Specification<T> specification = Specification.where(null);
        for (Specification<T> specificationItem : specifications) {
            specification = specification.and(specificationItem);
        }
        return specification;
    }

    public Specification<T> createSpecification(OperatorBase operator, String field, List<Object> values) {
        switch (operator) {
            case EQUALS:
                return funcEquals(field, values);
            case NOT_EQ:
                return funcNotEquals(field, values);
            case GREATER_THAN:
                return funcGreaterThan(field, values);
            case LESS_THAN:
                return funcLessThan(field, values);
            case LIKE:
                return funcLike(field, values);
            case IN:
                return funcIn(field, values);
            case BETWEEN:
                return funcBetween(field, values);
            case IS_NULL:
                return funcIsNull(field);
            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    public Specification<T> funcEquals(String field, List<Object> values) {
        return (root, query, cb) -> cb.equal(root.get(field), values.get(0));
    }

    public Specification<T> funcNotEquals(String field, List<Object> values) {
        return (root, query, cb) -> {
            if (Objects.isNull(values) || Objects.isNull(values.get(0))) {
                return cb.isNotNull(root.get(field));
            } else {
                return cb.notEqual(root.get(field), values.get(0));
            }
        };
    }

    public Specification<T> funcGreaterThan(String field, List<Object> values) {
        return (root, query, cb) -> cb.gt(root.get(field), (Number) values.get(0));
    }

    public Specification<T> funcLessThan(String field, List<Object> values) {
        return (root, query, cb) -> cb.lt(root.get(field), (Number) values.get(0));
    }

    public Specification<T> funcLike(String field, List<Object> values) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.like(root.get(field), (String) values.get(0));
        };
    }

    public Specification<T> funcIn(String field, List<Object> values) {
        return (root, query, cb) -> cb.in(root.get(field)).value(values);
    }

    public Specification<T> funcBetween(String field, List<Object> values) {
        return (root, query, cb) -> cb.between(
                resolveFieldComparable(root, field),
                resolveValuesComparable(values.get(0)),
                resolveValuesComparable(values.get(1))
        );
    }

    public Specification<T> funcIsNull(String field) {
        return (root, query, cb) -> cb.isNull(root.get(field));
    }

    public Path<Comparable> resolveFieldComparable(Root<T> root, String field) {
        return resolveField(root, field);
    }

    public <Y> Path<Y> resolveField(Root<T> root, String selector) {
        if (!selector.contains(".")) {
            return root.get(selector);
        }
        String[] segments = selector.split("\\.");
        Join<?, ?> joined = root.join(segments[0]);

        if (segments.length > 2) {
            for (int i = 1; i < segments.length - 1; i++) {
                joined = joined.join(segments[i]);
            }
        }
        return joined.get(segments[segments.length - 1]);
    }

    public <Y> Comparable<Y> resolveValuesComparable(Object object) {
        return (Comparable<Y>) object;
    }


}
