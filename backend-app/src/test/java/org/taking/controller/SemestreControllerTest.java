package org.taking.controller;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.taking.model.Semestre;
import org.taking.service.SemestreService;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SemestreControllerTest {

    @InjectMock SemestreService semestreService;
    @Inject SemestreController semestreController;

    private Semestre semestre;

    @BeforeEach
    void setUp() {
        semestre = new Semestre();
        semestre.setId(1L);
        semestre.setName("1º Semestre");
    }

    @Test
    void getSemestresOK() {
        List<Semestre> semestres = new ArrayList<>();
        semestres.add(semestre);
        Mockito.when(semestreService.getSemestres("")).thenReturn(semestres);
        Response response = semestreController.getSemestres("");
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        List<Semestre> entity = (List<Semestre>) response.getEntity();
        assertFalse(entity.isEmpty());
        assertEquals(1L, entity.get(0).getId());
        assertEquals("1º Semestre", entity.get(0).getName());
    }

    @Test
    void getByIdOK() {
        Mockito.when(semestreService.findSemestreById(1L)).thenReturn(Optional.of(semestre));

        Response response = semestreController.getById(1L);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
        Semestre entity = (Semestre) response.getEntity();
        assertEquals(1L, entity.getId());
        assertEquals("1º Semestre", entity.getName());
    }

    @Test
    void getByIdKO() {
        Mockito.when(semestreService.findSemestreById(1L)).thenReturn(Optional.empty());

        Response response = semestreController.getById(1L);
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }
}
