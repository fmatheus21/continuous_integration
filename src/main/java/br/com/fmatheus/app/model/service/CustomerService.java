package br.com.fmatheus.app.model.service;

import br.com.fmatheus.app.model.entity.Customer;

import java.util.Optional;

public interface CustomerService extends GenericService<Customer, Integer>{

    Optional<Customer> findByDocument(String document);
}
