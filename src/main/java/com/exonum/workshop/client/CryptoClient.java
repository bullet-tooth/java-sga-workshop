package com.exonum.workshop.client;

import com.exonum.binding.common.crypto.CryptoFunctions;
import com.exonum.binding.common.crypto.KeyPair;
import com.exonum.binding.common.message.TransactionMessage;
import com.exonum.workshop.CustomsControlService;
import com.exonum.workshop.transaction.ChangeStatusTx;
import com.exonum.workshop.transaction.CreatePersonTx;
import com.exonum.workshop.transaction.TxProtos.ChangeStatus;
import com.exonum.workshop.transaction.TxProtos.CreatePerson;
import com.google.common.io.BaseEncoding;

public class CryptoClient {

  private static final BaseEncoding HEX_ENCODER = BaseEncoding.base16().lowerCase();

  public static KeyPair generateNewKeys() {
    return CryptoFunctions.ed25519().generateKeyPair();
  }

  public static TransactionMessage createMessage(CreatePerson tx, KeyPair authorKeys) {
    return TransactionMessage.builder()
        .serviceId(CustomsControlService.ID)
        .transactionId(CreatePersonTx.ID)
        .payload(tx.toByteArray())
        .sign(authorKeys, CryptoFunctions.ed25519());
  }

  public static TransactionMessage createMessage(ChangeStatus tx, KeyPair authorKeys) {
    return TransactionMessage.builder()
        .serviceId(CustomsControlService.ID)
        .transactionId(ChangeStatusTx.ID)
        .payload(tx.toByteArray())
        .sign(authorKeys, CryptoFunctions.ed25519());
  }

  public static String toHex(byte[] bytes) {
    return HEX_ENCODER.encode(bytes);
  }

}
