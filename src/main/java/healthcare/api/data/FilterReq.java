package healthcare.api.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FilterReq {
    private String field;
    private List<String> values;
    private OperatorBase operator;
    private ConditionBase condition;
}
