package org.taking.repository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import org.taking.model.CursoSemestreDisciplina;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class CursoSemestreDisciplinaRepository implements PanacheRepository<CursoSemestreDisciplina> {

  public List<CursoSemestreDisciplina> findCursoSemestreDisciplinasByCurso(long cursoId) {
    PanacheQuery<CursoSemestreDisciplina> query = this.find("select distinct csd from CursoSemestreDisciplina csd inner join fetch csd.curso c inner join fetch csd.semestre s inner join fetch csd.disciplina d where c.id = :cursoId order by c.name, s.name, d.name", Parameters.with("cursoId", cursoId));
    return query.list();
  }
  
}
