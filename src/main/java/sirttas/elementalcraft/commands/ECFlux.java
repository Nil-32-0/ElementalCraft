package sirttas.elementalcraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;

public class ECFlux {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ec_flux").then(Commands.literal("check")
                .executes(ECFlux::executeCheck)));
    }

    private static int executeCheck(CommandContext<CommandSourceStack> command) {
        if(command.getSource().getEntity() instanceof Player player) {
            var handler = command.getSource().getLevel().getChunkAt(BlockPos.containing(player.getPosition(1))).getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow();

            player.sendSystemMessage(Component.literal(String.valueOf(handler.getRatio())));
        }
        return Command.SINGLE_SUCCESS;
    }
}
