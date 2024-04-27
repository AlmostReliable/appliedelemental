package com.almostreliable.appelem.element;

import appeng.api.client.AEKeyRenderHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;

public class ElementRenderer implements AEKeyRenderHandler<ElementKey> {

    @Override
    public void drawInGui(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, ElementKey key) {
        int elementColor = getElementColor(key);
        guiGraphics.fill(x, y, x + 16, y + 16, elementColor);
    }

    @Override
    public void drawOnBlockFace(
        PoseStack poseStack, MultiBufferSource buffers, ElementKey key, float scale, int combinedLight, Level level
    ) {
        int elementColor = getElementColor(key);

        poseStack.pushPose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        poseStack.translate(0, 0, 0.01);

        scale -= 0.05f;
        var x0 = -scale / 2f;
        var y0 = scale / 2f;
        var x1 = scale / 2f;
        var y1 = -scale / 2f;

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        var matrix = poseStack.last().pose();
        builder.vertex(matrix, x0, y1, 0).color(elementColor).endVertex();
        builder.vertex(matrix, x1, y1, 0).color(elementColor).endVertex();
        builder.vertex(matrix, x1, y0, 0).color(elementColor).endVertex();
        builder.vertex(matrix, x0, y0, 0).color(elementColor).endVertex();

        Tesselator.getInstance().end();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    @Override
    public Component getDisplayName(ElementKey key) {
        return key.computeDisplayName();
    }

    private static int getElementColor(ElementKey key) {
        ElementType elementType = (ElementType) key.getPrimaryKey();
        return elementType.getColor() | 0xFF00_0000; // fill alpha
    }
}
