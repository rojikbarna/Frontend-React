package org.flow.repositories;

import org.flow.models.Ordering;
import org.springframework.data.repository.CrudRepository;

public interface OrderingRepository extends CrudRepository<Ordering, Long>{
    Ordering findByQrCodePath(String path);
    Iterable<Ordering> findAllByOrderByCreatedDesc();

}