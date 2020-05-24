package xyz.diogomurano.hcf.storage.json;

import net.minecraft.util.com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsonStorageManager {

    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);
    private final Gson gson;

    public JsonStorageManager(Gson gson) {
        this.gson = gson;
    }

    public StorageResult storageFile(File directory, File file, Object object) {
        try {
            return new AbstractJsonStorage(this, directory, file).createOrUpdate(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StorageResult.FAILED;
    }

    public void executeAsync(Runnable runnable) {
        POOL.execute(runnable);
    }

    public void shutdown() {
        POOL.shutdown();
    }

    public Gson getGson() {
        return gson;
    }
}
