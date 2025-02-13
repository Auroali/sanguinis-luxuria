package com.auroali.sanguinisluxuria.common.loot;

import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.BLLootFunctions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class SetBloodLootFunction extends ConditionalLootFunction {
    private final int blood;

    protected SetBloodLootFunction(LootCondition[] conditions, int blood) {
        super(conditions);
        this.blood = blood;
    }

    public static ConditionalLootFunction.Builder<?> builder(int blood) {
        return builder(lootConditions -> new SetBloodLootFunction(lootConditions, blood));
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        return BloodStorageItem.setItemBlood(stack, this.blood);
    }

    @Override
    public LootFunctionType getType() {
        return BLLootFunctions.SET_BLOOD;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<SetBloodLootFunction> {

        @Override
        public SetBloodLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            if (!json.has("blood"))
                throw new JsonParseException("Missing blood field");

            int blood = json.get("blood").getAsInt();
            if (blood < 0)
                throw new JsonParseException("Blood must be non-negative");

            return new SetBloodLootFunction(conditions, blood);
        }

        @Override
        public void toJson(JsonObject jsonObject, SetBloodLootFunction conditionalLootFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, conditionalLootFunction, jsonSerializationContext);
            jsonObject.addProperty("blood", conditionalLootFunction.blood);
        }
    }
}
