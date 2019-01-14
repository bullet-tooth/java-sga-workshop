package com.exonum.workshop.view;

import com.exonum.workshop.model.ModelProtos.Person;

public class PersonShortDto {

  private final String id;

  private PersonShortDto(String id) {
    this.id = id;
  }

  public static PersonShortDto toDto(Person person) {
    return new PersonShortDto(person.getPassport());
  }

}
