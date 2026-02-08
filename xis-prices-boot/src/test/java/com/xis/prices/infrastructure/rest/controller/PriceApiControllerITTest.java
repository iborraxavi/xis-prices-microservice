package com.xis.prices.infrastructure.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class PriceApiControllerITTest {

  private static final String API_PATH = "/api/v1/prices";
  private static final Long PRODUCT_ID = 35455L;
  private static final Integer BRAND_ID = 1;

  private static final String APPLICATION_DATE_1 = "2020-06-14T10:00:00Z";
  private static final String APPLICATION_DATE_2 = "2020-06-14T16:00:00Z";
  private static final String APPLICATION_DATE_3 = "2020-06-14T21:00:00Z";
  private static final String APPLICATION_DATE_4 = "2020-06-15T10:00:00Z";
  private static final String APPLICATION_DATE_5 = "2020-06-16T21:00:00Z";
  private static final String APPLICATION_DATE_NOT_FOUND = "2024-06-16T21:00:00Z";

  @Autowired private WebTestClient webClientTest;

  @Test
  @DisplayName(
      "Test 1: Given applicationDate, productId and brandId, when get price, then return expected price")
  void test1_whenGivenParameters_returnExpectedPrice() {
    webClientTest
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(API_PATH)
                    .queryParam("applicationDate", APPLICATION_DATE_1)
                    .queryParam("productId", PRODUCT_ID)
                    .queryParam("brandId", BRAND_ID)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(PRODUCT_ID)
        .jsonPath("$.brandId")
        .isEqualTo(BRAND_ID)
        .jsonPath("$.priceList")
        .isEqualTo(1)
        .jsonPath("$.startDate")
        .isEqualTo("2020-06-14T00:00:00Z")
        .jsonPath("$.endDate")
        .isEqualTo("2020-12-31T23:59:59Z")
        .jsonPath("$.price")
        .isEqualTo(35.50);
  }

  @Test
  @DisplayName(
      "Test 2: Given applicationDate, productId and brandId, when get price, then return expected price")
  void test2_whenGivenParameters_returnExpectedPrice() {
    webClientTest
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(API_PATH)
                    .queryParam("applicationDate", APPLICATION_DATE_2)
                    .queryParam("productId", PRODUCT_ID)
                    .queryParam("brandId", BRAND_ID)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(PRODUCT_ID)
        .jsonPath("$.brandId")
        .isEqualTo(BRAND_ID)
        .jsonPath("$.priceList")
        .isEqualTo(2)
        .jsonPath("$.startDate")
        .isEqualTo("2020-06-14T15:00:00Z")
        .jsonPath("$.endDate")
        .isEqualTo("2020-06-14T18:30:00Z")
        .jsonPath("$.price")
        .isEqualTo(25.45);
  }

  @Test
  @DisplayName(
      "Test 3: Given applicationDate, productId and brandId, when get price, then return expected price")
  void test3_whenGivenParameters_returnExpectedPrice() {
    webClientTest
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(API_PATH)
                    .queryParam("applicationDate", APPLICATION_DATE_3)
                    .queryParam("productId", PRODUCT_ID)
                    .queryParam("brandId", BRAND_ID)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(PRODUCT_ID)
        .jsonPath("$.brandId")
        .isEqualTo(BRAND_ID)
        .jsonPath("$.priceList")
        .isEqualTo(1)
        .jsonPath("$.startDate")
        .isEqualTo("2020-06-14T00:00:00Z")
        .jsonPath("$.endDate")
        .isEqualTo("2020-12-31T23:59:59Z")
        .jsonPath("$.price")
        .isEqualTo(35.50);
  }

  @Test
  @DisplayName(
      "Test 4: Given applicationDate, productId and brandId, when get price, then return expected price")
  void test4_whenGivenParameters_returnExpectedPrice() {
    webClientTest
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(API_PATH)
                    .queryParam("applicationDate", APPLICATION_DATE_4)
                    .queryParam("productId", PRODUCT_ID)
                    .queryParam("brandId", BRAND_ID)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(PRODUCT_ID)
        .jsonPath("$.brandId")
        .isEqualTo(BRAND_ID)
        .jsonPath("$.priceList")
        .isEqualTo(3)
        .jsonPath("$.startDate")
        .isEqualTo("2020-06-15T00:00:00Z")
        .jsonPath("$.endDate")
        .isEqualTo("2020-06-15T11:00:00Z")
        .jsonPath("$.price")
        .isEqualTo(30.50);
  }

  @Test
  @DisplayName(
      "Test 5: Given applicationDate, productId and brandId, when get price, then return expected price")
  void test5_whenGivenParameters_returnExpectedPrice() {
    webClientTest
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(API_PATH)
                    .queryParam("applicationDate", APPLICATION_DATE_5)
                    .queryParam("productId", PRODUCT_ID)
                    .queryParam("brandId", BRAND_ID)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(PRODUCT_ID)
        .jsonPath("$.brandId")
        .isEqualTo(BRAND_ID)
        .jsonPath("$.priceList")
        .isEqualTo(4)
        .jsonPath("$.startDate")
        .isEqualTo("2020-06-15T16:00:00Z")
        .jsonPath("$.endDate")
        .isEqualTo("2020-12-31T23:59:59Z")
        .jsonPath("$.price")
        .isEqualTo(38.95);
  }

  @Test
  @DisplayName(
      "Given applicationDate, productId and brandId, when price not found, then return expected error")
  void findPrice_whenPriceNotFound_returnExpectedError() {
    webClientTest
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(API_PATH)
                    .queryParam("applicationDate", APPLICATION_DATE_NOT_FOUND)
                    .queryParam("productId", PRODUCT_ID)
                    .queryParam("brandId", BRAND_ID)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound();
  }
}
