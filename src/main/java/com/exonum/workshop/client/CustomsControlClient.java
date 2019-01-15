package com.exonum.workshop.client;

import static com.exonum.binding.common.serialization.json.JsonSerializer.json;
import static com.exonum.workshop.client.CryptoClient.createMessage;
import static com.exonum.workshop.client.CryptoClient.toHex;

import com.exonum.binding.common.crypto.KeyPair;
import com.exonum.binding.common.message.TransactionMessage;
import com.exonum.workshop.transaction.TxProtos.ChangeStatus;
import com.exonum.workshop.transaction.TxProtos.CreatePerson;
import com.exonum.workshop.view.PersonFullDto;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomsControlClient {

  private static final OkHttpClient HTTP = new OkHttpClient();
  private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

  private static final String EJB_HOST = "http://127.0.0.1:6000";
  private static final String CORE_HOST = "http://127.0.0.1:3000";
  private static final String SERVICE_PATH = EJB_HOST + "/api/my-service";
  private static final String SUBMIT_TX_PATH = CORE_HOST + "/api/explorer/v1/transactions";

  public static boolean createPerson(CreatePerson tx, KeyPair keys) {
    return send(createMessage(tx, keys));
  }

  public static boolean changeStatus(ChangeStatus tx, KeyPair keys) {
    return send(createMessage(tx, keys));
  }

  private static boolean send(TransactionMessage message) {
    String hex = toHex(message.toBytes());
    try (Response response = submitTransaction(hex)) {
      return response.isSuccessful();
    }
  }

  private static Response submitTransaction(String body) {
    try {
      String json = json().toJson(new TxMsg(body));
      Request request = new Builder()
          .url(SUBMIT_TX_PATH)
          .post(RequestBody.create(MEDIA_TYPE, json))
          .build();

      return HTTP.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static PersonFullDto getPerson(String id) {
    try {
      Response response = get("/persons/" + id);
      if (response.isSuccessful()) {
        String jsonResponse = response.body().string();
        return json().fromJson(jsonResponse, PersonFullDto.class);
      } else {
        return null;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Response get(String path) throws IOException {
    Request request = new Builder()
        .url(SERVICE_PATH + path)
        .get()
        .build();
    return HTTP.newCall(request).execute();
  }

}
