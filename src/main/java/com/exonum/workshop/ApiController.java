package com.exonum.workshop;

import static com.exonum.binding.common.serialization.json.JsonSerializer.json;
import static com.google.common.base.Preconditions.checkArgument;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.util.stream.Collectors.toList;

import com.exonum.workshop.model.CustomsControlStatus;
import com.exonum.workshop.model.ModelProtos.Person;
import com.exonum.workshop.model.ModelProtos.Status;
import com.exonum.workshop.view.PersonFullDto;
import com.exonum.workshop.view.PersonShortDto;
import com.exonum.workshop.view.PersonStatusDto;
import com.google.common.collect.ImmutableMap;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides an access to the next endpoints:
 *<pre>
 *   GET /persons -> returns list of person IDs
 *   GET /persons/AA123 -> returns full info for the person with given ID
 *   GET /persons/AA123/status -> returns customs control status for the person with given ID
 *</pre>
 */
public class ApiController {

  private static final String ID_PARAM = "id";
  private static final String GET_PERSONS_PATH = "/persons";
  private static final String GET_PERSON_PATH = "/persons/:" + ID_PARAM;
  private static final String GET_STATUS_PATH = "/persons/:" + ID_PARAM + "/status";

  private final CustomsControlService service;

  private final Map<String, Handler<RoutingContext>> handlers =
      ImmutableMap.<String, Handler<RoutingContext>>builder()
          .put(GET_PERSONS_PATH, this::getAllPersons)
          .put(GET_PERSON_PATH, this::getPersonById)
          .put(GET_STATUS_PATH, this::getPersonStatus)
          .build();

  public ApiController(CustomsControlService service) {
    this.service = service;
  }

  void mountApi(Router router) {
    router.route()
        .handler(BodyHandler.create());

    handlers.forEach((path, handler) ->
        router.route(path).handler(handler)
    );
  }

  private void getAllPersons(RoutingContext rc) {
    List<PersonShortDto> list = service.getPersons().stream()
        .map(PersonShortDto::toDto)
        .collect(toList());

    rc.response()
        .putHeader("Content-Type", "application/json")
        .end(json().toJson(list));
  }

  private void getPersonById(RoutingContext rc) {
    String id = getRequiredParameter(rc.request(), ID_PARAM);

    Optional<Person> person = service.getPersonById(id);
    if (person.isPresent()) {
      String personJson = person
          .map(PersonFullDto::toDto)
          .map(json()::toJson)
          .get();

      rc.response()
          .putHeader("Content-Type", "application/json")
          .end(personJson);
    } else {
      rc.response()
          .setStatusCode(HTTP_NOT_FOUND)
          .end();
    }
  }

  private void getPersonStatus(RoutingContext rc) {
    String id = getRequiredParameter(rc.request(), ID_PARAM);
    Optional<Status> status = service.getStatus(id);

    if (status.isPresent()) {
      String statusJson = status
          .map(Status::getStatus)
          .map(CustomsControlStatus::of)
          .map(CustomsControlStatus::name)
          .map(s -> new PersonStatusDto(id, s))
          .map(json()::toJson)
          .get();

      rc.response()
          .putHeader("Content-Type", "application/json")
          .end(statusJson);
    } else {
      rc.response()
          .setStatusCode(HTTP_NOT_FOUND)
          .end();
    }
  }

  private static String getRequiredParameter(HttpServerRequest request, String key) {
    MultiMap parameters = request.params();
    checkArgument(parameters.contains(key), "No required key (%s) in request parameters: %s",
        key, parameters);
    return parameters.get(key);
  }


}
