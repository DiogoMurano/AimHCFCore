package xyz.diogomurano.hcf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.diogomurano.hcf.command.CommandMapUtil;
import xyz.diogomurano.hcf.command.registry.AbstractCommandRegistry;
import xyz.diogomurano.hcf.command.registry.ChatControlCommand;
import xyz.diogomurano.hcf.configurations.MessageTag;
import xyz.diogomurano.hcf.listener.fix.PearlGlitchListener;
import xyz.diogomurano.hcf.listener.user.UserListener;
import xyz.diogomurano.hcf.listener.world.AutoSmeltListener;
import xyz.diogomurano.hcf.storage.database.DatabaseConnection;
import xyz.diogomurano.hcf.storage.database.dao.UserStatisticsDao;
import xyz.diogomurano.hcf.storage.database.dao.UserStatisticsDaoImpl;
import xyz.diogomurano.hcf.storage.database.sqlite.SqliteConnection;
import xyz.diogomurano.hcf.storage.json.JsonStorageManager;
import xyz.diogomurano.hcf.storage.json.utils.ItemStackAdapter;
import xyz.diogomurano.hcf.storage.json.utils.LocationAdapter;
import xyz.diogomurano.hcf.user.service.UserService;
import xyz.diogomurano.hcf.user.service.UserServiceImpl;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class HCF extends JavaPlugin {

    private static HCF instance;

    private Gson gson;

    private DatabaseConnection databaseConnection;
    private JsonStorageManager storageManager;

    private UserStatisticsDao userStatisticsDao;

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

        userStatisticsDao = new UserStatisticsDaoImpl(databaseConnection);
        userStatisticsDao.createTables();

        userService = new UserServiceImpl();
    }

    @Override
    public void onEnable() {
        registerListeners();
    }

    @Override
    public void onDisable() {
        databaseConnection.shutdown();
    }

    private void registerCommands() {
        registerCommand(new ChatControlCommand());
    }

    private void registerListeners() {
        //Fix
        getServer().getPluginManager().registerEvents(new PearlGlitchListener(), this);
        //User
        getServer().getPluginManager().registerEvents(new UserListener(this), this);
        //World
        getServer().getPluginManager().registerEvents(new AutoSmeltListener(), this);
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

    private void registerCommand(AbstractCommandRegistry commandRegistry) {
        commandRegistry.registerCommands();
        getServer().getPluginManager().registerEvents(commandRegistry, this);
    }

    public <T extends CommandExecutor> void registerCommand(@Nonnull T command, @Nonnull String... aliases) {
        CommandMapUtil.registerCommand(this, command, aliases);
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public JsonStorageManager getStorageManager() {
        return storageManager;
    }

    public UserStatisticsDao getUserStatisticsDao() {
        return userStatisticsDao;
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
