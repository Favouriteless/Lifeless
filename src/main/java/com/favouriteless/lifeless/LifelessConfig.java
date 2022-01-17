package com.favouriteless.lifeless;

import net.minecraftforge.common.ForgeConfigSpec;

public class LifelessConfig {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static ForgeConfigSpec.ConfigValue<Integer> BASE_LIVES;
	public static ForgeConfigSpec.ConfigValue<Integer> RANDOM_LIVES;
	public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TRADING;

	static {
		BASE_LIVES = BUILDER.comment("Number of starting lives").define("base_lives", 3);
		RANDOM_LIVES = BUILDER.comment("Max number of random lives").define("random_lives", 2);
		ENABLE_TRADING = BUILDER.comment("Allow players to trade lives").define("life_trading", true);

		SPEC = BUILDER.build();
	}

}
