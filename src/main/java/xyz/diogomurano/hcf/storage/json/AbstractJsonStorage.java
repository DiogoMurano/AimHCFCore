package xyz.diogomurano.hcf.storage.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import xyz.diogomurano.hcf.deathban.DeathBan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Builder
public abstract class AbstractJsonStorage<T> {

    private final JsonStorageManager storageManager;

    public abstract StorageResult createOrUpdate(T t) throws ExecutionException, InterruptedException;

    public abstract StorageResult delete(T t) throws ExecutionException, InterruptedException;

    public void writeInFile(File file, String data) throws IOException {
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }
}
