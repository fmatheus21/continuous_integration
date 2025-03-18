package br.com.fmatheus.app.controller.resource;

import br.com.fmatheus.app.controller.facade.CustomerFacade;
import br.com.fmatheus.app.model.entity.Customer;
import br.com.fmatheus.app.model.repository.CustomerRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
class CustomerResourceIntegrationTest {

    private static final String URL = "/customers";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerFacade customerFacade;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    static void beforeAll(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @Order(1)
    @DisplayName("Buscando um cliente por ID.")
    void testFindByIdSuccess() {
        var id = 1;
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL.concat("/{id}")).build(id))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);
    }

    @Test
    @Order(2)
    @DisplayName("Buscando um cliente por ID e não foi encontrado.")
    void testFindByIdException() {
        var id = 100;
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL.concat("/{id}")).build(id))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.massage").isEqualTo("Registro não encontrado.");
    }


    @Test
    @Order(3)
    @DisplayName("Listando clientes.")
    void testListNotEmpty() {
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .value(response -> assertThat(response).isNotEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Listando clientes e retornando vazio.")
    void testListIsEmpty() {
        this.repository.deleteAll();
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL).build())
                .exchange()
                .expectStatus().isNoContent()
                .expectBodyList(Customer.class)
                .value(response -> assertThat(response).isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Cadastrando novo registro.")
    void testCreateSuccess() {
        this.webTestClient.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(this.json())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Martin Cauã Vicente Melo")
                .jsonPath("$.document").isEqualTo("11594722897");
    }

    @Test
    @Order(6)
    @DisplayName("Excluindo registro.")
    void testDelete() {
        var id = 1;
        this.webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(URL.concat("/{id}")).build(id))
                .exchange()
                .expectStatus().isOk();
    }


    private String json() {
        return """
                {
                    "name": "Martin Cauã Vicente Melo",
                    "document": "11594722897"
                }
                """;
    }


}