package xyz.diogomurano.hcf.storage.database.dao.deathban;

import xyz.diogomurano.hcf.storage.database.DatabaseConnection;
import xyz.diogomurano.hcf.deathban.DeathBan;
import xyz.diogomurano.hcf.user.User;

import java.sql.Connection;
import java.util.Optional;

public class DeathBanDaoImpl implements DeathBanDao {

    private final DatabaseConnection databaseConnection;

    public DeathBanDaoImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Optional<DeathBan> findByUser(User user, boolean async) {
        return Optional.empty();
    }

    @Override
    public void createOrUpdate(DeathBan deathBan) {
        databaseConnection.execute(() -> {
            if (findByUser(deathBan.getUser(), false).isPresent()) {

            } else {

            }
        });
    }

    @Override
    public void delete(DeathBan deathBan) {

    }

    public Connection getConnection() {
        return databaseConnection.getConnection();
    }
}
