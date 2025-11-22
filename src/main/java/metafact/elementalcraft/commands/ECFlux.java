package metafact.elementalcraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import metafact.elementalcraft.api.ElementalCraftCapabilities;

public class ECFlux {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ec_flux").requires(s -> s.hasPermission(2))
                .then(Commands.literal("check").executes(ECFlux::executeCheck))
                .then(Commands.literal("add").then(Commands.argument("amount",
                        FloatArgumentType.floatArg(0F, 1000000F))
                        .executes(ECFlux::executeAdd)
                ))
                .then(Commands.literal("drain").then(Commands.argument("amount",
                        FloatArgumentType.floatArg(0F, 1000000F))
                        .executes(ECFlux::executeDrain)
                ))
        );
    }

    private static int executeCheck(CommandContext<CommandSourceStack> command) {
        if(command.getSource().getEntity() instanceof Player player) {
            var chunk = command.getSource().getLevel().getChunkAt(player.blockPosition());
            var handler = chunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow();

            player.sendSystemMessage(Component.literal("Current ratio: " + handler.getRatio()));
            player.sendSystemMessage(Component.literal("("+handler.getCurrentFlux()+"/"+handler.getMax()+")"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int executeAdd(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            var chunk = command.getSource().getLevel().getChunkAt(player.blockPosition());
            var handler = chunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow();

            handler.replenish(FloatArgumentType.getFloat(command, "amount"));
            executeCheck(command);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int executeDrain(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            var chunk = command.getSource().getLevel().getChunkAt(player.blockPosition());
            var handler = chunk.getCapability(ElementalCraftCapabilities.SOURCE_FLUX).resolve().orElseThrow();

            handler.consume(FloatArgumentType.getFloat(command, "amount"));
            executeCheck(command);
        }
        return Command.SINGLE_SUCCESS;
    }
}
