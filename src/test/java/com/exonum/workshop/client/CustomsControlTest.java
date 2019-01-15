package com.exonum.workshop.client;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.exonum.binding.common.crypto.KeyPair;
import com.exonum.workshop.transaction.TxProtos.ChangeStatus;
import com.exonum.workshop.transaction.TxProtos.CreatePerson;
import com.exonum.workshop.view.PersonFullDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// Requires running ejb app with service
@Disabled
class CustomsControlTest {

  private static final KeyPair KEYS = CryptoClient.generateNewKeys();

  @Test
  void createPerson() {
    CreatePerson person = CreatePerson.newBuilder()
        .setName("Brad Pitt")
        .setPassport("US012345")
        .setAge(55)
        .setSex("Male")
        .build();

    boolean success = CustomsControlClient.createPerson(person, KEYS);

    assertTrue(success);

    PersonFullDto actualPerson = CustomsControlClient.getPerson(person.getPassport());

    assertEquals(person.getPassport(), actualPerson.getPassport());
    assertEquals(person.getName(), actualPerson.getName());
    assertEquals(person.getAge(), actualPerson.getAge());
    assertEquals(person.getSex(), actualPerson.getSex());

    System.out.println(actualPerson);
  }

  @Test
  void createPerson2() {
    CreatePerson person = CreatePerson.newBuilder()
        .setName("Tom Cruse")
        .setPassport("US5555")
        .setAge(60)
        .setSex("Male")
        .build();

    boolean success = CustomsControlClient.createPerson(person, KEYS);

    assertTrue(success);

    PersonFullDto actualPerson = CustomsControlClient.getPerson(person.getPassport());

    System.out.println(actualPerson);

    assertEquals(person.getPassport(), actualPerson.getPassport());
    assertEquals(person.getName(), actualPerson.getName());
    assertEquals(person.getAge(), actualPerson.getAge());
    assertEquals(person.getSex(), actualPerson.getSex());

  }

  @Test
  void changeStatus() {
    ChangeStatus status = ChangeStatus.newBuilder()
        .setPassport("US012345")
        .setNewStatus(1)
        .build();

    boolean success = CustomsControlClient.changeStatus(status, KEYS);
    assertTrue(success);
  }

  @Test
  void changeStatus2() {
    ChangeStatus status = ChangeStatus.newBuilder()
        .setPassport("US5555")
        .setNewStatus(-11)
        .build();

    boolean success = CustomsControlClient.changeStatus(status, KEYS);
    assertTrue(success);
  }

}
