package com.auroali.sanguinisluxuria.compat.emi;

import com.auroali.sanguinisluxuria.common.recipes.BloodCauldronRecipe;
import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CauldronInfusingEmiRecipe implements EmiRecipe {
    final BloodCauldronRecipe recipe;
    List<EmiIngredient> inputs;
    List<EmiStack> outputs;

    public CauldronInfusingEmiRecipe(BloodCauldronRecipe recipe, MinecraftClient client) {
        this.recipe = recipe;
        this.inputs = List.of(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.outputs = List.of(EmiStack.of(recipe.getOutput()));;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiCompat.BLOOD_CAULDRON_RECIPE_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 64;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 3, 6);
        widgets.addDrawable(1, 44, 16, 16, (stack, mouseX, mouseY, delta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            BlockState state = BLBlocks.BLOOD_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, recipe.getCauldronLevel());
            BlockRenderManager blockRenderer = client.getBlockRenderManager();
            BakedModel model = blockRenderer.getModel(state);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            VertexConsumerProvider.Immediate provider = VertexConsumerProvider.immediate(builder);
            VertexConsumer consumer = provider.getBuffer(RenderLayers.getBlockLayer(state));
            stack.push();
            stack.translate(0, 8, 140);
            stack.scale(1, -1, 1);
            stack.scale(16, 16, 16);
            //stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(35));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
            DiffuseLighting.enableGuiDepthLighting();
            blockRenderer.getModelRenderer().render(stack.peek(), consumer, state, model, 1.0f, 1.0f, 1.0f, 15728880, OverlayTexture.DEFAULT_UV);
            DiffuseLighting.disableGuiDepthLighting();
            RenderSystem.disableDepthTest();
            provider.draw();
            stack.pop();
        }).tooltip(List.of(TooltipComponent.of(Text.translatable("gui.sanguinisluxuria.blood_bottle_tooltip", recipe.getCauldronLevel()).asOrderedText())));
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 24, 36);
        widgets.addSlot(outputs.get(0), 47, 36).drawBack(false).recipeContext(this);
    }
}
