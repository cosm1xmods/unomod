package me.cosm1x.unomod.config;

import me.cosm1x.unomod.UnoMod;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

@Config(name = UnoMod.MODID)
public class UnoModConfig implements ConfigData { 
    @Comment("Can players in spectator gamemode see cards. Default: \"false\"")
    private boolean canSpectatorsSeeCards = false;
    
    @Comment("Table will break when it's center broken. Default: \"true\"")
    private boolean tableBreakAction = true;
    
    @Comment("Replace the resource pack entry in server.properties. Default: \"true\"")
    private boolean replaceResourcePack = true;

    @Comment("Namespace of item that will be used for forming the table. Default: \"minecraft:copper_ingot\"")
    private String itemFrameItem = "minecraft:copper_ingot";

    @Comment("Namespace of block that will be used as table center. Default: \"minecraft:smooth_stone\"")
    private String tableCenterBlock = "minecraft:smooth_stone";

    @Comment("Namespace of block that will be used as seat. Default: \"minecraft:birch_stairs\"")
    private String seatBlock = "minecraft:birch_stairs";
    
    @Comment("Namespace of slab that will be used for the rest of table. Default: \"minecraft:cobblestone_slab\"")
    private String slabBlock = "minecraft:cobblestone_slab";

    public boolean replaceResourcePack() {
        return this.replaceResourcePack;
    }
    
    public boolean getSpectatorsBoolean() {
        return this.canSpectatorsSeeCards;
    }
    
    public boolean getTableBreakAction() {
        return this.tableBreakAction;
    }
    
    public Item getItemFrameItem() {
        return Registries.ITEM.get(new Identifier(itemFrameItem));
    }

    public String getItemFrameItemNamespace() {
        return this.itemFrameItem;
    }

    public Block getTableCenterBlock() {
        return Registries.BLOCK.get(new Identifier(tableCenterBlock));
    }

    public String getTableCenterBlockNamespace() {
        return this.tableCenterBlock;
    }
    
    public Block getSeatBlock() {
        return Registries.BLOCK.get(new Identifier(seatBlock));
    }

    public String getSeatBlockNamespace() {
        return this.seatBlock;
    }
    
    public Block getSlabBlock() {
        return Registries.BLOCK.get(new Identifier(slabBlock));
    }

    public String getSlabBlockNamespace() {
        return this.slabBlock;
    }
    
}