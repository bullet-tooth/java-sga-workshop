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

import static com.google.common.base.Preconditions.checkNotNull;

import com.exonum.binding.common.hash.HashCode;
import com.exonum.binding.common.serialization.StandardSerializers;
import com.exonum.binding.service.Schema;
import com.exonum.binding.storage.database.View;
import com.exonum.binding.storage.indices.ListIndex;
import com.exonum.binding.storage.indices.ListIndexProxy;
import com.exonum.binding.storage.indices.MapIndex;
import com.exonum.binding.storage.indices.MapIndexProxy;
import com.exonum.workshop.model.ModelProtos.Person;
import com.exonum.workshop.model.ModelProtos.Status;
import java.util.Collections;
import java.util.List;

/**
 * {@code MySchema} provides access to the tables of {@link $.MyService},
 * given a database state: a {@link View}.
 *
 * @see <a href="https://exonum.com/doc/architecture/storage/#table-types">Exonum table types.</a>
 */
public final class CustomsControlSchema implements Schema {

  private final View view;

  public CustomsControlSchema(View view) {
    this.view = checkNotNull(view);
  }

  @Override
  public List<HashCode> getStateHashes() {
    // You shall usually return a list of the state hashes
    // of all Merklized collections of this service,
    // see https://exonum.com/doc/architecture/storage/#merkelized-indices
    return Collections.emptyList();
  }

  public ListIndex<Person> persons() {
    return ListIndexProxy.newInstance("persons", view, Person.class);
  }

  public MapIndex<String, Status> statuses() {
    return MapIndexProxy.newInstance("statuses",
        view,
        StandardSerializers.string(),
        StandardSerializers.protobuf(Status.class));
  }

}
