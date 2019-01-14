package com.exonum.workshop.transaction;

enum TxError {
  DUPLICATE_PERSON(1);

  byte errorCode;

  TxError(int code) {
    this.errorCode = (byte) code;
  }

}
