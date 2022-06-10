package io.github.mclovelock.lovelock.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.mclovelock.lovelock.Lovelock;
import io.github.mclovelock.lovelock.common.container.WarmingCabinetContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WarmingCabinetScreen extends AbstractContainerScreen<WarmingCabinetContainer> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Lovelock.MODID,
			"textures/gui/warming_cabinet.png");

	public WarmingCabinetScreen(WarmingCabinetContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected void renderBg(PoseStack stack, float mouseX, int mouseY, int partialTicks) {
		renderBackground(stack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		super.render(stack, mouseX, mouseY, partialTicks);
		font.draw(stack, title, leftPos + 8, topPos + 5, 0x404040);
		font.draw(stack, playerInventoryTitle, leftPos + 8, topPos + 73, 0x404040);
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
	}

}
