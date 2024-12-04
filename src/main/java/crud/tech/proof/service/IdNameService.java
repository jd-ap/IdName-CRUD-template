package crud.tech.proof.service;

import crud.tech.proof.entity.IdName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public sealed interface IdNameService permits IdNameServiceImpl {

    Page<IdName> findAll(Pageable pageable);

    Optional<IdName> findOneById(UUID id);

    Page<IdName> findAllByNameThatContain(String name, Pageable pageable);

    IdName createOne(IdName idName);

    IdName modifyOneById(UUID id, IdName idName);

    void deleteOneById(UUID id);
}
