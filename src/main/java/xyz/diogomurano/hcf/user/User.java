package xyz.diogomurano.hcf.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import xyz.diogomurano.hcf.deathban.DeathBan;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class User {

    private final UUID uniqueId;
    private final UserStatistics userStatistics;
    private final DeathBan deathban;


}
