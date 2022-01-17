package com.favouriteless.lifeless.mixin;

import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(DeathScreen.class)
public class MixinDeathScreen {

	@Inject(method = "<init>", at = @At("RETURN") )
	public void constructorReturn(Component pCauseOfDeath, boolean pHardcore, CallbackInfo callback) {
		((IMixinDeathScreen)this).setHardcore(false);
		((IMixinScreen)this).setTitle(new TranslatableComponent("deathScreen.title"));
	}

}
