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

// Define que o perfil de teste será utilizado durante a execução dos testes
@ActiveProfiles("test")
// Define que o contexto da aplicação será inicializado com um servidor em uma porta aleatória
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Configura o WebTestClient automaticamente para os testes
@AutoConfigureWebTestClient
// Define a ordem de execução dos testes
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// Estende a funcionalidade do JUnit 5 com suporte ao Spring
@ExtendWith(SpringExtension.class)
class CustomerResourceIntegrationTest {

    // Define a URL base dos endpoints testados
    private static final String URL = "/customers";

    // Injeta o WebTestClient, usado para realizar chamadas HTTP nos testes
    @Autowired
    private WebTestClient webTestClient;

    // Injeta a fachada do serviço de clientes
    @Autowired
    private CustomerFacade customerFacade;

    // Injeta o repositório para manipulação direta do banco de dados durante os testes
    @Autowired
    private CustomerRepository repository;

    // Injeta o Flyway para gerenciamento de migrações do banco de dados
    @Autowired
    private Flyway flyway;

    // Método executado antes de todos os testes para limpar e recriar o banco de dados
    @BeforeAll
    static void beforeAll(@Autowired Flyway flyway) {
        flyway.clean();   // Remove todas as tabelas do banco
        flyway.migrate(); // Reaplica todas as migrations do banco
    }

    @Test
    @Order(1)
    @DisplayName("Buscando um cliente por ID.")
    void testFindByIdSuccess() {
        var id = 1; // Define o ID a ser pesquisado
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL.concat("/{id}")).build(id)) // Monta a URL com o ID do cliente
                .exchange() // Executa a requisição
                .expectStatus().isOk() // Espera que o status da resposta seja 200 OK
                .expectBody() // Captura o corpo da resposta
                .jsonPath("$.id").isEqualTo(id); // Verifica se o ID retornado é o esperado
    }

    @Test
    @Order(2)
    @DisplayName("Buscando um cliente por ID e não foi encontrado.")
    void testFindByIdException() {
        var id = 100; // Define um ID inexistente
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL.concat("/{id}")).build(id)) // Monta a URL com o ID
                .exchange() // Executa a requisição
                .expectStatus().is4xxClientError() // Espera um erro 4xx
                .expectBody()
                .jsonPath("$.massage").isEqualTo("Registro não encontrado."); // Verifica se a mensagem de erro é a esperada
    }


    @Test
    @Order(3)
    @DisplayName("Listando clientes.")
    void testListNotEmpty() {
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL).build()) // Monta a URL do endpoint de listagem
                .exchange() // Executa a requisição
                .expectStatus().isOk() // Espera que o status seja 200 OK
                .expectBodyList(Customer.class) // Captura a resposta como uma lista de clientes
                .value(response -> assertThat(response).isNotEmpty()); // Verifica se a lista não está vazia
    }

    @Test
    @Order(4)
    @DisplayName("Listando clientes e retornando vazio.")
    void testListIsEmpty() {
        this.repository.deleteAll(); // Remove todos os registros do banco
        this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL).build()) // Monta a URL do endpoint de listagem
                .exchange() // Executa a requisição
                .expectStatus().isNoContent() // Espera um status 204 No Content
                .expectBodyList(Customer.class)
                .value(response -> assertThat(response).isEmpty()); // Verifica se a lista retornada está vazia
    }

    @Test
    @Order(5)
    @DisplayName("Cadastrando novo registro.")
    void testCreateSuccess() {
        this.webTestClient.post()
                .uri(URL) // Define o endpoint de criação de clientes
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                .bodyValue(this.json()) // Envia o JSON com os dados do novo cliente
                .exchange() // Executa a requisição
                .expectStatus().isCreated() // Espera um status 201 Created
                .expectBody()
                .jsonPath("$.id").isNotEmpty() // Verifica se o ID do cliente foi gerado
                .jsonPath("$.name").isEqualTo("Martin Cauã Vicente Melo") // Verifica se o nome está correto
                .jsonPath("$.document").isEqualTo("11594722897"); // Verifica se o documento está correto
    }

    @Test
    @Order(6)
    @DisplayName("Excluindo registro.")
    void testDelete() {
        var id = 1; // Define o ID do cliente a ser excluído
        this.webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(URL.concat("/{id}")).build(id)) // Monta a URL com o ID do cliente
                .exchange() // Executa a requisição
                .expectStatus().isOk(); // Espera um status 200 OK
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