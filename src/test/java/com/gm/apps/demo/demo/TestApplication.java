package com.gm.apps.demo.demo;

import org.apache.commons.codec.binary.Base64;

public class TestApplication {
  public static void main(String[] args) {
    String token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6Im1haGFqYW41QGF2YXlhLmNvbSIsInVybjpvYXV0aDpzY29wZSI6IiIsImlzcyI6Imh0dHA6Ly9rbm93bWFpbC5hdXRoZW50aWNhdGlvbi5tZSIsImF1ZCI6IjA5NDFkYTVkODRhZTRhMjg5MDVmYjEyZjgxMzY3NjI4IiwiZXhwIjoxNTYwMDgyNjQ5LCJuYmYiOjE1NTc0OTA2NDl9.IlUtVr2yG6dE4DOnktYZA6ExVHKo4-r5dLwzQslAZ0Q";

    String[] split_string = token.split("\\.");
    String base64EncodedHeader = split_string[0];
    String base64EncodedBody = split_string[1];
    String base64EncodedSignature = split_string[2];

    System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
    Base64 base64Url = new Base64(true);
    String header = new String(base64Url.decode(base64EncodedHeader));
    System.out.println("JWT Header : " + header);


    System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
    String body = new String(base64Url.decode(base64EncodedBody));
    System.out.println("JWT Body : " + body);

  }
}
