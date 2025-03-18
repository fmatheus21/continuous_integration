package br.com.fmatheus.app.controller.facade;

import br.com.fmatheus.app.controller.exception.NotFoundException;
import br.com.fmatheus.app.model.entity.Customer;
import br.com.fmatheus.app.model.service.CustomerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Teste de Customer")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class CustomerFacadeUnitTest {


    @Mock // Cria um mock do serviço CustomerService
    private CustomerService customerService;

    @InjectMocks // Injeta o mock de CustomerService na instância de CustomerFacade
    private CustomerFacade customerFacade;

    private Customer customer;

    @BeforeEach
        // Configuração antes de cada teste
    void setUp() {
        this.customer = new Customer();
        this.customer.setId(1);
        this.customer.setName("Julia Gabrielly Carvalho");
        this.customer.setDocument("42830860101");
    }

    /**
     * Testa o método {@link CustomerFacade#save(Customer)}, garantindo que um cliente seja salvo corretamente.
     */
    @Test
    @Order(1)
    @DisplayName("Salva registro com sucesso.")
    void testSaveSuccess() {
        when(this.customerService.findByDocument(customer.getDocument())).thenReturn(Optional.empty()); // Simula um cliente inexistente
        when(this.customerService.save(any(Customer.class))).thenReturn(this.customer); // Simula o salvamento do cliente

        var result = this.customerFacade.save(this.customer); // Executa o método save
        assertEquals(1, result.getId()); // Verifica se o ID do cliente retornado é o esperado
        verify(this.customerService, times(1)).save(this.customer); // Garante que o método save foi chamado uma vez

    }

    /**
     * Testa o método {@link CustomerFacade#save(Customer)} e lança uma exceção ao tentar salvar um cliente com documento duplicado.
     */
    @Test
    @Order(2)
    @DisplayName("Lança exceção ao tentar salvar registro.")
    void testSaveException() {
        when(this.customerService.findByDocument(this.customer.getDocument())).thenReturn(Optional.of(this.customer)); // Simula cliente já existente
        var exception = assertThrows(RuntimeException.class, () -> this.customerFacade.save(this.customer)); // Testa se a exceção é lançada

        assertEquals("O documento informado já existe.", exception.getMessage()); // Valida a mensagem da exceção
        verify(this.customerService, never()).save(any(Customer.class)); // Garante que o método save nunca foi chamado

    }

    /**
     * Testa o método {@link CustomerFacade#findById(Integer)} e busca de um cliente por ID.
     */
    @Test
    @Order(3)
    @DisplayName("Pesquisa registro por ID com sucesso.")
    void testFindByIdSuccess() {
        when(this.customerService.findById(1)).thenReturn(Optional.of(this.customer)); // Simula a busca bem-sucedida de um cliente

        var result = this.customerFacade.findById(1); // Chama o método findById
        assertNotNull(result); // Garante que o resultado não é nulo
        assertEquals(1, result.getId()); // Verifica se o ID é o esperado
        verify(this.customerService, times(1)).findById(1); // Garante que o método findById foi chamado uma vez

    }

    /**
     * Testa o método {@link CustomerFacade#findById(Integer)} e lança uma exceção ao tentar buscar um cliente inexistente.
     */
    @Test
    @Order(4)
    @DisplayName("Lança exceção ao pesquisa registro por ID.")
    void testFindByIdException() {
        when(this.customerService.findById(1)).thenReturn(Optional.empty()); // Simula cliente não encontrado

        var exception = assertThrows(NotFoundException.class, () -> this.customerFacade.findById(1)); // Testa se a exceção é lançada
        assertEquals("Registro não encontrado.", exception.getMessage()); // Verifica a mensagem da exceção
        verify(this.customerService, times(1)).findById(1); // Garante que o método findById foi chamado uma vez
    }

    /**
     * Testa o método {@link CustomerFacade#list()} e retorna uma lista não vazia.
     */
    @Test
    @Order(5)
    @DisplayName("Retorna uma lista não vazia.")
    void testListNotEmpty() {
        when(this.customerService.findAll()).thenReturn(List.of(this.customer)); // Simula uma lista com um cliente

        var result = customerFacade.list(); // Chama o método list
        assertFalse(result.isEmpty()); // Verifica se a lista não está vazia
        assertEquals(1, result.size()); // Verifica o tamanho da lista
        verify(this.customerService, times(1)).findAll(); // Garante que o método findAll foi chamado uma vez
    }

    /**
     * Testa o método {@link CustomerFacade#list()} e retorna uma lista vazia.
     */
    @Test
    @Order(6)
    @DisplayName("Retorna uma lista vazia.")
    void testListEmpty() {
        when(this.customerService.findAll()).thenReturn(Collections.emptyList()); // Simula uma lista vazia

        var result = customerFacade.list(); // Chama o método list
        assertTrue(result.isEmpty()); // Verifica se a lista está vazia
        verify(this.customerService, times(1)).findAll(); // Garante que o método findAll foi chamado uma vez
    }

    /**
     * Testa o método {@link CustomerFacade#delete(Integer)} excluindo um registro.
     */
    @Test
    @Order(7)
    @DisplayName("Exclui registro com sucesso.")
    void testDeleteSuccess() {
        doNothing().when(this.customerService).deleteById(1); // Simula uma deleção bem-sucedida
        assertDoesNotThrow(() -> this.customerFacade.delete(1)); // Garante que nenhuma exceção é lançada
        verify(this.customerService, times(1)).deleteById(1); // Garante que o método deleteById foi chamado uma vez
    }

}

