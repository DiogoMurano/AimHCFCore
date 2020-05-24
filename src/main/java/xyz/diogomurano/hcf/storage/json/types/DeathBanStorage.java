package xyz.diogomurano.hcf.storage.json.types;

import net.minecraft.util.com.google.gson.Gson;
import xyz.diogomurano.hcf.HCF;
import xyz.diogomurano.hcf.deathban.DeathBan;
import xyz.diogomurano.hcf.storage.json.AbstractJsonStorage;
import xyz.diogomurano.hcf.storage.json.JsonStorageManager;
import xyz.diogomurano.hcf.storage.json.StorageResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DeathBanStorage extends AbstractJsonStorage<DeathBan> {

    private final JsonStorageManager storageManager;
    private final File directory;
    private final Gson gson;

    public DeathBanStorage() {
        super(HCF.getInstance().getStorageManager());
        storageManager = HCF.getInstance().getStorageManager();
        gson = storageManager.getGson();

        directory = new File(HCF.getInstance().getDataFolder().getAbsolutePath() + File.separator + "deathban");
        directory.mkdirs();
    }

    @Override
    public StorageResult createOrUpdate(DeathBan deathBan) throws ExecutionException, InterruptedException {
        File file = new File(directory.getAbsolutePath(), deathBan.getUser().getUniqueId().toString() + ".yml");
        CompletableFuture<StorageResult> completableFuture = new CompletableFuture<>();
        storageManager.executeAsync(() -> {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                writeInFile(file, gson.toJson(deathBan, DeathBan.class));
                completableFuture.complete(StorageResult.SUCCESSFULLY);
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            }
        });
        completableFuture.exceptionally(throwable -> StorageResult.FAILED);
        return completableFuture.get();
    }

    @Override
    public StorageResult delete(DeathBan deathBan) throws ExecutionException, InterruptedException {
        File file = new File(directory.getAbsolutePath(), deathBan.getUser().getUniqueId().toString() + ".yml");
        CompletableFuture<StorageResult> completableFuture = new CompletableFuture<>();

        storageManager.executeAsync(() -> {
            if (file.exists()) {
                file.delete();
            }
            completableFuture.complete(StorageResult.SUCCESSFULLY);
        });

        return completableFuture.get();
    }
}
