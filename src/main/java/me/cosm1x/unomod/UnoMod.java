package me.cosm1x.unomod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.cosm1x.unomod.command.CommandRegistry;
import me.cosm1x.unomod.config.UnoModConfig;
import me.cosm1x.unomod.event.registry.EventsRegistry;
import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class UnoMod implements ModInitializer {
	public static final String MODID = "unomod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
			LOGGER.warn("You are running mod on client");
			LOGGER.warn("Something can go not as expected");
			LOGGER.warn("No support will be providen");
		}
		LOGGER.info("Hello!");
		LOGGER.info("Initializating...");
		CommandRegistry.register();
		EventsRegistry.register();
		AutoConfig.register(UnoModConfig.class, JanksonConfigSerializer::new);
		GenericUtils.setupPredicate();
		GenericUtils.reloadConfig();
		Managers.register();
	}

	public static Identifier initIdentifier(String path) {
		return new Identifier(MODID, path);
	}
}
