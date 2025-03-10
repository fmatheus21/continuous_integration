package br.com.fmatheus.app.controller.resource;

import br.com.fmatheus.app.controller.facade.CustomerFacade;
import br.com.fmatheus.app.model.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerResource {
    
    private final CustomerFacade facade;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Customer save(@RequestBody Customer customer){
        return this.facade.save(customer);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Customer findById(@PathVariable Integer id){
        return this.facade.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<Customer> list(){
        return this.facade.list();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public  void delete(@PathVariable Integer id){
        this.facade.delete(id);
    }
}
