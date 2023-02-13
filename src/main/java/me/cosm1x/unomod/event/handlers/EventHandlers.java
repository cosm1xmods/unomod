package me.cosm1x.unomod.event.handlers;

import java.util.List;

import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.game.BlockStateStorage;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.PlayerStorage;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.managers.TableManager;
import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EventHandlers {

    // *************
    // UseBlockEvent
    // *************

    public static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        BlockState lstate = world.getBlockState(result.getBlockPos());
        if (!(lstate.getBlock().equals(Blocks.OAK_BUTTON))) {
            return ActionResult.PASS;
        }
        ServerPlayerEntity lplayer = (ServerPlayerEntity) player;
        TableManager tableManager = Managers.getTableManager();
        for (Table table : tableManager.getTables()) {
            if (table.getGame().getGameState() == GameState.INGAME) continue;
            PlayerStorage playerStorage = table.getPlayerStorage();
            List<ServerPlayerEntity> players = playerStorage.getPlayers();
            if (players.contains(lplayer)) continue;
            BlockStateStorage blockStateStorage = table.getBlockStateStorage();
            for (BlockState state : blockStateStorage.getButtons()) {
                if (state.equals(lstate)) {
                    Direction property = state.get(Properties.HORIZONTAL_FACING);
                    switch (property) {
                        case NORTH:
                            if (!(blockStateStorage.isNorthButtonPressed())) {
                                playerStorage.setNorthPlayer(lplayer);
                                blockStateStorage.pressNorthButton(world, result.getBlockPos());
                            }
                            break;
                        case EAST:
                            if (!(blockStateStorage.isEastButtonPressed())) {
                                playerStorage.setEastPlayer(lplayer);
                                blockStateStorage.pressEastButton(world, result.getBlockPos());
                            }
                            break;
                        case SOUTH:
                            if (!(blockStateStorage.isSouthButtonPressed())) {
                                playerStorage.setSouthPlayer(lplayer);
                                blockStateStorage.pressSouthButton(world, result.getBlockPos());
                            }
                            break;
                        case WEST:
                            if (!(blockStateStorage.isWestButtonPressed())) {
                                playerStorage.setWestPlayer(lplayer);
                                blockStateStorage.pressWestButton(world, result.getBlockPos());
                            }
                            break;
                        default:
                            break;

                    }
                }
            }           
            if (playerStorage.getPlayers().isEmpty()) playerStorage.setCurrentPlayer(lplayer);;
            blockStateStorage.updateButtons();
            playerStorage.updatePlayers();
        }

        return ActionResult.PASS;
    }

    public static ActionResult takeCard(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        ItemStack stack = player.getStackInHand(hand);
        if ((!stack.isEmpty())) return ActionResult.PASS;
        // if (stack.getNbt() != null && stack.getNbt().getInt("CustomModelData") != 0 && stack.getItem().equals(Items.STRUCTURE_BLOCK)) return ActionResult.PASS;
        BlockState lstate = world.getBlockState(result.getBlockPos());
        TableManager tableManager = Managers.getTableManager();
        List<Table> tables = tableManager.getTables();
        if (tables.isEmpty()) return ActionResult.PASS;
        for (Table table : tables) {
            Game game = table.getGame();
            if (game.getGameState() != GameState.INGAME) continue;
            BlockStateStorage blockStateStorage = table.getBlockStateStorage();
            if (!(blockStateStorage.getButtons().contains(lstate))) continue;
            PlayerStorage playerStorage = table.getPlayerStorage();
            if (!(playerStorage.getCurrentPlayer().equals((ServerPlayerEntity)player))) continue;
            switch (lstate.get(Properties.HORIZONTAL_FACING)) {
                case NORTH:
                    if (((ServerPlayerEntity)player).equals(playerStorage.getNorthPlayer())) {
                        Managers.getCardManager().giveCard((ServerPlayerEntity)player, 1, false);
                    }
                    break;
                case SOUTH:
                    if (((ServerPlayerEntity)player).equals(playerStorage.getSouthPlayer())) {
                        Managers.getCardManager().giveCard((ServerPlayerEntity)player, 1, false);
                    }
                    break;
                case WEST:
                    if (((ServerPlayerEntity)player).equals(playerStorage.getWestPlayer())) {
                        Managers.getCardManager().giveCard((ServerPlayerEntity)player, 1, false);
                    }
                    break;
                case EAST:
                    if (((ServerPlayerEntity)player).equals(playerStorage.getEastPlayer())) {
                        Managers.getCardManager().giveCard((ServerPlayerEntity)player, 1, false);
                    }
                    break;
                default:
                    break;
            }
        }

        return ActionResult.PASS;
    }

    public static ActionResult onUseItem(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        if (player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;
        Table table = GenericUtils.getTableByPlayer((ServerPlayerEntity)player);

        if (table != null)
            if (table.getBlockStateStorage().getButtons().contains(world.getBlockState(result.getBlockPos()))) return ActionResult.PASS;
        if (player.getStackInHand(hand).getNbt() != null) {
            if (player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 0 && player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 6000) {
                Managers.getCardManager().onItemUse((ServerPlayerEntity)player, (ServerWorld)world, hand);
            }
        }
        return ActionResult.PASS;
    }

    // ************
    // UseItemEvent
    // ************
    
    public static TypedActionResult<ItemStack> onItemCardUse(PlayerEntity player, World world, Hand hand) {
        if (hand != Hand.MAIN_HAND) return TypedActionResult.pass(player.getStackInHand(hand));
        if (player.getStackInHand(hand).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        if (player.getStackInHand(hand).getNbt() != null) {
            if (player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 0) {
                Managers.getCardManager().onItemUse((ServerPlayerEntity)player, (ServerWorld)world, hand);
            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    // ***************
    // BlockBreakEvent
    // ***************

    public static boolean beforeCenterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        TableManager tableManager = Managers.getTableManager();
        if (tableManager.getTables().isEmpty()) {
            return true;
        }
        boolean bl = GenericUtils.getConfig().getTableBreakAction();
        for (Table table : tableManager.getTables()) {
            if (!(pos.equals(table.getCenter()))) return true;
            if (!(table.getGame().getGameState() == GameState.WAITING)) return false;
            if (!(table.getCenter().equals(pos))) continue;
            if (!bl) return false;
        }
        return true;
    }

    public static void afrerCenterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        TableManager tableManager = Managers.getTableManager();
        if (tableManager.getTables().isEmpty()) {
            return;
        }
        tableManager.onTableBreak(pos, world);
    }

    public static boolean beforeButtonBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        TableManager tableManager = Managers.getTableManager();
        List<Table> tables = tableManager.getTables();
        if (tables.isEmpty()) return true;
        for (Table table : tables) {
            BlockStateStorage stateStorage = table.getBlockStateStorage();
            if (stateStorage.getButtons().contains(state)) {
                return false;
            }
        }
        return true;
    }

}
