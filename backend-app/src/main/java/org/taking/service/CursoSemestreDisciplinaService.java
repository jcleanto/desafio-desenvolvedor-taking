package org.taking.service;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.directory.InvalidAttributesException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.Validate;
import org.taking.dto.CursoSemestreDisciplinaDTO;
import org.taking.model.CursoSemestreDisciplina;
import org.taking.model.CursoSemestreDisciplinaKey;
import org.taking.repository.CursoSemestreDisciplinaRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CursoSemestreDisciplinaService {

  private final CursoSemestreDisciplinaRepository cursoSemestreDisciplinaRepository;

  public List<CursoSemestreDisciplina> getCursoSemestreDisciplinas() {
    return cursoSemestreDisciplinaRepository.listAll();
  }

  @Transactional
  public void create(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) throws InvalidAttributesException {
    Validate.notNull(cursoSemestreDisciplinaDTO, "CursoSemestreDisciplinaDTO não pode ser nulo");

    // cria a chave primária composta, utilizando os ids de cada entidade (Curso, Semestre e Disciplina), que estão populados no DTO
    CursoSemestreDisciplinaKey cursoSemestreDisciplinaKey = new CursoSemestreDisciplinaKey(cursoSemestreDisciplinaDTO.getCurso().getId(), cursoSemestreDisciplinaDTO.getSemestre().getId(), cursoSemestreDisciplinaDTO.getDisciplina().getId());
    CursoSemestreDisciplina cursoSemestreDisciplina = new CursoSemestreDisciplina();
    cursoSemestreDisciplina.setCursoSemestreDisciplinaKey(cursoSemestreDisciplinaKey);

    cursoSemestreDisciplinaRepository.persist(cursoSemestreDisciplina);
  }

  @Transactional
  public void delete(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) {
    // realiza uma busca pela chave primária composta
    PanacheQuery<CursoSemestreDisciplina> query = cursoSemestreDisciplinaRepository.find("curso_id = :cursoId and semestre_id = :semestreId and disciplina_id = :disciplinaId", Parameters.with("cursoId", cursoSemestreDisciplinaDTO.getCurso().getId()).and("semestreId", cursoSemestreDisciplinaDTO.getSemestre().getId()).and("disciplinaId", cursoSemestreDisciplinaDTO.getDisciplina().getId()));
    Optional<CursoSemestreDisciplina> cursoSemestreDisciplina = query.singleResultOptional();
    Validate.notNull(cursoSemestreDisciplina, "CursoSemestreDisciplina não pode ser nulo");

    cursoSemestreDisciplinaRepository.delete(cursoSemestreDisciplina.get());
  }
  
}
