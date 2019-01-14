/*
 * Copyright 2018 The Exonum Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exonum.workshop;

import static com.exonum.workshop.Utils.streamOf;
import static com.google.common.base.Preconditions.checkState;
import static java.util.stream.Collectors.toList;

import com.exonum.binding.service.AbstractService;
import com.exonum.binding.service.Node;
import com.exonum.binding.service.Schema;
import com.exonum.binding.service.TransactionConverter;
import com.exonum.binding.storage.database.Fork;
import com.exonum.binding.storage.database.View;
import com.exonum.binding.storage.indices.ListIndex;
import com.exonum.binding.storage.indices.MapIndex;
import com.exonum.workshop.model.ModelProtos.Person;
import com.exonum.workshop.model.ModelProtos.Status;
import com.google.inject.Inject;
import io.vertx.ext.web.Router;
import java.util.List;
import java.util.Optional;

public final class CustomsControlService extends AbstractService {

  public static final short ID = 42;
  static final String NAME = "my-service";
  static final String INITIAL_SERVICE_CONFIGURATION = "{ \"version\": 0.1 }";
  private Node node;

  @Inject
  public CustomsControlService(TransactionConverter transactionConverter) {
    super(ID, NAME, transactionConverter);
  }

  @Override
  protected Schema createDataSchema(View view) {
    return new CustomsControlSchema(view);
  }

  @Override
  public Optional<String> initialize(Fork fork) {
    return Optional.of(INITIAL_SERVICE_CONFIGURATION);
  }

  @Override
  public void createPublicApiHandlers(Node node, Router router) {
    this.node = node;
    new ApiController(this).mountApi(router);
  }

  public List<Person> getPersons() {
    checkBlockchainInitialized();

    return node.withSnapshot(view -> {
      CustomsControlSchema schema = new CustomsControlSchema(view);
      ListIndex<Person> people = schema.persons();

      return streamOf(people)
          .collect(toList());
    });
  }

  public Optional<Person> getPersonById(String id) {
    checkBlockchainInitialized();

    return node.withSnapshot(view -> {
      CustomsControlSchema schema = new CustomsControlSchema(view);
      ListIndex<Person> people = schema.persons();

      return streamOf(people)
          .filter(person -> person.getPassport().equals(id))
          .findFirst();
    });
  }

  public Optional<Status> getStatus(String id) {
    checkBlockchainInitialized();

    return node.withSnapshot(view -> {
      CustomsControlSchema schema = new CustomsControlSchema(view);
      MapIndex<String, Status> statuses = schema.statuses();

      return Optional.ofNullable(statuses.get(id));
    });
  }

  private void checkBlockchainInitialized() {
    checkState(node != null, "Service has not been fully initialized yet");
  }


}
