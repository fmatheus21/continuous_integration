package br.com.fmatheus.app.model.service.impl;

import br.com.fmatheus.app.model.entity.Customer;
import br.com.fmatheus.app.model.repository.CustomerRepository;
import br.com.fmatheus.app.model.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public List<Customer> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return this.repository.save(customer);
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }
}
