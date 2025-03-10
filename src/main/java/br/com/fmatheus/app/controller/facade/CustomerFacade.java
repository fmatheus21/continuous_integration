package br.com.fmatheus.app.controller.facade;

import br.com.fmatheus.app.controller.exception.NotFoundException;
import br.com.fmatheus.app.model.entity.Customer;
import br.com.fmatheus.app.model.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomerFacade {

    private final CustomerService customerService;

    public CustomerFacade(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer save(Customer customer) {
        var result = this.customerService.findByDocument(customer.getDocument());
        if (result.isPresent()) {
            throw new RuntimeException("O documento informado já existe.");
        }
        return this.customerService.save(customer);
    }

    public Customer findById(Integer id) {
        return this.customerService.findById(id).orElseThrow(() -> new NotFoundException("Registro não encontrado."));
    }

    public Collection<Customer> list() {
        return this.customerService.findAll();
    }

    public void delete(Integer id) {
        this.customerService.deleteById(id);
    }

}
