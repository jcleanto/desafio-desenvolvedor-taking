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

    @GET
    public Response getCursos(
            @QueryParam("name") @DefaultValue("") String semestreName
    ) {
        try {
            return Response.ok(semestreService.getSemestres(semestreName)).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        try {
            final var semestreByIdOpt = semestreService.findSemestreById(id);
            if (semestreByIdOpt.isEmpty()) {
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(semestreByIdOpt.get()).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @POST
    public Response create(Semestre semestre) {
        try {
            semestreService.create(semestre);
            return Response.noContent().build();
        } catch (Exception e) {
            if (e instanceof InvalidAttributesException) {
                return Response.status(Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
            }

            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response replace(@PathParam("id") long semestreId, Semestre semestre) {
        try {
            return Response.ok(semestreService.replace(semestreId, semestre)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                return Response.status(Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }

            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response update(@PathParam("id") long semestreId) {
        var isDeleted = semestreService.delete(semestreId);
        if (!isDeleted) {
            return Response.notModified().build();
        }
        return Response.noContent().build();
    }
  
}
