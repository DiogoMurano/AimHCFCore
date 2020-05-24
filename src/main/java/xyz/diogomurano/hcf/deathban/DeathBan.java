package xyz.diogomurano.hcf.deathban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import xyz.diogomurano.hcf.user.User;

@AllArgsConstructor
@Builder
@Data
public class DeathBan {

    private final User user;
    private final String reason;
    private final long creationMillis = 0;
    private final long expiryMillis = 0;
    private final Location location;

}
