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

import org.taking.model.Semestre;
import org.taking.service.SemestreService;

@AllArgsConstructor
@Path("/api/v1/semestre")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SemestreController {

    private final SemestreService semestreService;

    /**
     * Método GET para buscar uma lista de Semestre com filtro por semestreName opcional.
     * 
     * @param semestreName
     * @return Response
     */
    @GET
    public Response getSemestres(
            @QueryParam("name") @DefaultValue("") String semestreName
    ) {
        try {
            return Response.ok(semestreService.getSemestres(semestreName)).build();
        } catch (Exception e) {
            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método GET para buscar o Semestre pelo id.
     * 
     * @param id
     * @return Response
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        try {
            final var semestreByIdOpt = semestreService.findSemestreById(id);
            if (semestreByIdOpt.isEmpty()) {
                // retorna http 404, caso não encontre o Semestre
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(semestreByIdOpt.get()).build();
        } catch (Exception e) {
            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método POST para criar um novo Semestre.
     * 
     * @param semestre
     * @return Response
     */
    @POST
    public Response create(Semestre semestre) {
        try {
            semestreService.create(semestre);
            return Response.noContent().build();
        } catch (Exception e) {
            if (e instanceof InvalidAttributesException) {
                // retorna como http 409, em caso de conflito
                return Response.status(Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
            }

            // retorna como http 400, em caso de erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método PUT para atualizar um Semestre.
     * 
     * @param semestreId
     * @param semestre
     * @return Response
     */
    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long semestreId, Semestre semestre) {
        try {
            return Response.ok(semestreService.replace(semestreId, semestre)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                // retorna com http 404, em caso não encontre o Semestre a ser atualizado
                return Response.status(Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }

            // retorna como http 400, em caso de qualquer outro erro
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    /**
     * Método DELETE para deletar um Semestre.
     * 
     * @param semestreId
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long semestreId) {
        var isDeleted = semestreService.delete(semestreId);
        if (!isDeleted) {
            // retorna http 304, em caso de já ter sido deletado
            return Response.notModified().build();
        }
        return Response.noContent().build();
    }
  
}
