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

package com.exonum.workshop.transaction;

import static com.exonum.binding.common.serialization.StandardSerializers.protobuf;
import static com.google.common.base.Preconditions.checkArgument;

import com.exonum.binding.service.TransactionConverter;
import com.exonum.binding.transaction.RawTransaction;
import com.exonum.binding.transaction.Transaction;
import com.exonum.workshop.CustomsControlService;
import com.exonum.workshop.transaction.TxProtos.ChangeStatus;
import com.exonum.workshop.transaction.TxProtos.CreatePerson;

/**
 * {@code MyTransactionConverter} converts transaction messages of
 * {@link com.exonum.workshop.CustomsControlService}
 * into {@linkplain Transaction executable transactions} of this service.
 */
public final class CustomControlTransactionConverter implements TransactionConverter {

  @Override
  public Transaction toTransaction(RawTransaction rawTransaction) {
    checkArgument(CustomsControlService.ID == rawTransaction.getServiceId());

    short id = rawTransaction.getTransactionId();
    byte[] bytes = rawTransaction.getPayload();

    switch (id) {
      case CreatePersonTx.ID:
        return createPerson(bytes);
      case ChangeStatusTx.ID:
        return changeStatus(bytes);
      default:
        throw new RuntimeException("Unknown transaction");
    }
  }

  private static Transaction createPerson(byte[] bytes) {
    CreatePerson tx = protobuf(CreatePerson.class).fromBytes(bytes);
    return new CreatePersonTx(tx.getPassport(), tx.getName(), tx.getAge(), tx.getSex());
  }

  private static Transaction changeStatus(byte[] bytes) {
    ChangeStatus tx = protobuf(ChangeStatus.class).fromBytes(bytes);
    return new ChangeStatusTx(tx.getPassport(), tx.getNewStatus());
  }

}
