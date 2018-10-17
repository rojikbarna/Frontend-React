package org.flow.repositories;

import org.flow.models.Achievement;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface AchievementRepository extends CrudRepository<Achievement, Long> {
    Iterable<Achievement> findByExpirationAfter(Date now);
    Iterable<Achievement> findAllByOrderByExpirationAsc();
    Iterable<Achievement> findByExpirationBefore(Date now);
}
