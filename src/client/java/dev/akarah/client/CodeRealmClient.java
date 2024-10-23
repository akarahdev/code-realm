package dev.akarah.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.akarah.client.commands.DevCommand;
import dev.akarah.client.commands.ExprCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;

public class CodeRealmClient implements ClientModInitializer {
    public static boolean INTERCEPT_DEV_TELEPORT_PACKET = false;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(
            (commandDispatcher, commandBuildContext) -> {
                DevCommand.register(commandDispatcher);
                ExprCommand.register(commandDispatcher);
            });
    }
}
