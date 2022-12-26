package me.cosm1x.unomod.config;

import blue.endless.jankson.Comment;
import me.cosm1x.unomod.UnoMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = UnoMod.MODID)
public class UnoModConfig implements ConfigData { 
    @Comment("Can players in spectator gamemode see cards. Default: false")
    private boolean canSpectatorsSeeCards = false;
    
    public boolean getSpectatorsBoolean() {
        return this.canSpectatorsSeeCards;
    }
}