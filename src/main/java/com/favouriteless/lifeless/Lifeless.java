package com.favouriteless.lifeless;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Lifeless.MOD_ID)
public class Lifeless {

    public static final String MOD_ID = "lifeless";
    public static final Logger LOGGER = LogManager.getLogger();

    public Lifeless() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
