package xyz.diogomurano.hcf.storage.database.dao.deathban;

import xyz.diogomurano.hcf.storage.database.Crud;
import xyz.diogomurano.hcf.deathban.DeathBan;
import xyz.diogomurano.hcf.user.User;

import java.util.Optional;

public interface DeathBanDao extends Crud<DeathBan> {

    Optional<DeathBan> findByUser(User user, boolean async);

}
