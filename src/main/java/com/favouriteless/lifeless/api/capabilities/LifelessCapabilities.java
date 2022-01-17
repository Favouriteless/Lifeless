package com.favouriteless.lifeless.api.capabilities;

import com.favouriteless.lifeless.Lifeless;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Lifeless.MOD_ID, bus=Bus.MOD)
public class LifelessCapabilities {

	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IPlayerLifeCapability.class);
	}

}
