package com.litematicassist.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.SlotActionType;

/** LEGALITY: goes through ClientPlayerInteractionManager.clickSlot, which builds the vanilla
 *  ClickSlotC2SPacket with the handler's current syncId/revision (stateId) and predicted slot
 *  changes. No hand-built packets, so ids can't desync from client state. */
public final class SlotClickHelper {
    private SlotClickHelper() {}
    public static void click(MinecraftClient mc, int slot, int button, SlotActionType type) {
        if (mc.player == null || mc.interactionManager == null) return;
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, button, type, mc.player);
    }
    public static void shiftClick(MinecraftClient mc, int slot) { click(mc, slot, 0, SlotActionType.QUICK_MOVE); }
}
