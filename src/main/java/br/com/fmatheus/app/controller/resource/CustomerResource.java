package br.com.fmatheus.app.controller.resource;

import br.com.fmatheus.app.controller.facade.CustomerFacade;
import br.com.fmatheus.app.model.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

    private final CustomerFacade facade;

    public CustomerResource(CustomerFacade facade) {
        this.facade = facade;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return this.facade.save(customer);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Customer findById(@PathVariable Integer id) {
        return this.facade.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Collection<Customer>> list() {
        var result = this.facade.list();
        return !result.isEmpty() ? ResponseEntity.ok(result) : ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.facade.delete(id);
    }
}
