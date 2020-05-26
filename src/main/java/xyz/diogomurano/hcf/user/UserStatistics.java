package xyz.diogomurano.hcf.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class UserStatistics {

    private final UUID uniqueId;
    private int kills;
    private int deaths;
    private int killStreak;


}
