package com.litematicassist;

import com.litematicassist.config.LAConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import java.util.*;

public class ModMenuIntegration implements ModMenuApi {
    @Override public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder b = ConfigBuilder.create().setParentScreen(parent)
                .setTitle(Text.literal("LitematicAssist")).setSavingRunnable(LAConfig::save);
            ConfigEntryBuilder eb = b.entryBuilder();
            var c = LAConfig.I;
            // Cloth renders categories as a left-hand list with entries on the right.
            for (var m : LitematicAssist.MODULES) {
                var cat = b.getOrCreateCategory(Text.literal(m.name));
                cat.addEntry(eb.startBooleanToggle(Text.literal("Enabled"), m.isEnabled())
                    .setSaveConsumer(v -> c.enabled.put(m.name, v)).build());
                switch (m.name) {
                    case "Auto Sell" -> {
                        cat.addEntry(eb.startStrField(Text.literal("GUI title contains"), c.sellTitle).setSaveConsumer(v -> c.sellTitle = v).build());
                        cat.addEntry(eb.startStrList(Text.literal("Items (ids)"), c.sellItems).setSaveConsumer(v -> c.sellItems = new ArrayList<>(v)).build());
                        cat.addEntry(eb.startIntField(Text.literal("Keep N"), c.keepN).setMin(0).setSaveConsumer(v -> c.keepN = v).build());
                        cat.addEntry(eb.startIntField(Text.literal("Confirm slot (-1 none)"), c.confirmSlot).setSaveConsumer(v -> c.confirmSlot = v).build());
                        cat.addEntry(eb.startIntField(Text.literal("Click delay (ticks)"), c.clickDelayTicks).setMin(2).setSaveConsumer(v -> c.clickDelayTicks = v).build());
                    }
                    case "Auto Auction" -> {
                        cat.addEntry(eb.startStrField(Text.literal("Command ({price})"), c.auctionCommand).setSaveConsumer(v -> c.auctionCommand = v).build());
                        cat.addEntry(eb.startDoubleField(Text.literal("Price"), c.auctionPrice).setMin(0).setSaveConsumer(v -> c.auctionPrice = v).build());
                    }
                    default -> {}
                }
            }
            return b.build();
        };
    }
}
