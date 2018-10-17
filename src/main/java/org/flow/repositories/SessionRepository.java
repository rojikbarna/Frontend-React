package org.flow.repositories;

import org.flow.models.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findByToken(String token);
}
