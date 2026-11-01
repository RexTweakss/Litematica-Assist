package com.litematicassist.module;

import com.litematicassist.config.LAConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public abstract class Module {
    public final String name;
    protected Module(String name) { this.name = name; }
    public boolean isEnabled() { return LAConfig.I.enabled.getOrDefault(name, false); }
    public void toggle() {
        boolean now = !isEnabled();
        LAConfig.I.enabled.put(name, now);
        LAConfig.save();
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!now) onDisable();
        if (mc.inGameHud != null) mc.inGameHud.setOverlayMessage(Text.literal(name + (now ? ": ON" : ": OFF")), false);
    }
    /** Called every client tick while enabled and in a world. */
    public abstract void tick(MinecraftClient mc);
    /** Abort cleanly (also called on death / dimension change). */
    public void onDisable() {}
    public void onAbort() { onDisable(); }
}
