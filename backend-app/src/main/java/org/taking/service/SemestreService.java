package org.taking.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import javax.naming.directory.InvalidAttributesException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.taking.model.Semestre;
import org.taking.repository.SemestreRepository;

@ApplicationScoped
@AllArgsConstructor
public class SemestreService {

    private final SemestreRepository semestreRepository;

    public List<Semestre> getSemestres(String semestreName) {
        if (StringUtils.isNotBlank(semestreName)) {
            return semestreRepository.findByName(semestreName);
        }

        return semestreRepository.listAll();
    }

    public Optional<Semestre> findSemestreById(long id) {
        return semestreRepository.findByIdOptional(id);
    }

    @Transactional
    public void create(Semestre semestre) throws InvalidAttributesException {
        if (semestre.getId() != null) {
            throw new InvalidAttributesException("Id tem que ser nulo");
        }
        Validate.notNull(semestre, "Semestre não pode ser nulo");
        Validate.notBlank(semestre.getName(), "Nome do Semestre é obrigatório");

        semestreRepository.persist(semestre);
    }

    @Transactional
    public Semestre replace(long semestreId, Semestre semestre) {
        semestre.setId(semestreId);
        return semestreRepository.update(semestre).orElseThrow(() -> new InvalidParameterException("Semestre não encontrado"));
    }

    @Transactional
    public boolean delete(long semestreId) {
        return semestreRepository.deleteById(semestreId);
    }
  
}
