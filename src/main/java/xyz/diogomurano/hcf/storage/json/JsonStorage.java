package xyz.diogomurano.hcf.storage.json;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Builder
public class JsonStorage {

    private final JsonStorageManager storageManager;
    private final File directory;
    private final File file;

    public StorageResult createOrUpdate(Object object) throws ExecutionException, InterruptedException {
        CompletableFuture<StorageResult> completableFuture = new CompletableFuture<>();
        storageManager.executeAsync(() -> {
            try {
                directory.mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                }
                final FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(storageManager.getGson().toJson(object, object.getClass()));
                fileWriter.flush();
                fileWriter.close();
                completableFuture.complete(StorageResult.SUCCESSFULLY);
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            }
        });
        completableFuture.exceptionally(throwable -> StorageResult.FAILED);
        return completableFuture.get();
    }
}
