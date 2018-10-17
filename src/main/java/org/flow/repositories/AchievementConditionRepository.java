package org.flow.repositories;

import org.flow.models.Achievement;
import org.flow.models.AchievementCondition;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface AchievementConditionRepository extends CrudRepository<AchievementCondition, Long> {
}
