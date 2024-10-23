package dev.akarah.client.expression;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;

public class ExprItemCreator {
    public static ItemStack createExpression(String data, String rawExpr) {
        var item = Items.SHULKER_SHELL.asItem().getDefaultInstance();

        var customValues = new CompoundTag();
        customValues.put("hypercube:varitem", StringTag.valueOf("""
            {"id":"num","data":{"name":"<NAME>"}}
            """.trim().replace("<NAME>", data)));
        var compound = new CompoundTag();
        compound.put("PublicBukkitValues", customValues);

        item.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
        item.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(5000));
        item.set(
            DataComponents.ITEM_NAME,
            Component.literal(rawExpr).withStyle(ChatFormatting.AQUA)
        );
        item.set(
            DataComponents.LORE,
            ItemLore.EMPTY
                .withLineAdded(Component.literal("Item Type: Number").withStyle(ChatFormatting.GRAY)
        ));
        return item;
    }
}
