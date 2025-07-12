package org.taking.controller;

import java.security.InvalidParameterException;
import java.util.Map;
import javax.naming.directory.InvalidAttributesException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import org.taking.model.Curso;
import org.taking.service.CursoService;

@AllArgsConstructor
@Path("/api/v1/curso")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoController {

    private final CursoService cursoService;

    /**
     * Método GET para buscar uma lista de Curso com filtro por cursoName opcional.
     * 
     * @param cursoName
     * @return Response
     */
    @GET
    public Response getCursos(
            @QueryParam("name") @DefaultValue("") String cursoName
    ) {
        try {
            return Response.ok(cursoService.getCursos(cursoName)).build();
        } catch (Exception e) {
            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método GET para buscar o Curso pelo id.
     * 
     * @param id
     * @return Response
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        try {
            final var cursoByIdOpt = cursoService.findCursoById(id);
            if (cursoByIdOpt.isEmpty()) {
                // retorna http 404, caso não encontre o Curso
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(cursoByIdOpt.get()).build();
        } catch (Exception e) {
            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método POST para criar um novo Curso.
     * 
     * @param curso
     * @return Response
     */
    @POST
    public Response create(Curso curso) {
        try {
            cursoService.create(curso);
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
     * Método PUT para atualizar um Curso.
     * 
     * @param cursoId
     * @param curso
     * @return Response
     */
    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long cursoId, Curso curso) {
        try {
            return Response.ok(cursoService.replace(cursoId, curso)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                // retorna com http 404, em caso não encontre o Curso a ser atualizado
                return Response.status(Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }

            // retorna como http 400, em caso de qualquer outro erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método DELETE para deletar um Curso.
     * 
     * @param cursoId
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long cursoId) {
        var isDeleted = cursoService.delete(cursoId);
        if (!isDeleted) {
            // retorna http 304, em caso de já ter sido deletado
            return Response.notModified().build();
        }

        return Response.noContent().build();
    }

}
