package me.cosm1x.unomod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import me.cosm1x.unomod.access.EntityMixinAccess;
import me.cosm1x.unomod.card.Card;
import me.cosm1x.unomod.config.UnoModConfig;
import me.cosm1x.unomod.enums.CardColor;
import me.cosm1x.unomod.enums.CardValue;
import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.managers.TableManager;
import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.block.Blocks;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class GenericUtils {

    public static EntityPredicate lookingAt;

    public static void setupPredicate() {
        NbtCompound nbtCompound = new NbtCompound();
        NbtCompound lnbt = new NbtCompound();
        lnbt.putBoolean("ingame", true);
        nbtCompound.put("unomod", lnbt);
        lookingAt = EntityPredicate.Builder.create().typeSpecific(
            PlayerPredicate.Builder.create().lookingAt(
                    EntityPredicate.Builder.create().type(EntityType.PLAYER).nbt(
                        new NbtPredicate(nbtCompound)).build()).build()).build();
    }

    public static Card[] ingameCards = {
        new Card(CardColor.WILD, CardValue.REDCHANGE), new Card(CardColor.WILD, CardValue.BLUECHANGE), 
        new Card(CardColor.WILD, CardValue.GREENCHANGE), new Card(CardColor.WILD, CardValue.YELLOWCHANGE),

        new Card(CardColor.WILD, CardValue.REDDRAW), new Card(CardColor.WILD, CardValue.BLUEDRAW), 
        new Card(CardColor.WILD, CardValue.GREENDRAW), new Card(CardColor.WILD, CardValue.YELLOWDRAW)
    };

    private static final Predicate<ItemFrameEntity> ITEM_FRAME_PREDICATE = entity -> {
        UnoModConfig config = GenericUtils.getConfig();
        BlockPos attachmentPos = entity.getDecorationBlockPos().offset(Axis.Y, -1);

        return entity.getType().equals(EntityType.ITEM_FRAME) && 
        entity.getHorizontalFacing().equals(Direction.UP) && 
        entity.world.getBlockState(attachmentPos).getBlock().equals(config.getTableCenterBlock()) &&
        entity.getHeldItemStack().getItem().equals(config.getItemFrameItem());
    };

    public static final Predicate<ItemStack> WILD_CHANGE = stack -> {
        for (int i = 0; i<4; i++) {
            if (ItemStack.areEqual(ingameCards[i].toItemStack(), stack)) {
                return true;
            }
        }
        return false;
    };

    public static final Predicate<ItemStack> WILD_DRAW = stack -> {
        for (int i = 4; i<8; i++) {
            if (ItemStack.areEqual(ingameCards[i].toItemStack(), stack)) {
                return true;
            }
        }
        return false;
    };

    public static final Predicate<ItemStack> UNO_BUTTON = stack -> {
        if (stack.getNbt() != null) {
            if (stack.getNbt().getInt("CustomModelData") == 6000) {
                return true;
            }
        }
        return false;
    };

    public static final Predicate<ItemStack> IS_A_CARD = stack -> {
        NbtCompound nbt = stack.getNbt();
        if (stack.getItem().equals(Items.STRUCTURE_BLOCK)) {
            int customModelData = nbt.getInt("CustomModelData");
            if (customModelData != 0 && customModelData >= 1000 && customModelData <= 6000) {
                return true;
            }
        }
        return false;
    };

    private static final Predicate<ItemFrameEntity> BIG_TABLE_PREDICATE = entity -> {
        BlockPos attachmentPos = entity.getDecorationBlockPos().offset(Axis.Y, -1);
        World world = entity.world;
        UnoModConfig config = GenericUtils.getConfig();
        return (
            // Checks for valid blocks(default - cobblestone slabs in TOP position)

            // Check for valid block in +X direction
            world.getBlockState(attachmentPos.offset(Axis.X, 2)).getBlock().equals(config.getSlabBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.X, 2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            world.getBlockState(attachmentPos.offset(Axis.X, 2).offset(Axis.Y, 1)).getBlock().equals(Blocks.AIR) &&
            // Check for valid block in -X direction
            world.getBlockState(attachmentPos.offset(Axis.X, -2)).getBlock().equals(config.getSlabBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.X, -2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            world.getBlockState(attachmentPos.offset(Axis.X, -2).offset(Axis.Y, 1)).getBlock().equals(Blocks.AIR) &&
            // Check for valid block in +Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, 2)).getBlock().equals(config.getSlabBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 2).offset(Axis.Y, 1)).getBlock().equals(Blocks.AIR) &&
            // Check for valid block in -Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, -2)).getBlock().equals(config.getSlabBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -2)).get(Properties.SLAB_TYPE).equals(SlabType.TOP) &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -2).offset(Axis.Y, 1)).getBlock().equals(Blocks.AIR) &&

            // Checks for valid seats(default - birch stairs directed to center block of table)

            // Check for valid block in +X direction
            world.getBlockState(attachmentPos.offset(Axis.X, 4)).getBlock().equals(config.getSeatBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.X, 4)).get(Properties.HORIZONTAL_FACING).equals(Direction.EAST) &&
            // Check for valid block in -X direction
            world.getBlockState(attachmentPos.offset(Axis.X, -4)).getBlock().equals(config.getSeatBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.X, -4)).get(Properties.HORIZONTAL_FACING).equals(Direction.WEST) &&
            // Check for valid block in +Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, 4)).getBlock().equals(config.getSeatBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.Z, 4)).get(Properties.HORIZONTAL_FACING).equals(Direction.SOUTH) &&
            // Check for valid block in -Z direction
            world.getBlockState(attachmentPos.offset(Axis.Z, -4)).getBlock().equals(config.getSeatBlock()) &&
            world.getBlockState(attachmentPos.offset(Axis.Z, -4)).get(Properties.HORIZONTAL_FACING).equals(Direction.NORTH)
        );
    };

    private static final Predicate<ItemFrameEntity> EXISTING_TABLE_TEST = entity -> {
        for (Table table : Managers.getTableManager().getTables()) {
            if (table.getItemFrame().equals(entity)) {
                return true;
            }
        }
        return false;
    };

    private static final Predicate<ServerPlayerEntity> IS_PLAYER_IN_GAME = player -> {
        return GenericUtils.getTableByPlayer(player) == null ? false : true;
    };

    private static UnoModConfig config;

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

    @Nullable
    public static Table getTableByPlayer(ServerPlayerEntity player) {
        for (Table table : Managers.getTableManager().getTables()) {
            for (ServerPlayerEntity lplayer : table.getPlayerStorage().getPlayers()) {
                if (player.equals(lplayer)) {
                    return table;
                }
            }
        } 
        return null;
    }


    public static List<? extends Entity> getWorldEntities(ServerWorld world) {
        return world.getEntitiesByType(filter, Entity::isAlive);
    }

    public static List<? extends Entity> getMarkedEntities(ServerWorld world) {
        return world.getEntitiesByType(filter, entity -> ((EntityMixinAccess) entity).unomod$hasOwnerUuid());
    }

    @Nullable
    public static List<? extends Entity> getEntitiesWithTag(ServerWorld world, String tag) {
        List<? extends Entity> entities = getWorldEntities(world);
        List<Entity> returnEntities = new ArrayList<>();
        for (Entity entity : entities) {
            for (String ltag : entity.getScoreboardTags()) {
                if (ltag.equals(tag)) {
                    returnEntities.add(entity);
                }
            }
        }
        if (!(returnEntities.isEmpty())) {
            return returnEntities;
        }
        return null;
    }
    
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

    // public static int getMaxGameId(GameManager gameManager) {
    //     List<Game> games = gameManager.getGames();
    //     try {
    //         Game lastGame = games.get(games.size() - 1);
    //         return lastGame.getId() + 1;
    //     } catch (Exception e) {}
        
    //     return 1;
    // }

    public static int getMaxTableId(TableManager tableManager) {
        List<Table> tables = tableManager.getTables();
        try {
            Table lastTable = tables.get(tables.size() - 1);
            return lastTable.getId() + 1;
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

    public static boolean existingTableTest(ItemFrameEntity entity) {
        return EXISTING_TABLE_TEST.test(entity);
    }

    public static boolean isPlayerInGame(ServerPlayerEntity player) {
        return IS_PLAYER_IN_GAME.test(player);
    }

    public static void setNbt(Entity entity, NbtCompound nbt) {
        NbtCompound entityNbt = NbtPredicate.entityToNbt(entity);
        NbtCompound newNbt = entityNbt.copyFrom(nbt);
        UUID uuid = entity.getUuid();
        entity.readNbt(newNbt);
        entity.setUuid(uuid);
    }

    public static ItemStack getItemFrameStack() {
        NbtCompound itemNbt = new NbtCompound();
        itemNbt.putString("id", "minecraft:item_frame");
        itemNbt.putByte("Count", (byte)1);
        return ItemStack.fromNbt(itemNbt);
    }

    @Nullable
    public static Table findTableById(int id) {
        for (Table table : Managers.getTableManager().getTables()) {
            if (table.getId() == id) {
                return table;
            }
        }
        
        return null;
    }

    public static Table findTableByGame(Game game) {
        for (Table table : Managers.getTableManager().getTables()) {
            if (table.getGame().equals(game)) {
                return table;
            }
        }
        
        return null;
    }

    public static int getNextIndex(List<?> list, int index, int offset) {
        return list.size() == index+1 ? 0+offset : (index+1+offset == list.size() ? (list.size() > index+1+offset ? 0+offset : 0) : index+1+offset);
    }

    public static int getPreviousIndex(List<?> list, int index, int offset) {
        return index-1 < 0 ? list.size()-1-offset : (index-1-offset < 0 ? list.size()-offset : index-1-offset);
    }

    public static int getNextIndex(List<?> list, int index) {
        return getNextIndex(list, index, 0);
    }

    public static int getPreviousIndex(List<?> list, int index) {
        return getPreviousIndex(list, index, 0);
    }
}
