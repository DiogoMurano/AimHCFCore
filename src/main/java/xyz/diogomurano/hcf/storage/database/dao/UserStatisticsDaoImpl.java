package xyz.diogomurano.hcf.storage.database.dao;

import xyz.diogomurano.hcf.storage.database.DatabaseConnection;
import xyz.diogomurano.hcf.user.User;
import xyz.diogomurano.hcf.user.UserStatistics;
import xyz.diogomurano.hcf.user.service.UserService;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserStatisticsDaoImpl implements UserStatisticsDao {

    private final DatabaseConnection databaseConnection;

    public UserStatisticsDaoImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public void createTables() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS `user_statistics` (`unique_id` VARCHAR(36) NOT NULL " +
                    "PRIMARY KEY, `kills` INT(8) NOT NULL DEFAULT '0', `deaths` INT(8) NOT NULL DEFAULT '0', `kill_streak`" +
                    " INT(8) NOT NULL)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void createOrUpdate(UserStatistics userStatistics) {
        databaseConnection.execute(() -> {
            if (findByUniqueId(userStatistics.getUniqueId()).isPresent()) {
                try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE `user_statistics` SET `kills`=?," +
                        " `deaths`=?, `kill_streak`=? WHERE `unique_id`=?")) {
                    stmt.setInt(1, userStatistics.getKills());
                    stmt.setInt(2, userStatistics.getDeaths());
                    stmt.setInt(3, userStatistics.getKillStreak());
                    stmt.setString(4, userStatistics.getUniqueId().toString());
                    stmt.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO `user_statistics` (`unique_id`) VALUES (?)")) {
                    stmt.setString(1, userStatistics.getUniqueId().toString());
                    stmt.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    @Override
    public void delete(UserStatistics userStatistics) {
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM `user_statistics` WHERE `unique_id`=?")) {
            stmt.setString(1, userStatistics.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConnection() {
        return databaseConnection.getConnection();
    }

    @Override
    public Optional<UserStatistics> findByUniqueId(UUID uniqueId) {
        UserStatistics statistics = null;

        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM `user_statistics` WHERE `unique_id`=?")) {
            stmt.setString(1, uniqueId.toString());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                statistics = UserStatistics.builder()
                        .uniqueId(uniqueId)
                        .kills(resultSet.getInt("kills"))
                        .deaths(resultSet.getInt("deaths"))
                        .killStreak(resultSet.getInt("kill_streak"))
                        .build();
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.ofNullable(statistics);
    }
}
