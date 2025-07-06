package org.taking.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import org.apache.commons.text.WordUtils;
import org.taking.model.Semestre;

@ApplicationScoped
public class SemestreRepository implements PanacheRepository<Semestre> {

    public void persist(Semestre semestre) {
        var semestreName = WordUtils.capitalize(semestre.getName());

        semestre.setName(semestreName);

        PanacheRepository.super.persist(semestre);
    }

    public List<Semestre> findByName(String semestreName) {
        return this.list("name", WordUtils.capitalize(semestreName));
    }

    public Optional<Semestre> update(Semestre semestre) {
        final var id = semestre.getId();
        var savedOpt = this.findByIdOptional(id);
        if (savedOpt.isEmpty()) {
            return Optional.empty();
        }

        var saved = savedOpt.get();
        saved.setName(semestre.getName());

        return Optional.of(saved);
    }
  
}
