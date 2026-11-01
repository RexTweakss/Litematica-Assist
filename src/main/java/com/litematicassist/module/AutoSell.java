package com.litematicassist.module;

import com.litematicassist.config.LAConfig;
import com.litematicassist.util.SlotClickHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;

/** While a chest-style GUI whose title contains LAConfig.sellTitle is open: shift-clicks
 *  configured items from the player inventory into it (keeping N), one click per delay,
 *  then optionally clicks the confirm slot. LEGALITY: normal QUICK_MOVE clicks via
 *  SlotClickHelper; max one click per `clickDelayTicks`; aborts if the screen closes. */
public class AutoSell extends Module {
    private int cooldown = 0;
    private boolean confirmed = false;
    public AutoSell() { super("Auto Sell"); }

    @Override public void tick(MinecraftClient mc) {
        var p = mc.player;
        if (p == null || mc.currentScreen == null) { confirmed = false; return; }
        if (!(p.currentScreenHandler instanceof GenericContainerScreenHandler h)) return;
        if (!mc.currentScreen.getTitle().getString().toLowerCase().contains(LAConfig.I.sellTitle.toLowerCase())) return;
        if (cooldown-- > 0) return;
        cooldown = Math.max(2, LAConfig.I.clickDelayTicks);

        for (Slot s : h.slots) {
            if (s.inventory != p.getInventory() || !s.hasStack()) continue;
            String id = Registries.ITEM.getId(s.getStack().getItem()).toString();
            if (!LAConfig.I.sellItems.contains(id)) continue;
            if (countOf(p, id) <= LAConfig.I.keepN) continue;     // keep-N threshold
            SlotClickHelper.shiftClick(mc, s.id);
            confirmed = false;
            return;
        }
        int c = LAConfig.I.confirmSlot;
        if (!confirmed && c >= 0 && c < h.slots.size()) {
            SlotClickHelper.click(mc, c, 0, net.minecraft.screen.slot.SlotActionType.PICKUP);
            confirmed = true;
        }
    }
    private int countOf(net.minecraft.client.network.ClientPlayerEntity p, String id) {
        int n = 0;
        for (int i = 0; i < p.getInventory().size(); i++) {
            var st = p.getInventory().getStack(i);
            if (!st.isEmpty() && Registries.ITEM.getId(st.getItem()).toString().equals(id)) n += st.getCount();
        }
        return n;
    }
    @Override public void onDisable() { cooldown = 0; confirmed = false; }
}
