package xyz.diogomurano.hcf.storage.json;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Builder
public abstract class AbstractJsonStorage<T, I> {

    private final JsonStorageManager storageManager;

    public abstract StorageResult createOrUpdate(T t) throws ExecutionException, InterruptedException;

    public abstract StorageResult delete(T t) throws ExecutionException, InterruptedException;

    public abstract T findByI(I i);

    public void writeInFile(File file, String data) throws IOException {
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }
}
