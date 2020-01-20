package io.crypto.beer.telegram.bot.engine.entity.pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginationSession {

    private Integer totalPages;
    @Builder.Default
    private Integer currentPage = 0;

    private String paginationServiceName;

    private List<IPaginationEntity> paginationValues;
    private IPaginationEntity selectedPaginationValue;

    @Builder.Default
    private Map<String, String> requestParameters = new HashMap<>();

    public void changePaginationService(String paginationServiceName) {
        this.paginationServiceName = paginationServiceName;
        currentPage = 0;
        requestParameters = new HashMap<>();
    }

    public void addReqestParameter(String name, String value) {
        requestParameters.put(name, value);
    }

    public void setSelectedValue(Integer valuePosition) {
        selectedPaginationValue = paginationValues.get(valuePosition);
    }

    public void previousPage() {
        currentPage--;
    }

    public void nextPage() {
        currentPage++;
    }
}
