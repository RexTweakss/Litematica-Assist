package com.litematicassist.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/** Attacks once after a wind charge was used, while falling, if the crosshair is ALREADY on a
 *  living entity within 3.0 blocks. No rotation changes. LEGALITY: one vanilla attack
 *  (PlayerInteractEntityC2SPacket via interactionManager) + swing; same as a manual click. */
public class AutoMace extends Module {
    private int lastWindChargeTick = -1000;
    private int tickCount = 0;
    public AutoMace() { super("Auto Mace"); }

    /** Called from the UseItemCallback registered in LitematicAssist. */
    public void noteItemUse(net.minecraft.item.ItemStack s) {
        if (s.isOf(Items.WIND_CHARGE)) lastWindChargeTick = tickCount;
    }

    @Override public void tick(MinecraftClient mc) {
        tickCount++;
        var p = mc.player;
        if (p == null || mc.interactionManager == null) return;
        if (tickCount - lastWindChargeTick > 60) return;          // only shortly after a wind charge
        if (!p.getMainHandStack().isOf(Items.MACE)) return;
        double fall = p.fallDistance;
        if (p.isOnGround() || fall < 1.5 || p.getVelocity().y > 0) return; // must be falling
        if (p.getAttackCooldownProgress(0f) < 1.0f) return;       // cooldown ready
        if (!(mc.crosshairTarget instanceof EntityHitResult hit) || hit.getType() != HitResult.Type.ENTITY) return;
        if (!(hit.getEntity() instanceof LivingEntity target) || !target.isAlive()) return;
        double reach = Math.min(3.0, p.getEntityInteractionRange());
        if (p.distanceTo(target) > reach) return;
        mc.interactionManager.attackEntity(p, target);
        p.swingHand(Hand.MAIN_HAND);
        lastWindChargeTick = -1000;                                // one attack per charge
    }
    @Override public void onDisable() { lastWindChargeTick = -1000; }
}
