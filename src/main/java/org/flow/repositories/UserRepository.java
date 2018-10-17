package org.flow.repositories;

import org.flow.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    Iterable<User> findAllByOrderByLastNameAsc();
}