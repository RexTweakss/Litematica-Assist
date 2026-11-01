package com.litematicassist.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
import java.util.*;

public class LAConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("litematicassist.json");
    public static LAConfig I = new LAConfig();

    public Map<String, Boolean> enabled = new HashMap<>();
    // Auto Sell
    public String sellTitle = "Sell";
    public List<String> sellItems = new ArrayList<>(List.of("minecraft:cobblestone"));
    public int keepN = 0;
    public int confirmSlot = -1;      // -1 = no confirm click
    public int clickDelayTicks = 4;
    // Auto Auction (command based)
    public String auctionCommand = "ah sell {price}";
    public double auctionPrice = 100;

    public static void load() {
        try { if (Files.exists(FILE)) { LAConfig c = GSON.fromJson(Files.readString(FILE), LAConfig.class); if (c != null) I = c; } }
        catch (Exception e) { System.err.println("[LitematicAssist] config load failed: " + e); }
    }
    public static void save() {
        try { Files.writeString(FILE, GSON.toJson(I)); }
        catch (Exception e) { System.err.println("[LitematicAssist] config save failed: " + e); }
    }
}
