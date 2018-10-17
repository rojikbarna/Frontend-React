package org.flow.repositories;

import org.flow.models.OrderLine;
import org.flow.models.Ordering;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineRepository extends CrudRepository<OrderLine, Long>{
}
