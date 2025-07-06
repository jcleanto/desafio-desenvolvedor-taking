package org.taking.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import org.apache.commons.text.WordUtils;
import org.taking.model.Curso;

@ApplicationScoped
public class CursoRepository implements PanacheRepository<Curso> {

    public void persist(Curso curso) {
        var cursoName = WordUtils.capitalize(curso.getName());

        curso.setName(cursoName);

        PanacheRepository.super.persist(curso);
    }

    public List<Curso> findByName(String cursoName) {
        return this.list("name", WordUtils.capitalize(cursoName));
    }

    public Optional<Curso> update(Curso curso) {
        final var id = curso.getId();
        var savedOpt = this.findByIdOptional(id);
        if (savedOpt.isEmpty()) {
            return Optional.empty();
        }

        var saved = savedOpt.get();
        saved.setName(curso.getName());

        return Optional.of(saved);
    }

}
