package xyz.diogomurano.hcf.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserStatistics {

    private int kills;
    private int deaths;
    private int killStreak;
    private long playTime;
    private long lastSeen;

}
