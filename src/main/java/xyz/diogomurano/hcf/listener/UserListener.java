package xyz.diogomurano.hcf.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import xyz.diogomurano.hcf.HCF;
import xyz.diogomurano.hcf.deathban.DeathBan;
import xyz.diogomurano.hcf.storage.database.dao.UserStatisticsDao;
import xyz.diogomurano.hcf.storage.json.types.DeathBanStorage;
import xyz.diogomurano.hcf.user.User;
import xyz.diogomurano.hcf.user.UserStatistics;
import xyz.diogomurano.hcf.user.service.UserService;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserListener implements Listener {

    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);

    private final HCF plugin;
    private final UserStatisticsDao userStatisticsDao;
    private final DeathBanStorage deathBanStorage;

    private final UserService userService;

    public UserListener(HCF plugin) {
        this.plugin = plugin;

        this.userStatisticsDao = plugin.getUserStatisticsDao();
        this.deathBanStorage = plugin.getStorageManager().getDeathBanStorage();

        this.userService = plugin.getUserService();
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        final UUID uniqueId = event.getUniqueId();

        UserStatistics userStatistics = null;
        final Optional<UserStatistics> optionalStatistics = userStatisticsDao.findByUniqueId(uniqueId);
        if (!optionalStatistics.isPresent()) {
            userStatistics = UserStatistics.builder()
                    .uniqueId(uniqueId)
                    .kills(0).deaths(0).killStreak(0)
                    .build();
            userStatisticsDao.createOrUpdate(userStatistics);
        } else {
            userStatistics = optionalStatistics.get();
        }

        final DeathBan deathBan = deathBanStorage.findByI(uniqueId);

        final User user = User.builder()
                .uniqueId(uniqueId)
                .userStatistics(userStatistics)
                .deathban(deathBan)
                .build();
        userService.add(user);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        POOL.execute(() -> {
            final User user = userService.findByUniqueId(player.getUniqueId());
            if (user != null) {
                userStatisticsDao.createOrUpdate(user.getUserStatistics());
                final DeathBan deathban = user.getDeathban();
                if (deathban != null) {
                    try {
                        deathBanStorage.createOrUpdate(deathban);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        final Player player = event.getPlayer();
        POOL.execute(() -> {
            final User user = userService.findByUniqueId(player.getUniqueId());
            if (user != null) {
                userStatisticsDao.createOrUpdate(user.getUserStatistics());
                final DeathBan deathban = user.getDeathban();
                if (deathban != null) {
                    try {
                        deathBanStorage.createOrUpdate(deathban);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @EventHandler
    private void onPluginDisableEvent(PluginDisableEvent event) {
        POOL.shutdown();
    }

}
