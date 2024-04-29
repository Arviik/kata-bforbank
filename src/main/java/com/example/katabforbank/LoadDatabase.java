package com.example.katabforbank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer("Olivia Manning"));
            customerRepository.save(new Customer("Vanna Haney"));
            customerRepository.save(new Customer("Brian Craig"));
            customerRepository.save(new Customer("Owen Rhodes"));
            customerRepository.save(new Customer("Kasimir Frank"));

            customerRepository.save(new Customer("Linus Gray", 1, 5));
            customerRepository.save(new Customer("Rachel Workman", 2, 15));
            customerRepository.save(new Customer("Lester Knowles", 3, 8));
            customerRepository.save(new Customer("Emmanuel Tyler", 3, 10));
            customerRepository.save(new Customer("Linda Mckay", 4, 20));

            customerRepository.findAll().forEach(customer -> log.info("Preloaded {}", customer));
        };
    }
}
