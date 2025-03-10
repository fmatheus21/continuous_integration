package br.com.fmatheus.app.controller.facade;

import br.com.fmatheus.app.model.entity.Customer;
import br.com.fmatheus.app.model.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class CustomerFacade {

    private final CustomerService customerService;

    public Customer save(Customer customer){
        return this.customerService.save(customer);
    }

    public Customer findById(Integer id){
        return this.customerService.findById(id).orElseThrow(()-> new RuntimeException("Registro não encontrado"));
    }

    public Collection<Customer> list(){
        return this.customerService.findAll();
    }

    public  void delete(Integer id){
        this.customerService.deleteById(id);
    }
}
