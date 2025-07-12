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
import org.taking.model.Disciplina;
import org.taking.repository.DisciplinaRepository;

@ApplicationScoped
@AllArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public List<Disciplina> getDisciplinas(String disciplinaName) {
        if (StringUtils.isNotBlank(disciplinaName)) {
            return disciplinaRepository.findByName(disciplinaName);
        }

        return disciplinaRepository.listAll();
    }

    public Optional<Disciplina> findDisciplinaById(long id) {
        return disciplinaRepository.findByIdOptional(id);
    }

    @Transactional
    public void create(Disciplina disciplina) throws InvalidAttributesException {
        if (disciplina.getId() != null) {
            throw new InvalidAttributesException("Id tem que ser nulo");
        }
        Validate.notNull(disciplina, "Disciplina não pode ser nula");
        Validate.notBlank(disciplina.getName(), "Nome da Disciplina é obrigatório");

        disciplinaRepository.persist(disciplina);
    }

    @Transactional
    public Disciplina replace(long disciplinaId, Disciplina disciplina) {
        disciplina.setId(disciplinaId);
        return disciplinaRepository.update(disciplina).orElseThrow(() -> new InvalidParameterException("Disciplina não encontrada"));
    }

    @Transactional
    public boolean delete(long disciplinaId) {
        return disciplinaRepository.deleteById(disciplinaId);
    }
  
}
