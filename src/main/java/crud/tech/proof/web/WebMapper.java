package crud.tech.proof.web;

import crud.tech.proof.entity.IdName;
import crud.tech.proof.model.IdNameDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebMapper {

    private static final Function<Page<IdName>, List<IdNameDto>> collectingResponse = page ->
            page.get().map(WebMapper::entityToResponseBody)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                        if (result.isEmpty()) throw new EmptyStackException();
                        return result;
                    }));

    private static final Function<Page<IdName>, HttpHeaders> generatePaginationHeaders = page -> {
        // WARN: REMEMBER page.getNumber index starts from zero
        var headers = new HttpHeaders();
        headers.add("x-Pagination-Current", String.valueOf(page.getNumber() + 1));
        headers.add("x-Pagination-Size", String.valueOf(page.getNumberOfElements()));
        headers.add("x-Pagination-Total", String.valueOf(page.getTotalPages()));
        headers.add("x-Pagination-HasNext", String.valueOf(page.hasNext()));
        headers.add("x-Pagination-HasPrevious", String.valueOf(page.hasPrevious()));
        headers.add("x-Pagination-NextPage", String.valueOf(page.hasNext() ? page.getNumber() + 2 : page.getNumber() + 1));
        headers.add("x-Pagination-PreviousPage", String.valueOf(page.hasPrevious() ? page.getNumber() : page.getNumber() + 1));
        return headers;
    };

    public static Pageable newPageRequest(Integer pageNumber, Integer pageSize, Collection<String> sort) {
        var page = null == pageNumber || pageNumber <= 0 ? 0 : pageNumber - 1;
        var size = null == pageSize ? 10 : pageSize;

        if (null == sort || sort.isEmpty())
            return PageRequest.of(page, size);

        var orders = sort.stream()
                .map(it -> it.split(":"))
                .map(it -> {
                    var direction = Sort.Direction.ASC;
                    if (it.length == 2 && "d".equalsIgnoreCase(it[1]))
                        direction = Sort.Direction.DESC;

                    return Sort.by(direction, it[0]);
                })
                .reduce(Sort::and)
                .orElseGet(Sort::unsorted);

        return PageRequest.of(page, size, orders);
    }

    public static IdName requestBodyToEntity(IdNameDto aDto) {
        return IdName.builder().name(aDto.getName()).build();
    }

    public static IdNameDto entityToResponseBody(IdName entity) {
        var dto = new IdNameDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setActive(entity.isActive());
        return dto;
    }

    public static ResponseEntity<List<IdNameDto>> toPageResponse(Page<IdName> page) {

        var result = collectingResponse.apply(page);
        var headers = generatePaginationHeaders.apply(page);

        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    public static ResponseEntity<Void> toLocationResponseWithId(URI uriLocation, HttpStatus httpStatus) {
        var headers = new HttpHeaders();
        headers.add("Location", uriLocation.toString());
        return new ResponseEntity<>(headers, httpStatus);
    }
}
