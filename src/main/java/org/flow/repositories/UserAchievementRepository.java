package org.flow.repositories;

import org.flow.models.Achievement;
import org.flow.models.UserAchievement;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface UserAchievementRepository extends CrudRepository<UserAchievement, Long>{
}
