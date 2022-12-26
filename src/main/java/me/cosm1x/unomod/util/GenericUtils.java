package me.cosm1x.unomod.util;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import me.cosm1x.unomod.access.EntityMixinAccess;
import me.cosm1x.unomod.config.UnoModConfig;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.GameManager;
import me.cosm1x.unomod.game.GameState;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class GenericUtils {

    private static final Predicate<ItemFrameEntity> ITEM_FRAME_PREDICATE = entity -> {
        return entity.getType() == EntityType.ITEM_FRAME && 
        entity.getHorizontalFacing() == Direction.UP && 
        entity.world.getBlockState(entity.getDecorationBlockPos()).getBlock() == Blocks.SMOOTH_STONE &&
        entity.getHeldItemStack().getItem() == Items.COPPER_INGOT;
    };

    private static final Predicate<ItemFrameEntity> BIG_TABLE_PREDICATE = entity -> {
        BlockPos attachmentPos = entity.getDecorationBlockPos();
        World world = entity.world;
        return (
            // Checks for valid blocks(default - cobblestone slabs in TOP position)

            // Check for valid block in +X direction
            world.getBlockState(attachmentPos.offset(Axis.X, 2)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.X, 2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            // Check for valid block in -X direction
            world.getBlockState(attachmentPos.offset(Axis.X, -2)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.X, -2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            // Check for valid block in +Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, 2)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            // Check for valid block in -Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, -2)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&

            // Checks for valid seats(default - birch stairs directed to center block of table)

            // Check for valid block in +X direction
            world.getBlockState(attachmentPos.offset(Axis.X, 4)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.X, 4)).get(Properties.FACING).equals(Direction.EAST) &&
            // Check for valid block in -X direction
            world.getBlockState(attachmentPos.offset(Axis.X, -4)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.X, -4)).get(Properties.FACING).equals(Direction.WEST) &&
            // Check for valid block in +Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, 4)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 4)).get(Properties.FACING).equals(Direction.SOUTH) &&
            // Check for valid block in -Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, -4)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -4)).get(Properties.FACING).equals(Direction.NORTH)

        );
    };

    private static final Predicate<ItemFrameEntity> SMALL_TABLE_PREDICATE = entity -> {
        BlockPos attachmentPos = entity.getDecorationBlockPos();
        World world = entity.world;

        return (
            // Checks for valid blocks(default - cobblestone slabs in TOP position)

            // Check for valid block in +X direction
            ((world.getBlockState(attachmentPos.offset(Axis.X, 1)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.X, 1)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            // Check for valid block in -X direction
            world.getBlockState(attachmentPos.offset(Axis.X, -1)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.X, -1)).get(Properties.SLAB_TYPE).equals(SlabType.TOP)) ||
            // Check for valid block in +Z direction
            (world.getBlockState(attachmentPos.offset(Axis.Z, 1)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 1)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            // Check for valid block in -Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, -1)).getBlock() == Blocks.COBBLESTONE_SLAB &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -1)).get(Properties.SLAB_TYPE).equals(SlabType.TOP))) &&

            // Checks for valid seats(default - birch stairs directed to center block of table)

            // Check for valid block in +X direction
            ((world.getBlockState(attachmentPos.offset(Axis.X, 3)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.X, 3)).get(Properties.FACING).equals(Direction.EAST) &&
            // Check for valid block in -X direction
            world.getBlockState(attachmentPos.offset(Axis.X, -3)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.X, -3)).get(Properties.FACING).equals(Direction.WEST)) ||
            // Check for valid block in +Z direction
            (world.getBlockState(attachmentPos.offset(Axis.Z, 3)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 3)).get(Properties.FACING).equals(Direction.SOUTH) &&
            // Check for valid block in -Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, -3)).getBlock() == Blocks.BIRCH_STAIRS &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -3)).get(Properties.FACING).equals(Direction.NORTH)))

        );
    };

    private static UnoModConfig config;

    private static GameManager ugameManager = new GameManager();

    private static final TypeFilter<Entity, ?> filter = new TypeFilter<Entity, Entity>(){
        @Override
        public Entity downcast(Entity entity) {
            return entity;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    };

    public static void onRightClick(Entity entity, ServerPlayerEntity player) {
        player.kill();
    }

    public static List<? extends Entity> getWorldEntities(ServerWorld world) {
        return world.getEntitiesByType(filter, Entity::isAlive);
    }

    public static List<? extends Entity> getMarkedEntities(ServerWorld world) {
        return world.getEntitiesByType(filter, entity -> ((EntityMixinAccess) entity).unomod$hasOwnerUuid());
    }
    
    
    // public static void onReloadCommand(CommandContext<ServerCommandSource> context) {
    //     List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
    //     List<? extends Entity> entities = getMarkedEntities(context.getSource().getWorld());
    //     if (config.getSpectatorsBoolean() == true) {
    //         for (ServerPlayerEntity pl : players) {
    //             for (Entity entity : entities) {
    //                 pl.networkHandler.sendPacket(new EntitiesDestroyS2CPacket(entity.getId()));
    //             }
    //         }

    //     } else {

    //     }
    // }
    
    private static void initConfig() {
        config = AutoConfig.getConfigHolder(UnoModConfig.class).getConfig();
    }

    public static void reloadConfig() {
        AutoConfig.getConfigHolder(UnoModConfig.class).load();
        initConfig();
    }
    
    public static UnoModConfig getConfig() {
        return GenericUtils.config;
    }

    public static GameManager getGameManager() {
        return ugameManager;
    }

    public static int getMaxGameId(GameManager gameManager) {
        List<Game> games = gameManager.getGames();
        try {
            Game lastGame = games.get(games.size() - 1);
            return lastGame.getId() + 1;
        } catch (Exception e) {}
        
        return 1;
    }

    @Nullable
    public static Game getFirstWaitingGame(List<Game> games) {
        for (Game game : games) {
            if (game.getGameState() == GameState.WAITING) {
                return game;
            }
        }
        return null;
    } 

    public static boolean itemFrameTest(ItemFrameEntity entity) {
        return ITEM_FRAME_PREDICATE.test(entity);
    }

    public static boolean bigTableTest(ItemFrameEntity entity) {
        return BIG_TABLE_PREDICATE.test(entity);
    }

    public static boolean smallTableTest(ItemFrameEntity entity) {
        return SMALL_TABLE_PREDICATE.test(entity);
    }
}
