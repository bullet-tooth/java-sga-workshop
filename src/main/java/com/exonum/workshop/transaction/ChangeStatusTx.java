package com.exonum.workshop.transaction;

import static com.exonum.binding.common.serialization.json.JsonSerializer.json;
import static com.exonum.workshop.model.CustomsControlStatus.statusExists;
import static com.google.common.base.Preconditions.checkArgument;

import com.exonum.binding.storage.indices.MapIndex;
import com.exonum.binding.transaction.Transaction;
import com.exonum.binding.transaction.TransactionContext;
import com.exonum.workshop.CustomsControlSchema;
import com.exonum.workshop.model.ModelProtos.Status;

public class ChangeStatusTx implements Transaction {

  public static final short ID = 1;

  private final String passport;
  private final int newStatus;

  public ChangeStatusTx(String passport, int newStatus) {
    checkArgument(statusExists(newStatus));
    this.newStatus = newStatus;
    this.passport = passport;
  }

  @Override
  public void execute(TransactionContext context) {
    CustomsControlSchema schema = new CustomsControlSchema(context.getFork());
    MapIndex<String, Status> statuses = schema.statuses();

    Status status = Status.newBuilder()
        .setStatus(newStatus)
        .build();

    statuses.put(passport, status);
  }

  @Override
  public String info() {
    return json().toJson(this);
  }

}
