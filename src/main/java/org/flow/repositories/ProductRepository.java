package org.flow.repositories;

import org.flow.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product findByName(String name);
    Iterable<Product> findAllByOrderByCategoryAscNameAsc();
}
