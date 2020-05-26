package xyz.diogomurano.hcf.storage.database.dao;

import xyz.diogomurano.hcf.storage.database.Crud;
import xyz.diogomurano.hcf.user.UserStatistics;

import java.util.Optional;
import java.util.UUID;

public interface UserStatisticsDao extends Crud<UserStatistics> {

    Optional<UserStatistics> findByUniqueId(UUID uniqueId);

}
