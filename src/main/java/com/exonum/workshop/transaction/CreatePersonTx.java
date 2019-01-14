package com.exonum.workshop.transaction;

import static com.exonum.binding.common.serialization.json.JsonSerializer.json;
import static com.exonum.workshop.Utils.streamOf;
import static com.exonum.workshop.model.CustomsControlStatus.ALLOWED;
import static com.exonum.workshop.transaction.TxError.DUPLICATE_PERSON;

import com.exonum.binding.storage.indices.ListIndex;
import com.exonum.binding.storage.indices.MapIndex;
import com.exonum.binding.transaction.Transaction;
import com.exonum.binding.transaction.TransactionContext;
import com.exonum.binding.transaction.TransactionExecutionException;
import com.exonum.workshop.CustomsControlSchema;
import com.exonum.workshop.model.ModelProtos.Person;
import com.exonum.workshop.model.ModelProtos.Status;
import java.util.Optional;

public final class CreatePersonTx implements Transaction {

  private static final short ID = 0;

  private final String passport;
  private final String name;
  private final int age;
  private final String sex;

  public CreatePersonTx(String passport, String name, int age, String sex) {
    this.passport = passport;
    this.name = name;
    this.age = age;
    this.sex = sex;
  }

  @Override
  public void execute(TransactionContext context) throws TransactionExecutionException {
    CustomsControlSchema schema = new CustomsControlSchema(context.getFork());
    ListIndex<Person> persons = schema.persons();

    Optional<String> duplicate = streamOf(persons)
        .map(Person::getPassport)
        .filter(id -> id.equals(passport))
        .findFirst();

    if (duplicate.isPresent()) {
      throw new TransactionExecutionException(DUPLICATE_PERSON.errorCode,
          String.format("Person with such id '%s' already registered", duplicate.get()));
    }

    Person person = Person.newBuilder()
        .setPassport(passport)
        .setName(name)
        .setAge(age)
        .setSex(sex)
        .build();
    persons.add(person);

    //add status
    Status status = Status
        .newBuilder()
        .setStatus(ALLOWED.id())
        .build();
    MapIndex<String, Status> statuses = schema.statuses();
    statuses.put(passport, status);
  }

  @Override
  public String info() {
    return json().toJson(this);
  }

}
