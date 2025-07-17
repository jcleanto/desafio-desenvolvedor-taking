package org.taking.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

import javax.naming.directory.InvalidAttributesException;
import java.security.InvalidParameterException;
import java.util.Map;

@AllArgsConstructor
@Path("/api/v1/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

  @Inject
  Keycloak keycloak;

  /**
   * Método GET para buscar uma lista de Users do Keycloak.
   *
   * @return Response
   */
  @GET
  @RolesAllowed({"admin"})
  public Response getUsers(
          @QueryParam("name") @DefaultValue("") String name,
          @QueryParam("first") @DefaultValue("0") Integer first,
          @QueryParam("pageSize") @DefaultValue("10") Integer pageSize
  ) {
    try {
      /* TODO: Implementar paginação no frontend para montar o Response de acordo
      List<UserRepresentation> items = keycloak.realm("desafio-desenvolvedor-taking").users().list(first, pageSize);
      Integer totalCount = keycloak.realm("desafio-desenvolvedor-taking").users().count();
      UserResponseDTO userResponseDTO = UserResponseDTO.builder().items(items).totalCount(totalCount).build();
      return Response.ok(userResponseDTO).build();
       */
      return Response.ok(keycloak.realm("desafio-desenvolvedor-taking").users().list()).build();
    } catch (Exception e) {
      // retorna como http 400, em caso de erro
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  /**
   * Método POST para criar um novo Usuário.
   *
   * @param user
   * @return Response
   */
  @POST
  @RolesAllowed("admin")
  public Response create(UserRepresentation user) {
    try {
      keycloak.realm("desafio-desenvolvedor-taking").users().create(user);
      return Response.noContent().build();
    } catch (Exception e) {
      if (e instanceof InvalidAttributesException) {
        // retorna como http 409, em caso de conflito
        return Response.status(Response.Status.CONFLICT).entity(Map.of("message", e.getMessage())).build();
      }

      // retorna como http 400, em caso de qualquer outro erro
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  /**
   * Método PUT para atualizar um Usuário.
   *
   * @param userId
   * @param user
   * @return Response
   */
  @PUT
  @RolesAllowed("admin")
  @Path("/{id}")
  public Response replace(@PathParam("id") String userId, UserRepresentation user) {
    try {
      keycloak.realm("desafio-desenvolvedor-taking").users().get(userId).update(user);
      return Response.ok().build();
    } catch (Exception e) {
      if (e instanceof InvalidParameterException) {
        // retorna com http 404, em caso não encontre o Usuário a ser atualizado
        return Response.status(Response.Status.NOT_FOUND).entity(Map.of("message", e.getMessage())).build();
      }

      // retorna como http 400, em caso de qualquer outro erro
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
  }

  /**
   * Método DELETE para deletar um Usuário.
   *
   * @param userId
   * @return Response
   */
  @DELETE
  @RolesAllowed("admin")
  @Path("/{id}")
  public Response delete(@PathParam("id") String userId) {
    try (var responseDeleted = keycloak.realm("desafio-desenvolvedor-taking").users().delete(userId)) {
      if (responseDeleted.getStatus() == 304) {
        // retorna http 304, em caso de já ter sido deletado
        return Response.notModified().build();
      }
    }

    return Response.noContent().build();
  }
}
