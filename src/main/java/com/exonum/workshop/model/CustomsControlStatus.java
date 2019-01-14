package com.exonum.workshop.model;

public enum CustomsControlStatus {
  ALLOWED(0),
  FORBIDDEN(1);

  private final int id;

  CustomsControlStatus(int id) {
    this.id = id;
  }

  public int id() {
    return id;
  }

  public static boolean statusExists(int status) {
    for (CustomsControlStatus s : CustomsControlStatus.values()) {
      if (s.id == status) {
        return true;
      }
    }
    return false;
  }

  public static CustomsControlStatus of(int status) {
    for (CustomsControlStatus s : CustomsControlStatus.values()) {
      if (s.id == status) {
        return s;
      }
    }
    throw new RuntimeException("Unknown status");
  }

}
