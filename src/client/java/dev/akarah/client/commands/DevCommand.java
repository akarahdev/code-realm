package dev.akarah.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;

public class DevCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("cdev").executes(ctx -> {
            ctx.getSource().sendFeedback(Component.literal("Doing alternate dev callback"));
            Minecraft.getInstance().player.connection.send(new ServerboundChatCommandPacket("dev"));
            return 0;
        }));
    }
}
