package org.taking.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import org.apache.commons.text.WordUtils;
import org.taking.model.Curso;
import org.taking.model.Disciplina;

@ApplicationScoped
public class DisciplinaRepository implements PanacheRepository<Disciplina> {

    public void persist(Disciplina disciplina) {
        var disciplinaName = WordUtils.capitalize(disciplina.getName());

        disciplina.setName(disciplinaName);

        PanacheRepository.super.persist(disciplina);
    }

    public List<Disciplina> findByName(String disciplinaName) {
        return this.list("name", WordUtils.capitalize(disciplinaName));
    }

    public Optional<Disciplina> update(Disciplina disciplina) {
        final var id = disciplina.getId();
        var savedOpt = this.findByIdOptional(id);
        if (savedOpt.isEmpty()) {
            return Optional.empty();
        }

        var saved = savedOpt.get();
        saved.setName(disciplina.getName());

        return Optional.of(saved);
    }
  
}
