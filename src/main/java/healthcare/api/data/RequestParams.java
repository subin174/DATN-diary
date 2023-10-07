package healthcare.api.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class RequestParams {
    @Builder.Default
    private PageRequest page = PageRequest.of(0, 32);

    @Builder.Default
    private List<FilterReq> filter = new ArrayList<>();

    @Builder.Default
    private List<Sort.Order> sort = new ArrayList<>();

    @Builder.Default
    private String localeCode = "vi_VN";

    @Builder.Default
    private Map<String, String[]> additions = new HashMap<>();

    public PageRequest getPageable () {
        if (sort.size() == 0) {
            return page;
        }

        return PageRequest.of(
            page.getPageNumber(),
            page.getPageSize(),
            Sort.by(sort)
        );
    }

    public String getAdditionsByIndex (String key, Integer index) {
        if (this.additions.get(key) == null || this.additions.get(key).length < index + 1){
            return null;
        }
        return this.additions.get(key)[index];
    }
}
