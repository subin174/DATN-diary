package healthcare.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePaginate {
    private long totalPages;
    private long totalItems;
    private int currentPage;

    public ResponsePaginate(Page page) {
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.currentPage = page.getPageable().getPageNumber() + 1;
    }
}
