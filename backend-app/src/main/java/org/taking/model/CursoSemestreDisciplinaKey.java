package org.taking.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "semestre_id")
    private Long semestreId;

    @Column(name = "disciplina_id")
    private Long disciplinaId;
}
