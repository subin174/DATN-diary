package healthcare.api.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterBase {
    private String field;
    private List<Object> values;
    private OperatorBase operator;
    private ConditionBase condition;

    public FilterBase(FilterReq filter, List<Object> values) {
        this.field = filter.getField();
        this.operator = filter.getOperator();
        this.condition = filter.getCondition();
        this.values = values;
    }
}
