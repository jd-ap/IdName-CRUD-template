package crud.tech.proof.controller;

import crud.tech.proof.Application;
import crud.tech.proof.model.IdNameDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = Application.class)
class IdsNamesControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest(name = "{index}: GET ''{0}'', expected next page ''{1}'' ")
    @Order(1)
    @DisplayName("Test 1: do find all resources would returning one element")
    @CsvSource({"/ids-names?pageNumber=1&pageSize=2&sort=name:a,true"
            , "/ids-names?q=first&pageNumber=1&pageSize=2&sort=name:d,false"})
    void givenFindAll_whenTest1_thenReturns200(String url, String expectedHasNext) {
        //given
        var aId = UUID.fromString("e5558ff1-80b9-4c5f-b63f-3f9e6f540001");
        var expected = new IdNameDto().id(aId).name("its a first name").active(Boolean.TRUE);

        //when
        var result = restTemplate.getForEntity(url, IdNameDto[].class);

        //then
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expected, result.getBody()[0]);

        var headers = result.getHeaders();
        assertAll("Verify headers",
                () -> assertEquals("1", headers.getFirst("x-pagination-current")),
                () -> assertEquals(expectedHasNext, headers.getFirst("x-pagination-hasNext")),
                () -> assertEquals("false", headers.getFirst("x-pagination-hasPrevious"))
        );
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: do find one by your id would returning one element")
    void givenFindById_whenTest2_thenReturns200() {
        //given
        String url = "/ids-names/%s";

        var aId = UUID.fromString("e5558ff1-80b9-4c5f-b63f-3f9e6f540001");
        var expected = new IdNameDto().id(aId).name("its a first name").active(Boolean.TRUE);

        //when
        var result = restTemplate.getForEntity(url.formatted(aId), IdNameDto.class);

        //then
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expected, result.getBody());
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: do find a resource by your name")
    void givenFindByName_whenTest3_thenReturns200() {
        //given
        String url = "/ids-names/name/%s?pageNumber=%d&pageSize=%d&sort=%s";

        String name = "first";
        Integer pageNumber = 1;
        Integer pageSize = 1;
        String sort = "name:d";

        var aId = UUID.fromString("e5558ff1-80b9-4c5f-b63f-3f9e6f540001");
        var expected = new IdNameDto().id(aId).name("its a first name").active(Boolean.TRUE);

        //when
        var result = restTemplate.getForEntity(url.formatted(name, pageNumber, pageSize, sort), IdNameDto[].class);

        //then
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expected, result.getBody()[0]);

        var headers = result.getHeaders();
        assertAll("Verify headers",
                () -> assertEquals("1", headers.getFirst("x-pagination-current")),
                () -> assertEquals("false", headers.getFirst("x-pagination-hasNext")),
                () -> assertEquals("false", headers.getFirst("x-pagination-hasPrevious"))
        );
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: do remove resource would disabled in database")
    void givenDeleteById_whenTest4_thenReturns204() {
        //given
        String url = "/ids-names/%s";

        var aId = UUID.fromString("e5558ff1-80b9-4c5f-b63f-3f9e6f540002");

        var expected = new IdNameDto().id(aId).name("its a second name").active(Boolean.FALSE);

        //when
        restTemplate.delete(url.formatted(aId));
        var result = restTemplate.getForEntity(url.formatted(aId), IdNameDto.class);

        //then
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expected, result.getBody());
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: do modify a resource would edited in database")
    void givenUpdateById_whenTest5_thenReturns200() {
        //given
        String url = "/ids-names/%s";

        var aId = UUID.fromString("e5558ff1-80b9-4c5f-b63f-3f9e6f540003");
        var command = new IdNameDto();
        command.setName("its a fixed name");

        var expected = new IdNameDto().id(aId).name("its a fixed name").active(Boolean.TRUE);

        //when
        restTemplate.put(url.formatted(aId), command);
        var result = restTemplate.getForEntity(url.formatted(aId), IdNameDto.class);

        //then
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(expected, result.getBody());
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: do create a resource would register in database")
    void givenCreateNew_whenTest6_thenReturns201() {
        //given
        String url = "/ids-names";

        var command = new IdNameDto();
        command.setName("its a new created name");

        //when
        var resultCreated = restTemplate.postForEntity(url, command, Void.class);

        //then
        assertEquals(CREATED, resultCreated.getStatusCode());
        assertNotNull(resultCreated.getHeaders().getFirst("Location"));

        //given
        url = resultCreated.getHeaders().getFirst("Location").replace("http://localhost:49432", "");

        //when
        var result = restTemplate.getForEntity(url, IdNameDto.class);

        //then
        assertEquals(OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getId());
        assertEquals("its a new created name", result.getBody().getName());
        assertTrue(result.getBody().getActive());
    }

}