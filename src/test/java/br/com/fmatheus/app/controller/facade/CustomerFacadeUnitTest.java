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

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerFacade customerFacade;

    private Customer customer;

    @BeforeEach
    void setUp() {
        this.customer = new Customer();
        this.customer.setId(1);
        this.customer.setName("Julia Gabrielly Carvalho");
        this.customer.setDocument("42830860101");
    }

    /**
     * Test: {@link CustomerFacade#save(Customer)}
     */
    @Test
    @Order(1)
    @DisplayName("Salva registro com sucesso.")
    void testSaveSuccess() {
        when(this.customerService.findByDocument(customer.getDocument())).thenReturn(Optional.empty());
        when(this.customerService.save(any(Customer.class))).thenReturn(this.customer);

        var result = this.customerFacade.save(this.customer);
        assertEquals(1, result.getId());
        verify(this.customerService, times(1)).save(this.customer);
    }

    /**
     * Test: {@link CustomerFacade#save(Customer)}
     */
    @Test
    @Order(2)
    @DisplayName("Lança exceção ao tentar salvar registro.")
    void testSaveException() {
        when(this.customerService.findByDocument(this.customer.getDocument())).thenReturn(Optional.of(this.customer));
        var exception = assertThrows(RuntimeException.class, () -> this.customerFacade.save(this.customer));

        assertEquals("O documento informado já existe.", exception.getMessage());
        verify(this.customerService, never()).save(any(Customer.class));
    }

    /**
     * Test: {@link CustomerFacade#findById(Integer)}
     */
    @Test
    @Order(3)
    @DisplayName("Pesquisa registro por ID com sucesso.")
    void testFindByIdSuccess() {
        when(this.customerService.findById(1)).thenReturn(Optional.of(this.customer));

        var result = this.customerFacade.findById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(this.customerService, times(1)).findById(1);
    }

    /**
     * Test: {@link CustomerFacade#findById(Integer)}
     */
    @Test
    @Order(4)
    @DisplayName("Lança exceção ao pesquisa registro por ID.")
    void testFindByIdException() {
        when(this.customerService.findById(1)).thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class, () -> this.customerFacade.findById(1));
        assertEquals("Registro não encontrado.", exception.getMessage());
        verify(this.customerService, times(1)).findById(1);
    }

    /**
     * Test: {@link CustomerFacade#list()}
     */
    @Test
    @Order(5)
    @DisplayName("Retorna uma lista não vazia.")
    void testListNotEmpty() {
        when(this.customerService.findAll()).thenReturn(List.of(this.customer));

        var result = customerFacade.list();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(this.customerService, times(1)).findAll();
    }

    /**
     * Test: {@link CustomerFacade#list()}
     */
    @Test
    @Order(6)
    @DisplayName("Retorna uma lista vazia.")
    void testListEmpty() {
        when(this.customerService.findAll()).thenReturn(Collections.emptyList());

        var result = customerFacade.list();
        assertTrue(result.isEmpty());
        verify(this.customerService, times(1)).findAll();
    }

    /**
     * Test: {@link CustomerFacade#delete(Integer)}
     */
    @Test
    @Order(7)
    @DisplayName("Exclui registro com sucesso.")
    void testDeleteSuccess() {
        doNothing().when(this.customerService).deleteById(1);
        assertDoesNotThrow(() -> this.customerFacade.delete(1));
        verify(this.customerService, times(1)).deleteById(1);
    }
}

