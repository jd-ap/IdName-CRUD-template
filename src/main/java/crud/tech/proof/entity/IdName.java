package crud.tech.proof.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ID_NAMES")
public class IdName {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;
    @Builder.Default
    private boolean active = Boolean.TRUE;

    public IdName merge(IdName idName) {
        this.name = idName.getName();
        return this;
    }

}
