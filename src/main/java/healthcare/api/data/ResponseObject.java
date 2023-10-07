package healthcare.api.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder
@Data
public class ResponseObject<T>
{
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ResponseStatus status = ResponseStatus.SUCCESS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponsePaginate paginate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
}
