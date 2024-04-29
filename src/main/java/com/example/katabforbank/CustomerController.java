package com.example.katabforbank;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    private final CustomerRepository repository;
    private final CustomerModelAssembler assembler;

    public CustomerController(CustomerRepository repository, CustomerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/customers/{id}")
    EntityModel<Customer> one(@PathVariable Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return assembler.toModel(customer);
    }

    @GetMapping("/customers")
    CollectionModel<EntityModel<Customer>> all() {
        List<EntityModel<Customer>> customerList = repository.findAll().stream().map(
                customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).one(customer.getId())).withSelfRel(),
                        linkTo(methodOn(CustomerController.class).all()).withRel("customers"))).toList();
        return CollectionModel.of(customerList, linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    @PostMapping("/customers")
    ResponseEntity<?> newCustomer(@RequestBody Customer newCustomer) {
        EntityModel<Customer> entityModel = assembler.toModel(repository.save(newCustomer));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/customers/{id}")
    ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/{id}/pot")
    EntityModel<PotResponse> getPot(@PathVariable Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        PotResponse res = new PotResponse(
                customer.getPot(),
                customer.getPassCount(),
                customer.getPotAvailability(),
                customer.getFullName());

        return EntityModel.of(res, linkTo(methodOn(CustomerController.class).getPot(customer.getId())).withSelfRel());
    }

    @PatchMapping("/customers/{id}/pot")
    EntityModel<PotResponse> addPot(@PathVariable Long id, @RequestBody int amount) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customer.addPot(amount);
        repository.save(customer);

        PotResponse res = new PotResponse(
                customer.getPot(),
                customer.getPassCount(),
                customer.getPotAvailability(),
                customer.getFullName());

        return EntityModel.of(res, linkTo(methodOn(CustomerController.class).addPot(id, amount)).withSelfRel());
    }
}
