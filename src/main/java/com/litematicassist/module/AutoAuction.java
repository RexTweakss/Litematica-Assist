package com.litematicassist.module;

import com.litematicassist.config.LAConfig;
import net.minecraft.client.MinecraftClient;

/** One-shot: when enabled and holding an item, sends the configured listing command
 *  (e.g. "ah sell 100") then turns itself off. LEGALITY: a normal chat-command packet,
 *  same as typing it; rate = once per toggle. Command syntax is server-specific. */
public class AutoAuction extends Module {
    public AutoAuction() { super("Auto Auction"); }
    @Override public void tick(MinecraftClient mc) {
        var p = mc.player;
        if (p == null || p.getMainHandStack().isEmpty()) return;
        String cmd = LAConfig.I.auctionCommand.replace("{price}", String.valueOf((long) LAConfig.I.auctionPrice));
        mc.getNetworkHandler().sendChatCommand(cmd);
        toggle(); // back to OFF after one listing
    }
}
