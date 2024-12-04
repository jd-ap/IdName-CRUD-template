package crud.tech.proof.controller;

import crud.tech.proof.model.IdNameDto;
import crud.tech.proof.service.IdNameService;
import crud.tech.proof.web.IdNamesApi;
import crud.tech.proof.web.WebMapper;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class Id_NamesController implements IdNamesApi {

    private final IdNameService idNameService;

    private final Function<UUID, URI> toLink = id -> linkTo(methodOn(Id_NamesController.class).findById(id)).toUri();

    @Override
    public ResponseEntity<Void> createNew(IdNameDto idNameDto) {
        var entity = WebMapper.requestBodyToEntity(idNameDto);
        var result = idNameService.createOne(entity);

        return WebMapper.toLocationResponseWithId(toLink.apply(result.getId()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<IdNameDto>> findAll(Integer pageNumber, Integer pageSize, Set<String> sort) {
        var pageable = WebMapper.newPageRequest(pageNumber, pageSize, sort);
        var page = idNameService.findAll(pageable);

        return WebMapper.toPageResponse(page);
    }

    @Override
    public ResponseEntity<IdNameDto> findById(UUID id) {
        return idNameService.findOneById(id)
                .map(WebMapper::entityToResponseBody)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @Override
    public ResponseEntity<List<IdNameDto>> findByName(String name, Integer pageNumber, Integer pageSize, Set<String> sort) {
        var pageable = WebMapper.newPageRequest(pageNumber, pageSize, sort);
        var page = idNameService.findAllByNameThatContain(name, pageable);

        return WebMapper.toPageResponse(page);
    }

    @Override
    public ResponseEntity<Void> updateById(UUID id, IdNameDto idNameDto) {
        var entity = WebMapper.requestBodyToEntity(idNameDto);
        var result = idNameService.modifyOneById(id, entity);

        return WebMapper.toLocationResponseWithId(toLink.apply(result.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteById(UUID id) {
        idNameService.deleteOneById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
