package crud.tech.proof.service;

import crud.tech.proof.entity.IdName;
import crud.tech.proof.repository.IdNameRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public non-sealed class IdNameServiceImpl implements IdNameService {

    private final IdNameRepository idNameRepository;

    @Override
    public Page<IdName> findAll(Pageable pageable) {
        return idNameRepository.findAll(pageable);
    }

    public Page<IdName> findallFuzzy(String q, Pageable pageable) {
        return idNameRepository.findAllFuzzy(q, pageable);
    }

    @Override
    public Optional<IdName> findOneById(UUID id) {
        return idNameRepository.findById(id);
    }

    @Override
    public Page<IdName> findAllByNameThatContain(String name, Pageable pageable) {
        return idNameRepository.findAllByName(name, pageable);
    }

    @Override
    public IdName createOne(IdName idName) {
        return idNameRepository.save(idName);
    }

    @Override
    @Transactional
    public IdName modifyOneById(UUID id, IdName idName) {
        return idNameRepository.findById(id)
                .filter(IdName::isActive)
                .map(it -> it.merge(idName))
                .map(idNameRepository::save)
                .orElseThrow();
    }

    @Override
    @Transactional
    public void deleteOneById(UUID id) {
        idNameRepository.deleteById(id);
    }

}
