package crud.tech.proof.repository;

import crud.tech.proof.entity.IdName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdNameRepository extends JpaRepository<IdName, UUID> {

    @Query("FROM IdName t WHERE t.id = :id")
    Optional<IdName> findById(UUID id);

    @Query("FROM IdName t WHERE t.active")
    Page<IdName> findAll(Pageable pageable);

    @Query("FROM IdName t WHERE LOWER(t.name) LIKE CONCAT('%',LOWER(:name),'%') and t.active")
    Page<IdName> findAllByName(String name, Pageable pageable);

    @Query(value = "SELECT * FROM ID_NAMES t WHERE fuzzy(t.id, :q, 1) or fuzzy(t.name, :q, 1)", nativeQuery = true)
    Page<IdName> findAllFuzzy(String q, Pageable pageable);

    @Modifying
    @Query("UPDATE IdName t SET t.active = false WHERE t.id = :id")
    void deleteById(UUID id);

}
