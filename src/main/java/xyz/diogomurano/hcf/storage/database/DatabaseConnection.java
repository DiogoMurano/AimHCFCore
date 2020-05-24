package xyz.diogomurano.hcf.storage.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;

public interface DatabaseConnection {

    ExecutorService getExecutor();

    Connection getConnection();

    boolean isConnected();

    void setupConnection();

    default void createTables() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS `death_ban` (`user_unique_id` VARCHAR(32) NOT NULL, `reason`" +
                    " TEXT NOT NULL, `creationMillis` BIGINT(20) NOT NULL, `expiryMillis` BIGINT(20) NOT NULL)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    default void execute(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    default void shutdown() {
        getExecutor().shutdown();
    }

}
