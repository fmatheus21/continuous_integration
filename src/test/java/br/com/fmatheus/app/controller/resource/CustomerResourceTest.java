package br.com.fmatheus.app.controller.resource;

import br.com.fmatheus.app.controller.facade.CustomerFacade;
import br.com.fmatheus.app.model.entity.Customer;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class CustomerResourceTest {

    private final static String URL = "/customers";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerFacade customerFacade;

    @Autowired
    private Flyway flyway;

    private Customer customer;

    @BeforeEach
    void setUp() {
        flyway.migrate();
    }


    @Test
    @Order(1)
    @DisplayName("Listando clientes")
    void list() {
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .value(resonse -> assertThat(resonse).isNotEmpty());
    }

}