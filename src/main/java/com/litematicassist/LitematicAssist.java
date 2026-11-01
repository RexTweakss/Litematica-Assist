package com.litematicassist;

import com.litematicassist.config.LAConfig;
import com.litematicassist.module.*;
import com.litematicassist.module.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import java.util.*;

public class LitematicAssist implements ClientModInitializer {
    public static final List<Module> MODULES = new ArrayList<>();
    private static final Map<Module, KeyBinding> KEYS = new LinkedHashMap<>();
    public static boolean LITEMATICA, METEOR;
    private static net.minecraft.world.World lastWorld;
    private static boolean wasAlive = true;

    @Override public void onInitializeClient() {
        LAConfig.load();
        LITEMATICA = FabricLoader.getInstance().isModLoaded("litematica");
        METEOR = FabricLoader.getInstance().isModLoaded("meteor-client"); // detected only; no hooks yet

        AutoMace mace = new AutoMace();
        MODULES.add(mace); MODULES.add(new AutoSell()); MODULES.add(new AutoAuction());

        // NOTE: KeyBinding.Category constructor form is 1.21.9+; verify against yarn.
        var cat = KeyBinding.Category.create(Identifier.of("litematicassist", "main"));
        for (Module m : MODULES)
            KEYS.put(m, KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.litematicassist." + m.name.toLowerCase().replace(' ', '_'),
                    InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), cat))); // default unbound

        UseItemCallback.EVENT.register((p, w, h) -> {
            if (w.isClient() && mace.isEnabled()) mace.noteItemUse(p.getStackInHand(h));
            return ActionResult.PASS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            for (var e : KEYS.entrySet()) while (e.getValue().wasPressed()) e.getKey().toggle();
            if (mc.player == null || mc.world == null) { lastWorld = null; return; }
            // Abort cleanly on death or dimension/world change.
            boolean alive = mc.player.isAlive();
            if ((lastWorld != null && lastWorld != mc.world) || (wasAlive && !alive))
                for (Module m : MODULES) m.onAbort();
            lastWorld = mc.world; wasAlive = alive;
            if (!alive) return;
            for (Module m : MODULES) if (m.isEnabled()) m.tick(mc);
        });
    }
}
