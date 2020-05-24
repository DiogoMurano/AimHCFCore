package xyz.diogomurano.hcf;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.diogomurano.hcf.storage.database.DatabaseConnection;
import xyz.diogomurano.hcf.storage.database.sqlite.SqliteConnection;
import xyz.diogomurano.hcf.storage.json.JsonStorageManager;

import java.io.File;
import java.io.IOException;

public class HCF extends JavaPlugin {

    private static HCF instance;

    private Gson gson;

    private DatabaseConnection databaseConnection;
    private JsonStorageManager storageManager;

    @Override
    public void onLoad() {
        instance = this;

        gson = new GsonBuilder().create();

        final File file = new File(getDataFolder(), "database.db");
        saveDefaultConfig();
        createFileOrIgnore(file);

        databaseConnection = new SqliteConnection(file);
        databaseConnection.setupConnection();
        databaseConnection.createTables();

        storageManager = new JsonStorageManager(gson);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        databaseConnection.shutdown();
    }

    private void createFileOrIgnore(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public JsonStorageManager getStorageManager() {
        return storageManager;
    }

    public Gson getGson() {
        return gson;
    }

    public static HCF getInstance() {
        return instance;
    }
}
