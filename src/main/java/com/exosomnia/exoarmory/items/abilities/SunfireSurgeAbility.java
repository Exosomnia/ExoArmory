package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SunfireSurgeAbility extends ArmoryAbility {

    public enum Stats implements AbilityStat {
        DURATION,
        COOLDOWN
    }

    public SunfireSurgeAbility() {
        super("sunfire_surge", 0xFF3F00);
    }

    //region ArmoryAbility Overrides
    public void buildRanks() {
        RANK_STATS.put(Stats.DURATION, new double[]{0.0, 15.0, 20.0, 25.0, 30.0});
        RANK_STATS.put(Stats.COOLDOWN, new double[]{120.0, 120.0, 120.0, 120.0, 120.0});
    }

    @Override
    public List<MutableComponent> getTooltip(DetailLevel detail, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detail, rank));

        switch (detail) {
            case DESCRIPTION:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.sunfire_surge.line.1"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.sunfire_surge.line.2"), ComponentUtils.Styles.DEFAULT_DESC.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.desc.sunfire_surge.line.3"), ComponentUtils.Styles.DEFAULT_DESC.getStyle(),
                        ComponentUtils.Styles.HIGHLIGHT_DESC.getStyle()));
                break;
            case STATISTICS:
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.sunfire_surge.line.1", (int)getStatForRank(Stats.DURATION, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                description.add(ComponentUtils.formatLine(I18n.get("ability.exoarmory.stat.sunfire_surge.line.2", (int)getStatForRank(Stats.COOLDOWN, rank)),
                        ComponentUtils.Styles.DEFAULT_DESC.getStyle(), ComponentUtils.Styles.HIGHLIGHT_STAT.getStyle()));
                break;
        }
        return description;
    }
    //endregion

    //region Ability Logic
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (level.isClientSide || event.phase.equals(TickEvent.Phase.END) || !player.hasEffect(ExoArmory.REGISTRY.EFFECT_SUNFIRE_SURGE.get())) { return; }

        BlockPos pos = player.blockPosition();
        BlockPos belowPos = pos.below();
        if (level.getBlockState(pos).canBeReplaced() && level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP)) {
            level.setBlock(pos, BaseFireBlock.getState(level, pos), Block.UPDATE_ALL);
            player.setRemainingFireTicks(20);
        }
    }
    //endregion
}
