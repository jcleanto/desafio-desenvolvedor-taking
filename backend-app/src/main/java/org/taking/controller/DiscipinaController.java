package org.taking.controller;

import java.security.InvalidParameterException;
import java.util.Map;
import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;

import org.taking.model.Disciplina;
import org.taking.service.DisciplinaService;

@AllArgsConstructor
@Path("/api/v1/disciplina")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DiscipinaController {

    private final DisciplinaService disciplinaService;

    /**
     * Método GET para buscar uma lista de Disciplina com filtro por disciplinaName opcional.
     * 
     * @param disciplinaName
     * @return Response
     */
    @GET
    public Response getDisciplinas(
            @QueryParam("name") @DefaultValue("") String disciplinaName
    ) {
        try {
            return Response.ok(disciplinaService.getDisciplinas(disciplinaName)).build();
        } catch (Exception e) {
            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método GET para buscar a Disciplina pelo id.
     * 
     * @param id
     * @return Response
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        try {
            final var disciplinaByIdOpt = disciplinaService.findDisciplinaById(id);
            if (disciplinaByIdOpt.isEmpty()) {
                // retorna http 404, caso não encontre a Disciplina
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(disciplinaByIdOpt.get()).build();
        } catch (Exception e) {
            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método POST para criar uma nova Disciplina.
     * 
     * @param disciplina
     * @return Response
     */
    @POST
    public Response create(Disciplina disciplina) {
        try {
            disciplinaService.create(disciplina);
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
     * Método PUT para atualizar uma Disciplina.
     * 
     * @param disciplinaId
     * @param disciplina
     * @return Response
     */
    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long disciplinaId, Disciplina disciplina) {
        try {
            return Response.ok(disciplinaService.replace(disciplinaId, disciplina)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                // retorna com http 404, em caso não encontre a Disciplina a ser atualizada
                return Response.status(Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }

            // retorna como http 400, em caso de qualquer outro erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método DELETE para deletar uma Disciplina.
     * 
     * @param disciplinaId
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long disciplinaId) {
        var isDeleted = disciplinaService.delete(disciplinaId);
        if (!isDeleted) {
            // retorna http 304, em caso de já ter sido deletada
            return Response.notModified().build();
        }
        return Response.noContent().build();
    }
  
}
