package dev.akarah.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.akarah.client.expression.ExprItemCreator;
import dev.akarah.client.expression.ExpressionParser;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ExprCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("expr")
                .then(
                    ClientCommandManager.argument("expression", StringArgumentType.greedyString())
                        .executes(ExprCommand::execute)
                )
        );
    }

    public static int execute(CommandContext<FabricClientCommandSource> context) {
        var expression = context.getArgument("expression", String.class);
        var parsed = ExpressionParser.parse(expression + "⊇          ");
        var item = ExprItemCreator.createExpression(parsed, expression);
        var slot = Minecraft.getInstance().player.getInventory().selected;
        Minecraft.getInstance().player.getInventory().setItem(slot, item);
        Minecraft.getInstance().player.connection.send(new ServerboundSetCreativeModeSlotPacket(36 + slot, item));
        return 0;

    }
}
