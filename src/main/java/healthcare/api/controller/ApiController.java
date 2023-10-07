package healthcare.api.controller;
import healthcare.api.data.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public abstract class ApiController {
    @Autowired
    protected HttpServletRequest request;

    protected List<String> getSortableFields() {
        return Collections.singletonList("id");
    }

    protected List<String> getFilterableFields() {
        return Collections.singletonList("id");
    }

    protected final List<String> ignored = Arrays.asList("page", "limit", "filter", "sort");

    private String getFilterPattern() {
        return String.format("^(%s):\\[(.*)\\]$", String.join("|", getFilterableFields()));
    }

    private OperatorBase getFilterOperator(String operator) {
        try {
            return OperatorBase.valueOf(operator);
        } catch (Exception e) {
            throw new RuntimeException("Unsupported filter operator");
        }
    }

    private FilterReq parseFilterExpression(String expression) {
        String field = expression.substring(0, expression.indexOf(":"));
        String value = expression.substring(expression.indexOf(":") + 1);

        JSONArray result = new JSONArray(value);
        if (result.length() < 2)
            return null;
        String operatorString = result.remove(0).toString();
        ConditionBase condition = ConditionBase.AND;
        if (operatorString.startsWith(ConditionBase.OR.name() + "_")){
            condition = ConditionBase.OR;
            operatorString = operatorString.substring(3);
        }
        OperatorBase operator = getFilterOperator(operatorString);
        List<String> values = IteratorUtils.toList(result.iterator())
                .stream().map(o -> getValue(operator,o))
                .collect(Collectors.toList());

        return FilterReq.builder()
                .field(field)
                .operator(operator)
                .values(values)
                .condition(condition)
                .build();
    }

    private String getValue(OperatorBase operator,Object o){
        String resp = o.toString();
        if (operator.equals(OperatorBase.LIKE)){
            resp = "%" + resp + "%";
        }
        return resp;
    }

    private Sort.Order parseSortingExpression(String expression) {
        String[] segments = expression.split(":");
        String sortDirection = segments.length > 1 ? segments[1] : "asc";

        return sortDirection.equalsIgnoreCase("asc")
                ? Sort.Order.asc(segments[0])
                : Sort.Order.desc(segments[0]);
    }

    protected RequestParams getParams() {
        Map<String, String[]> params = request.getParameterMap();


        // Pagination parameters
        int page = params.containsKey("page") ? Integer.parseInt(request.getParameter("page")) : 1;
        int limit = params.containsKey("limit") ? Integer.parseInt(request.getParameter("limit")) : 10;

        RequestParams.RequestParamsBuilder builder = RequestParams.builder()
                .page(PageRequest.of(Math.max(page - 1, 0), Math.max(limit, 1)));

        String localeCode = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (localeCode != null && localeCode.matches("^([a-z]{2})_([A-Z]{2})$")) {
            builder.localeCode(localeCode);
        }

        // Filter parameters
        if (params.containsKey("filter")) {
            List<FilterReq> filters = Arrays.stream(request.getParameterValues("filter"))
                    .filter(rawFilter -> rawFilter.matches(getFilterPattern()))
                    .map(this::parseFilterExpression)
                    .collect(Collectors.toList());
            builder.filter(filters);
        }

        // Prepare sorting parameters
        if (params.containsKey("sort")) {
            List<String> sortableFields = getSortableFields();
            List<Sort.Order> sorting = Arrays.stream(request.getParameterValues("sort"))
                    .map(this::parseSortingExpression)
                    .filter(order -> sortableFields.contains(order.getProperty()))
                    .collect(Collectors.toList());

            builder.sort(sorting);
        } else {
            List<String> sortableField = getSortableFields();
            List<Sort.Order> defaultSorting = Stream.of("id:desc")
                    .map(this::parseSortingExpression)
                    .filter(order -> sortableField.contains(order.getProperty()))
                    .collect(Collectors.toList());
            builder.sort(defaultSorting);
        }

        return builder
                .additions(
                        params.entrySet().stream()
                                .filter(entry -> !ignored.contains(entry.getKey()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                )
                .build();
    }

    protected ResponseEntity<?> responseSuccess(Object data) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .data(data)
                        .build()
        );
    }

    protected ResponseEntity<?> responseSuccess(List<?> data) {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .data(data)
                        .build()
        );
    }

    protected ResponseEntity<?> responseSuccess(Page<?> data) {
        ResponseObject<?> responseObject = ResponseObject.builder()
                .data(data.getContent())
                .paginate(
                        ResponsePaginate.builder()
                                .currentPage(data.getPageable().getPageNumber() + 1)
                                .totalItems(data.getTotalElements())
                                .totalPages(data.getTotalPages())
                                .build()
                )
                .build();
        return ResponseEntity.ok(responseObject);
    }

    protected ResponseEntity<?> responseSuccess() {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .build()
        );
    }
}
