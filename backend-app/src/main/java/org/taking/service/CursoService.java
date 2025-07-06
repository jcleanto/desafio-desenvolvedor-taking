package org.taking.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.directory.InvalidAttributesException;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.taking.model.Curso;
import org.taking.repository.CursoRepository;

@ApplicationScoped
@AllArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public List<Curso> getCursos(String cursoName) {
        if (StringUtils.isNotBlank(cursoName)) {
            return cursoRepository.findByName(cursoName);
        }

        return cursoRepository.listAll();
    }

    public Optional<Curso> findCursoById(long id) {
        return cursoRepository.findByIdOptional(id);
    }

    @Transactional
    public void create(Curso curso) throws InvalidAttributesException {
        if (curso.getId() != null) {
            throw new InvalidAttributesException("Id tem que ser nulo");
        }
        Validate.notNull(curso, "Curso não pode ser nulo");
        Validate.notBlank(curso.getName(), "Nome do Curso é obrigatório");

        cursoRepository.persist(curso);
    }

    @Transactional
    public Curso replace(long cursoId, Curso curso) {
        curso.setId(cursoId);
        return cursoRepository.update(curso).orElseThrow(() -> new InvalidParameterException("Curso não encontrado"));
    }

    @Transactional
    public boolean delete(long cursoId) {
        return cursoRepository.deleteById(cursoId);
    }
}
