package com.royalmail.barcode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BarcodeApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void validateBarcodeWithValidFormatReturnsTrue() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/AB473124829GB", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isValid()).isEqualTo(true);
    }

    @Test
    void validateBarcodeWithValidPaddedSerialNumberReturnsTrue() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/AB000000014GB", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isValid()).isEqualTo(true);
    }

    @Test
    void validateBarcodeWithInvalidCheckDigitReturnsFalse() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/AB473124828GB", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isValid()).isEqualTo(false);
    }

    @Test
    void validateBarcodeWithInvalidPrefixReturnsFalse() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/a473124829GB", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isValid()).isEqualTo(false);
    }

    @Test
    void validateBarcodeWithInvalidSerialNumberReturnsFalse() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/AB124829GB", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isValid()).isEqualTo(false);
    }

    @Test
    void validateBarcodeWithInvalidCountryCodeReturnsFalse() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/AB473124829EN", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isValid()).isEqualTo(false);
    }

    @Test
    void validateEmptyBarcodeReturnsNotFound() {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/validate/", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
