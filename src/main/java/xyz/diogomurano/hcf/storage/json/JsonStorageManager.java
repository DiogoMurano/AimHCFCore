package xyz.diogomurano.hcf.storage.json;

import com.google.gson.Gson;
import xyz.diogomurano.hcf.storage.json.types.DeathBanStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsonStorageManager {

    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);
    private final Gson gson;

    private final DeathBanStorage deathBanStorage;

    public JsonStorageManager(Gson gson) {
        this.gson = gson;

        deathBanStorage = new DeathBanStorage();
    }

    public void executeAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    public void shutdown() {
        POOL.shutdown();
    }

    public DeathBanStorage getDeathBanStorage() {
        return deathBanStorage;
    }

    public Gson getGson() {
        return gson;
    }
}
