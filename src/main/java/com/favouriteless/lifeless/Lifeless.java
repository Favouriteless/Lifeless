package com.favouriteless.lifeless;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Lifeless.MOD_ID)
public class Lifeless {

    public static boolean DISABLE_HARDCORE = false;
    public static final String MOD_ID = "lifeless";
    public static final Logger LOGGER = LogManager.getLogger();

    public Lifeless() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LifelessConfig.SPEC, "lifeless-common.toml");
        MinecraftForge.EVENT_BUS.register(this);
    }
}
