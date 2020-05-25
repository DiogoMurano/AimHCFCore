package xyz.diogomurano.hcf.storage.json.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonDeserializer<Location>, JsonSerializer<Location> {

    private static final String CORD_X = "cord-x";
    private static final String CORD_Y = "cord-y";
    private static final String CORD_Z = "cord-z";
    private static final String WORLD = "world-name";

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject obj = jsonElement.getAsJsonObject();
        try {
            int x = obj.get(CORD_X).getAsInt();
            int y = obj.get(CORD_Y).getAsInt();
            int z = obj.get(CORD_Z).getAsInt();
            String world = obj.get(WORLD).getAsString();
            return new Location(Bukkit.getWorld(world), x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JsonElement serialize(Location src, Type type, JsonSerializationContext context) {
        final JsonObject obj = new JsonObject();
        try {
            obj.addProperty(CORD_X, src.getBlockX());
            obj.addProperty(CORD_Y, src.getBlockY());
            obj.addProperty(CORD_Z, src.getBlockZ());
            obj.addProperty(WORLD, src.getWorld().getName());
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
