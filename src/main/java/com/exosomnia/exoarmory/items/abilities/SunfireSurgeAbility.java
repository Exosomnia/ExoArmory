package com.exosomnia.exoarmory.items.abilities;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.utils.TooltipUtils;
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
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SunfireSurgeAbility extends ArmoryAbility {

    public SunfireSurgeAbility() {
        super("sunfire_surge", 0xFF3F00);
    }

    @Override
    public List<MutableComponent> getTooltip(boolean detailed, int rank) {
        List<MutableComponent> description = new ArrayList<>(super.getTooltip(detailed, rank));

        if (detailed) {
            description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.sunfire_surge.line.1"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
            description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.sunfire_surge.line.2"), TooltipUtils.Styles.DEFAULT_DESC.getStyle()));
            description.add(TooltipUtils.formatLine(I18n.get("ability.exoarmory.desc.sunfire_surge.line.3"), TooltipUtils.Styles.DEFAULT_DESC.getStyle(),
                    TooltipUtils.Styles.HIGHLIGHT_DESC.getStyle()));
        }
        return description;
    }

    //region Ability Logic
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (level.isClientSide || !player.hasEffect(ExoArmory.REGISTRY.EFFECT_STELLAR_INFUSION.get())) { return; }

        BlockPos pos = player.blockPosition();
        BlockPos belowPos = pos.below();
        if (level.getBlockState(pos).canBeReplaced() && level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP)) {
            level.setBlock(pos, BaseFireBlock.getState(level, pos), Block.UPDATE_ALL);
            player.setRemainingFireTicks(20);
        }
    }
    //endregion
}
