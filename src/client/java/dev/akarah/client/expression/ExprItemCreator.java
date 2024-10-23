package dev.akarah.client.expression;

import com.mojang.brigadier.StringReader;
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
        var dataType = "num";
        var fancyType = "Number";
        var reparser = new ExpressionParser();
        reparser.reader = new StringReader(rawExpr);
        var expr = reparser.parseExpression();

        System.out.println(expr);

        if(expr.hasStringChild()) {
            dataType = "str";
            fancyType = "String";
        }

        if(expr.hasComponentChild()) {
            dataType = "comp";
            fancyType = "Text";
        }


        var customValues = new CompoundTag();
        customValues.put("hypercube:varitem", StringTag.valueOf("""
            {"id":"<TYPE>","data":{"name":"<NAME>"}}
            """.trim().replace("<NAME>", data)
                    .replace("<TYPE>", dataType)));
        var compound = new CompoundTag();
        compound.put("PublicBukkitValues", customValues);

        item.set(DataComponents.CUSTOM_DATA, CustomData.of(compound));
        item.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(5000));
        item.set(
            DataComponents.ITEM_NAME,
            Component.literal(
                rawExpr.trim().replace("\0", "")
            ).withStyle(ChatFormatting.AQUA)
        );
        item.set(
            DataComponents.LORE,
            ItemLore.EMPTY
                .withLineAdded(Component.literal("Item Type: " + fancyType).withStyle(ChatFormatting.GRAY)
        ));
        return item;
    }
}
