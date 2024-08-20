package com.auroali.sanguinisluxuria.compat.emi;

import com.auroali.sanguinisluxuria.common.blocks.SkillUpgraderBlock;
import com.auroali.sanguinisluxuria.common.recipes.AltarRecipe;
import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

public class AltarEmiRecipe implements EmiRecipe {
    final AltarRecipe recipe;
    List<EmiIngredient> inputs;
    List<EmiStack> outputs;

    public AltarEmiRecipe(AltarRecipe recipe, MinecraftClient client) {
        this.recipe = recipe;
        DefaultedList<EmiIngredient> stacks = DefaultedList.ofSize(8, EmiStack.EMPTY);
        for(int i = 0; i < recipe.getIngredients().size(); i++) {
            stacks.set(i, EmiIngredient.of(recipe.getIngredients().get(i)));
        }
        this.inputs = stacks;
        this.outputs = List.of(EmiStack.of(recipe.getOutput(client.world.getRegistryManager())));;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiCompat.ALTAR_RECIPE_CATEGORY;
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
        return 128;
    }

    @Override
    public int getDisplayHeight() {
        return 100;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        for(int i = 0; i < inputs.size() / 2; i++) {
            int x = 32 + (int) (32 * Math.cos((MathHelper.HALF_PI / 2 + i * 2 * MathHelper.TAU / inputs.size())));
            int y = 40 + (int) (32 * Math.sin((MathHelper.HALF_PI / 2 + i * 2 * MathHelper.TAU / inputs.size())));
            widgets.addSlot(inputs.get(i), x, y);
        }
        for(int i = inputs.size() / 2 - 1; i < inputs.size(); i++) {
            int x = 32 + (int) (32 * Math.cos((i * 2 * MathHelper.TAU / inputs.size())));
            int y = 40 + (int) (32 * Math.sin((i * 2 * MathHelper.TAU / inputs.size())));
            widgets.addSlot(inputs.get(i), x, y);
        }
        widgets.addDrawable(30, 52, 16, 16, (drawContext, mouseX, mouseY, delta) -> {
            MatrixStack stack = drawContext.getMatrices();
            MinecraftClient client = MinecraftClient.getInstance();
            BlockState state = BLBlocks.ALTAR.getDefaultState().with(SkillUpgraderBlock.ACTIVE, true);
            BlockRenderManager blockRenderer = client.getBlockRenderManager();
            BakedModel model = blockRenderer.getModel(state);
            VertexConsumer consumer = drawContext.getVertexConsumers().getBuffer(RenderLayers.getBlockLayer(state));
            stack.push();
            stack.translate(0, 0, 150);
            stack.multiplyPositionMatrix(new Matrix4f().scaling(1.0f, -1.0f, 1.0f));
            stack.scale(16, 16, 16);
            //stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35));
            stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45));
            DiffuseLighting.enableGuiDepthLighting();
            blockRenderer.getModelRenderer().render(stack.peek(), consumer, state, model, 1.0f, 1.0f, 1.0f, 15728880, OverlayTexture.DEFAULT_UV);
            DiffuseLighting.disableGuiDepthLighting();
            RenderSystem.disableDepthTest();
            stack.pop();
        });
        widgets.addFillingArrow(84, 40, recipe.getProcessingTicks() * 50)
                .tooltip(List.of(TooltipComponent.of(Text.translatable("emi.cooking.time", recipe.getProcessingTicks() / 20f).asOrderedText())));
        widgets.addSlot(outputs.get(0), 109, 40).recipeContext(this);
    }
}
