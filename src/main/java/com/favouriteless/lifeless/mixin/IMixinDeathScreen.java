package com.favouriteless.lifeless.mixin;

import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DeathScreen.class)
public interface IMixinDeathScreen {

	@Accessor
	@Mutable
	void setHardcore(boolean value);

}
