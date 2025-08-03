package io.github.yo56789.toywatergun;

import io.github.yo56789.toywatergun.entity.TWGEntities;
import io.github.yo56789.toywatergun.item.TWGItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToyWaterGun implements ModInitializer {
	public static final String MOD_ID = "toywatergun";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialising");
		TWGItems.init();
		TWGEntities.init();
	}
}