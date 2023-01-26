package me.cosm1x.unomod.game;

import java.util.ArrayList;
import java.util.List;

import me.cosm1x.unomod.UnoMod;
import me.cosm1x.unomod.config.UnoModConfig;
import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class TableManager {
    private List<Table> tables = new ArrayList<>();
    
    public TableManager() {}
    
    public void tick(MinecraftServer server) {
        for (Table table : this.tables) {
            if (table.getGame().getGameState() != GameState.WAITING) return;
            BlockStateStorage blockStateStorage = table.getBlockStateStorage();
            if (blockStateStorage.isSouthButtonPressed() && blockStateStorage.isEastButtonPressed() && 
                blockStateStorage.isNorthButtonPressed() && blockStateStorage.isWestButtonPressed()) {
                    table.getGame().setGameState(GameState.PREGAME);
                }
        }
    }

    public void onBigTableForm(ItemFrameEntity entity, World world) {
        if (tables.isEmpty()) {
            Game game = new Game(GameState.WAITING);          
            this.setupBigTable(entity, world, GenericUtils.getMaxTableId(this), game);
        } else {
            BlockPos tableCenter = entity.getDecorationBlockPos().offset(Axis.Y, -1);
            for (Table table : this.tables) {
                if (table.getCenter().equals(tableCenter)) {
                    return;
                }
            }
            Game game = new Game(GameState.WAITING);          
            this.setupBigTable(entity, world, GenericUtils.getMaxTableId(this), game);
        }
    }
    
    private void setupBigTable(ItemFrameEntity entity, World world, int id, Game game) {
        BlockStateStorage blockStateStorage = new BlockStateStorage();
        PlayerStorage playerStorage = new PlayerStorage();
        BlockPos tableCenter = entity.getDecorationBlockPos().offset(Axis.Y, -1);
        
        // Seats
        BlockPos poseNegZ = tableCenter.offset(Axis.Z, -4);
        BlockPos posePosZ = tableCenter.offset(Axis.Z, 4);
        BlockPos poseNegX = tableCenter.offset(Axis.X, -4);
        BlockPos posePosX = tableCenter.offset(Axis.X, 4);
        BlockPos[] poses = {poseNegZ, posePosZ, poseNegX, posePosX};
        
        // Buttons
        BlockPos buttonNegZ = tableCenter.offset(Axis.Z, -2);
        BlockPos buttonPosZ = tableCenter.offset(Axis.Z, 2);
        BlockPos buttonNegX = tableCenter.offset(Axis.X, -2);
        BlockPos buttonPosX = tableCenter.offset(Axis.X, 2);
        BlockPos[] buttons = {buttonNegZ, buttonPosZ, buttonNegX, buttonPosX};
        for (int i = 0; i < 4; i++) {
            BlockState lstate;
            PigEntity lentity = EntityType.PIG.create((ServerWorld)world, null, null, tableCenter, SpawnReason.COMMAND, false, false);
            BlockPos pose = poses[i];
            BlockPos button = buttons[i].offset(Axis.Y, 1);
            BlockState buttonState = Blocks.OAK_BUTTON.getDefaultState();
            switch (i) {
                case 0:
                    lstate = buttonState.with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(Properties.WALL_MOUNT_LOCATION, WallMountLocation.FLOOR);
                    lentity.refreshPositionAndAngles(pose.getX()+0.5, pose.getY()-0.35, pose.getZ()+0.6, 0, 0);
                    world.setBlockState(button, lstate);
                    blockStateStorage.setNorthButton(lstate);
                    break;
                case 1:
                    lstate = buttonState.with(Properties.HORIZONTAL_FACING, Direction.SOUTH).with(Properties.WALL_MOUNT_LOCATION, WallMountLocation.FLOOR);
                    lentity.refreshPositionAndAngles(pose.getX()+0.5, pose.getY()-0.35, pose.getZ()+0.4, 180, 0);
                    world.setBlockState(button, lstate);
                    blockStateStorage.setSouthButton(lstate);
                    break;
                case 2:
                    lstate = buttonState.with(Properties.HORIZONTAL_FACING, Direction.WEST).with(Properties.WALL_MOUNT_LOCATION, WallMountLocation.FLOOR);
                    lentity.refreshPositionAndAngles(pose.getX()+0.6, pose.getY()-0.35, pose.getZ()+0.5, -90, 0);
                    world.setBlockState(button, lstate);
                    blockStateStorage.setWestButton(lstate);
                    break;
                case 3:
                    lstate = buttonState.with(Properties.HORIZONTAL_FACING, Direction.EAST).with(Properties.WALL_MOUNT_LOCATION, WallMountLocation.FLOOR);
                    lentity.refreshPositionAndAngles(pose.getX()+0.4, pose.getY()-0.35, pose.getZ()+0.5, 90, 0);
                    world.setBlockState(button, lstate);
                    blockStateStorage.setEastButton(lstate);
                    break;
                }
                
                // Setup Pig Entity
                NbtCompound pigNbt = new NbtCompound();
                pigNbt.putBoolean("Invulnerable", true);
                pigNbt.putBoolean("Saddle", true);  
                pigNbt.putBoolean("NoAI", true);
                GenericUtils.setNbt(lentity, pigNbt);
                lentity.addScoreboardTag("table" + id);
                StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.INVISIBILITY, 2147483647, 255, false, false, false);
                lentity.setStatusEffect(effect, entity);
            world.spawnEntity(lentity);
        }
        blockStateStorage.updateButtons();
        
        // Setup Item Frame
        NbtCompound itemNbt = new NbtCompound();
        itemNbt.putString("id", "minecraft:diamond_block");
        itemNbt.putByte("Count", (byte)1);
        entity.setHeldItemStack(ItemStack.fromNbt(itemNbt));
        
        NbtCompound itemFrameNbt = new NbtCompound();
        itemFrameNbt.putBoolean("Invulnerable", true);
        itemFrameNbt.putBoolean("Invisible", true);
        itemFrameNbt.putBoolean("Fixed", true);
        GenericUtils.setNbt(entity, itemFrameNbt);
        
        Table table = new Table(entity, tableCenter, id, game, playerStorage, blockStateStorage);
        
        
        tables.add(table);
        
    }

    public void onTableBreak(BlockPos pos, World world) {
        UnoModConfig config = GenericUtils.getConfig();
        List<Table> tablesToRemove = new ArrayList<>();
        for (Table table : this.tables) {
            BlockPos tableCenter = table.getCenter();
            if (!(table.getCenter().equals(pos))) {
                continue;
            }

            // Removing seats(pigs)
            List<? extends Entity> entities = GenericUtils.getEntitiesWithTag((ServerWorld)world, "table" + table.getId());
            for (Entity lentity : entities) {
                lentity.discard();
            }
            
            // Removing buttons
            BlockPos buttonNegZ = tableCenter.offset(Axis.Z, -2);
            BlockPos buttonPosZ = tableCenter.offset(Axis.Z, 2);
            BlockPos buttonNegX = tableCenter.offset(Axis.X, -2);
            BlockPos buttonPosX = tableCenter.offset(Axis.X, 2);
            BlockPos[] buttons = {buttonNegZ, buttonPosZ, buttonNegX, buttonPosX};
            for (int i = 0; i<4; i++) {
                BlockPos button = buttons[i].offset(Axis.Y, 1);
                world.setBlockState(button, Blocks.AIR.getDefaultState());
            }

            ItemFrameEntity itemFrame = table.getItemFrame();
            // Setup Item Frame
            NbtCompound itemNbt = new NbtCompound();
            itemNbt.putString("id", config.getItemFrameItemNamespace());
            itemNbt.putByte("Count", (byte)1);
            ItemStack originalItem = ItemStack.fromNbt(itemNbt);
            
            itemFrame.dropStack(originalItem);
            itemFrame.dropStack(GenericUtils.getItemFrameStack());

            itemFrame.discard();
            try {
                table.getGame().getCardEntity().discard();
            } catch (Exception e){
                UnoMod.LOGGER.error("Expection while deleting table... " + e);
            }

            if (world.getScoreboard().getObjective("table" + table.getId()) != null) {
                world.getScoreboard().removeObjective(world.getScoreboard().getObjective("table" + table.getId()));
            }
            
            // Forming list for tables to remove
            tablesToRemove.add(table);
        }

        

        // Removing tables
        this.deleteTables(tablesToRemove);
    }
    
    public List<Table> getTables() {
        return this.tables;
    }

    public void addTable(Table table) {
        this.tables.add(table);
    }

    public void deleteTables(List<Table> table) {
        this.tables.removeAll(table);
    }

        // public void deleteTable(Table table) {
    //     this.tables.remove(table);
    // }

    // public void deleleTable(int id) {
    //     for (Table table : this.tables) {
    //         if (table.getId() == id) {
    //             this.tables.remove(table);
    //             break;
    //         }
    //     }
    // }
}
