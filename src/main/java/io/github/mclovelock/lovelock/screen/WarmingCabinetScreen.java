package io.github.mclovelock.lovelock.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.mclovelock.lovelock.Lovelock;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WarmingCabinetScreen extends AbstractContainerScreen<WarmingCabinetMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Lovelock.MODID,
			"textures/gui/warming_cabinet.png");

	public WarmingCabinetScreen(WarmingCabinetMenu pMenu, Inventory pInv, Component pTitle) {
		super(pMenu, pInv, pTitle);

	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
		for (int i = 0; i < 4; i++) {
			if (menu.isCrafting(i)) {
				blit(pPoseStack, x + 102, y + 41, 176, 0, 8, menu.getScaledProgress(i));
			}
		}
	}

	@Override
	public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
		renderBackground(pPoseStack);
		super.render(pPoseStack, mouseX, mouseY, delta);
		renderTooltip(pPoseStack, mouseX, mouseY);
	}
}
