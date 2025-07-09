package org.taking.repository;

import javax.enterprise.context.ApplicationScoped;
import org.taking.model.CursoSemestreDisciplina;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class CursoSemestreDisciplinaRepository implements PanacheRepository<CursoSemestreDisciplina> {
  
}
