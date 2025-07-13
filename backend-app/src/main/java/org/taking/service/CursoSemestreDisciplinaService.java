package org.taking.service;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import javax.naming.directory.InvalidAttributesException;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.Validate;
import org.taking.dto.CursoSemestreDisciplinaDTO;
import org.taking.model.Curso;
import org.taking.model.CursoSemestreDisciplina;
import org.taking.model.CursoSemestreDisciplinaKey;
import org.taking.model.Disciplina;
import org.taking.model.Semestre;
import org.taking.repository.CursoRepository;
import org.taking.repository.CursoSemestreDisciplinaRepository;
import org.taking.repository.DisciplinaRepository;
import org.taking.repository.SemestreRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CursoSemestreDisciplinaService {

  private final CursoSemestreDisciplinaRepository cursoSemestreDisciplinaRepository;
  private final CursoRepository cursoRepository;
  private final SemestreRepository semestreRepository;
  private final DisciplinaRepository disciplinaRepository;

  public List<CursoSemestreDisciplina> getCursoSemestreDisciplinas() {
    return cursoSemestreDisciplinaRepository.listAll();
  }

  public List<CursoSemestreDisciplina> findCursoSemestreDisciplinasByCurso(long cursoId) {
    return cursoSemestreDisciplinaRepository.findCursoSemestreDisciplinasByCurso(cursoId);
  }

  @Transactional
  public void create(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) throws InvalidAttributesException {
    Validate.notNull(cursoSemestreDisciplinaDTO, "CursoSemestreDisciplinaDTO não pode ser nulo");

    // cria a chave primária composta, utilizando os ids de cada entidade (Curso, Semestre e Disciplina), que estão populados no DTO
    CursoSemestreDisciplinaKey cursoSemestreDisciplinaKey = new CursoSemestreDisciplinaKey(cursoSemestreDisciplinaDTO.getCurso().getId(), cursoSemestreDisciplinaDTO.getSemestre().getId(), cursoSemestreDisciplinaDTO.getDisciplina().getId());
    Curso curso = cursoRepository.findById(cursoSemestreDisciplinaDTO.getCurso().getId());
    Semestre semestre = semestreRepository.findById(cursoSemestreDisciplinaDTO.getSemestre().getId());
    Disciplina disciplina = disciplinaRepository.findById(cursoSemestreDisciplinaDTO.getDisciplina().getId());
    
    CursoSemestreDisciplina cursoSemestreDisciplina = new CursoSemestreDisciplina();
    cursoSemestreDisciplina.setCursoSemestreDisciplinaKey(cursoSemestreDisciplinaKey);
    cursoSemestreDisciplina.setCurso(curso);
    cursoSemestreDisciplina.setSemestre(semestre);
    cursoSemestreDisciplina.setDisciplina(disciplina);

    cursoSemestreDisciplinaRepository.persist(cursoSemestreDisciplina);
  }

  @Transactional
  public void delete(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) {
    // realiza uma busca pela chave primária composta
    PanacheQuery<CursoSemestreDisciplina> query = cursoSemestreDisciplinaRepository.find("from CursoSemestreDisciplina csd left join fetch csd.curso c left join fetch csd.semestre s left join fetch csd.disciplina d where c.id = :cursoId and s.id = :semestreId and d.id = :disciplinaId", Parameters.with("cursoId", cursoSemestreDisciplinaDTO.getCurso().getId()).and("semestreId", cursoSemestreDisciplinaDTO.getSemestre().getId()).and("disciplinaId", cursoSemestreDisciplinaDTO.getDisciplina().getId()));
    Optional<CursoSemestreDisciplina> cursoSemestreDisciplinaOptional = query.singleResultOptional();
    Validate.notNull(cursoSemestreDisciplinaOptional, "CursoSemestreDisciplina não pode ser nulo");

    if (cursoSemestreDisciplinaOptional.isPresent()) {
      cursoSemestreDisciplinaRepository.delete(cursoSemestreDisciplinaOptional.get());
    }
  }
  
}
