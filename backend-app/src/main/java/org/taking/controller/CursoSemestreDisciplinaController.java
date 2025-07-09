package org.taking.controller;

import java.util.Map;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.taking.dto.CursoSemestreDisciplinaDTO;
import org.taking.model.CursoSemestreDisciplina;
import org.taking.model.CursoSemestreDisciplinaKey;
import org.taking.service.CursoSemestreDisciplinaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Path("/api/v1/cursosemestredisciplina")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoSemestreDisciplinaController {

  private final CursoSemestreDisciplinaService cursoSemestreDisciplinaService;

  @GET
  public Response getCursoSemestreDisciplinas() {
    try {
      return Response.ok(cursoSemestreDisciplinaService.getCursoSemestreDisciplinas()).build();
    } catch (Exception e) {
      return Response.status(Status.BAD_REQUEST).build();
    }
  }

  @POST
  public Response create(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) {
    try {
      CursoSemestreDisciplinaKey cursoSemestreDisciplinaKey = new CursoSemestreDisciplinaKey(cursoSemestreDisciplinaDTO.getCurso().getId(), cursoSemestreDisciplinaDTO.getSemestre().getId(), cursoSemestreDisciplinaDTO.getDisciplina().getId());
      CursoSemestreDisciplina cursoSemestreDisciplina = new CursoSemestreDisciplina();
      cursoSemestreDisciplina.setCursoSemestreDisciplinaKey(cursoSemestreDisciplinaKey);
      cursoSemestreDisciplinaService.create(cursoSemestreDisciplina);

      return Response.noContent().build();
    } catch (Exception e) {
      if (e instanceof InvalidAttributesException) {
        return Response.status(Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
      }

      return Response.status(Status.BAD_REQUEST).build();
    }
  }
  
  @POST
  @Path("/delete")
  public Response delete(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) {
    try {
      cursoSemestreDisciplinaService.delete(cursoSemestreDisciplinaDTO);
      return Response.noContent().build();
    } catch (Exception e) {
      return Response.status(Status.BAD_REQUEST).build();
    }
  }
}
