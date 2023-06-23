package sv.gerry.RESTJpa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sv.gerry.RESTJpa.entities.Laptop;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LaptopControllerTest {

    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private int port = 8080;

    @Test

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void testCreate() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String json = """

                        {
                  "brand": "dell test",
                  "model": "Pavilion",
                  "memory": 16,
                  "hardDrive": 256,
                  "price": 999.99,
                  "inStock": true
                }



                                """;

        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<Laptop> response = testRestTemplate.postForEntity("/api/laptops", requestEntity, Laptop.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDelete() {
        ResponseEntity<Laptop> response = testRestTemplate.exchange("/api/laptops/1", HttpMethod.DELETE, null,
                Laptop.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    void testDeleteAll() {

        ResponseEntity<Laptop[]> response = testRestTemplate.exchange("/api/laptops", HttpMethod.DELETE, null,
                Laptop[].class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testFindAll() {

        ResponseEntity<Laptop[]> response = testRestTemplate.getForEntity("/api/laptops", Laptop[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Laptop> laptops = Arrays.asList(response.getBody());
        System.out.println(laptops.size());

    }

    @Test
    void testFindOneById() {

        System.out.println(port + "***********************************");

        ResponseEntity<Laptop> response = testRestTemplate.getForEntity("/api/laptops/1", Laptop.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    void testUpdate() {
        ResponseEntity<Laptop> response = testRestTemplate.getForEntity("/api/laptops/1", Laptop.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Laptop existingLaptop = response.getBody();
        assertNotNull(existingLaptop);

        existingLaptop.setBrand("Lenovo");
        existingLaptop.setMemory(32);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Laptop> requestEntity = new HttpEntity<Laptop>(existingLaptop, headers);

        ResponseEntity<Laptop> putResponse = testRestTemplate.exchange("/api/laptops/1", HttpMethod.PUT, requestEntity,
                Laptop.class);
        assertEquals(HttpStatus.OK, putResponse.getStatusCode());

        ResponseEntity<Laptop> updatedLaptopResponse = testRestTemplate.getForEntity("/api/laptops/1", Laptop.class);
        assertEquals(HttpStatus.OK, updatedLaptopResponse.getStatusCode());
        Laptop updatedLaptop = updatedLaptopResponse.getBody();
        assertNotNull(updatedLaptop);

    }
}
