package xyz.diogomurano.hcf;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.diogomurano.hcf.storage.database.DatabaseConnection;
import xyz.diogomurano.hcf.storage.database.sqlite.SqliteConnection;
import xyz.diogomurano.hcf.storage.json.JsonStorageManager;
import xyz.diogomurano.hcf.storage.json.utils.ItemStackAdapter;
import xyz.diogomurano.hcf.storage.json.utils.LocationAdapter;
import xyz.diogomurano.hcf.user.service.UserService;
import xyz.diogomurano.hcf.user.service.UserServiceImpl;

import java.io.File;
import java.io.IOException;

public class HCF extends JavaPlugin {

    private static HCF instance;

    private Gson gson;

    private DatabaseConnection databaseConnection;
    private JsonStorageManager storageManager;
    private UserService userService;

    @Override
    public void onLoad() {
        instance = this;

        gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(ItemStackAdapter.class, new ItemStackAdapter())
                .create();

        final File file = new File(getDataFolder(), "database.db");
        saveDefaultConfig();
        createFileOrIgnore(file);

        databaseConnection = new SqliteConnection(file);
        databaseConnection.setupConnection();
        databaseConnection.createTables();

        storageManager = new JsonStorageManager(gson);
        userService = new UserServiceImpl();
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

    public UserService getUserService() {
        return userService;
    }

    public Gson getGson() {
        return gson;
    }

    public static HCF getInstance() {
        return instance;
    }
}
