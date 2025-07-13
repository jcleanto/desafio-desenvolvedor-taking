package org.taking.controller;

import java.util.Map;

import javax.naming.directory.InvalidAttributesException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.taking.dto.CursoSemestreDisciplinaDTO;
import org.taking.service.CursoSemestreDisciplinaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Path("/api/v1/cursosemestredisciplina")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoSemestreDisciplinaController {

  private final CursoSemestreDisciplinaService cursoSemestreDisciplinaService;

  /**
   * Método GET para buscar uma lista de CursoSemestreDisciplina
   * 
   * @return Response
   */
  @GET
  public Response getCursoSemestreDisciplinas() {
    try {
      return Response.ok(cursoSemestreDisciplinaService.getCursoSemestreDisciplinas()).build();
    } catch (Exception e) {
      // retorna como http 400, em caso de erro
      return Response.status(Status.BAD_REQUEST).build();
    }
  }

  /**
   * Método GET para buscar uma lista de CursoSemestreDisciplina
   * 
   * @param cursoId
   * @return Response
   */
  @GET
  @Path("/{cursoId}")
  public Response getCursoSemestreDisciplinasByCurso(@PathParam("cursoId") long cursoId) {
    try {
      return Response.ok(cursoSemestreDisciplinaService.findCursoSemestreDisciplinasByCurso(cursoId)).build();
    } catch (Exception e) {
      // retorna como http 400, em caso de erro
      return Response.status(Status.BAD_REQUEST).build();
    }
  }

  /**
   * Método POST para criar um novo CursoSemestreDisciplina, que recebe um JSON como um CursoSemestreDisciplinaDTO.
   * 
   * @param cursoSemestreDisciplinaDTO
   * @return Response
   */
  @POST
  public Response create(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) {
    try {
      cursoSemestreDisciplinaService.create(cursoSemestreDisciplinaDTO);

      return Response.noContent().build();
    } catch (Exception e) {
      if (e instanceof InvalidAttributesException) {
        // retorna como http 409, em caso de conflito
        return Response.status(Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
      }

      // retorna como http 400, em caso de qualquer outro erro
      return Response.status(Status.BAD_REQUEST).build();
    }
  }
  
  /**
   * Método DELETE para deletar um CursoSemestreDisciplina, que recebe um JSON como um CursoSemestreDisciplinaDTO.
   * 
   * @param cursoSemestreDisciplinaDTO
   * @return Response
   */
  @POST
  @Path("/delete")
  public Response delete(CursoSemestreDisciplinaDTO cursoSemestreDisciplinaDTO) {
    try {
      cursoSemestreDisciplinaService.delete(cursoSemestreDisciplinaDTO);
      return Response.noContent().build();
    } catch (Exception e) {
      // retorna como http 400, em caso de erro
      return Response.status(Status.BAD_REQUEST).build();
    }
  }
}
