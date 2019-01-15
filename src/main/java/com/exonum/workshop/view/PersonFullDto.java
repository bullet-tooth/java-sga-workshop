package com.exonum.workshop.view;

import com.exonum.workshop.model.ModelProtos.Person;
import com.google.common.base.MoreObjects;

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


  public String getPassport() {
    return passport;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getSex() {
    return sex;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("passport", passport)
        .add("name", name)
        .add("age", age)
        .add("sex", sex)
        .toString();
  }
}
