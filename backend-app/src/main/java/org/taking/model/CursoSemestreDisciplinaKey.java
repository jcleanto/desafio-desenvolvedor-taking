package org.taking.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CursoSemestreDisciplinaKey implements Serializable {

    @Column(name = "cursoId")
    private Long cursoId;

    @Column(name = "semestreId")
    private Long semestreId;

    @Column(name = "disciplinaId")
    private Long disciplinaId;
}
