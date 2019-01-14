package com.exonum.workshop.view;

import com.exonum.workshop.model.ModelProtos.Person;

public class PersonFullDto {

  private final String passport;
  private final String name;
  private final int age;
  private final String sex;

  public PersonFullDto(String passport, String name, int age, String sex) {
    this.passport = passport;
    this.name = name;
    this.age = age;
    this.sex = sex;
  }

  public static PersonFullDto toDto(Person person) {
    return new PersonFullDto(person.getPassport(), person.getName(), person.getAge(),
        person.getSex());
  }

}
