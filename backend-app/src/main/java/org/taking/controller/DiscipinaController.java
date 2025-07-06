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

    @GET
    public Response getDisciplinas(
            @QueryParam("name") @DefaultValue("") String disciplinaName
    ) {
        try {
            return Response.ok(disciplinaService.getDisciplinas(disciplinaName)).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        try {
            final var disciplinaByIdOpt = disciplinaService.findDisciplinaById(id);
            if (disciplinaByIdOpt.isEmpty()) {
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(disciplinaByIdOpt.get()).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @POST
    public Response create(Disciplina disciplina) {
        try {
            disciplinaService.create(disciplina);
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
    public Response replace(@PathParam("id") long disciplinaId, Disciplina curso) {
        try {
            return Response.ok(disciplinaService.replace(disciplinaId, curso)).build();
        } catch (Exception e) {
            if (e instanceof InvalidParameterException) {
                return Response.status(Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
            }

            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response update(@PathParam("id") long disciplinaId) {
        var isDeleted = disciplinaService.delete(disciplinaId);
        if (!isDeleted) {
            return Response.notModified().build();
        }
        return Response.noContent().build();
    }
  
}
