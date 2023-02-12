package me.cosm1x.unomod.eventhandler;

import java.util.List;

import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.game.BlockStateStorage;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.PlayerStorage;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.game.TableManager;
import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EventHandlers {

    // UseBlockEvent

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
        System.out.println(player.toString() + " " + world.toString() + " " + hand.toString()); 
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        if (player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;
        Table table = GenericUtils.getTableByPlayer((ServerPlayerEntity)player);
        System.out.println(table);

        if (table != null)
            if (table.getBlockStateStorage().getButtons().contains(world.getBlockState(result.getBlockPos()))) return ActionResult.PASS;
        if (player.getStackInHand(hand).getNbt() != null) {
            if (player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 0 && player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 6000) {
                System.out.println("onItemUse");
                Managers.getCardManager().onItemUse((ServerPlayerEntity)player, (ServerWorld)world, hand);
            }
        }
        return ActionResult.PASS;
    }

    // public static ActionResult onUnoButtonPressBlock(PlayerEntity player, World world, Hand hand, BlockHitResult result) {
    //     System.out.println(player.toString() + " " + world.toString() + " " + hand.toString());
    //     if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
    //     if (player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;
    //     if (!(GenericUtils.isPlayerInGame((ServerPlayerEntity)player))) return ActionResult.PASS;
    //     Table table = GenericUtils.getTableByPlayer((ServerPlayerEntity)player);
    //     if (table.getGame().getGameState() != GameState.INGAME) return ActionResult.PASS;
    //     if (player.getStackInHand(hand).getNbt() != null) 
    //         if (player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 6000 && (!(player.getStackInHand(hand).getItem().equals(Items.STRUCTURE_BLOCK))))
    //             return ActionResult.PASS;
    //     Inventories.remove(player.getInventory(), GenericUtils.UNO_BUTTON, 64, false);
    //     ((ServerPlayerEntityMixinAccess)((ServerPlayerEntity)player)).unomod$toggleUno();
    //     for (ServerPlayerEntity lplayer : table.getPlayerStorage().getPlayers()) {
    //         if (lplayer.equals((ServerPlayerEntity)player)) continue;
    //         ScoreboardPlayerScore lplayerScore = world.getScoreboard().getPlayerScore(lplayer.getEntityName(), world.getScoreboard().getObjective("table"+table.getId()));
    //         if (lplayerScore.getScore() == 1 && (!((ServerPlayerEntityMixinAccess)lplayer).unomod$isUnoPressed())) {
    //             Managers.getCardManager().giveCard(lplayer, 2, false);
    //         }
    //     }
    //     return ActionResult.PASS;
    // }
 
    // UseItemEvent

    public static TypedActionResult<ItemStack> onItemCarduse(PlayerEntity player, World world, Hand hand) {
        System.out.println(player.toString() + " " + world.toString() + " " + hand.toString());
        if (hand != Hand.MAIN_HAND) return TypedActionResult.pass(player.getStackInHand(hand));
        if (player.getStackInHand(hand).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        if (player.getStackInHand(hand).getNbt() != null) {
            if (player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 0) {
                System.out.println("onItemUse");
                Managers.getCardManager().onItemUse((ServerPlayerEntity)player, (ServerWorld)world, hand);
            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    // public static TypedActionResult<ItemStack> onUnoButtonPressItem(PlayerEntity player, World world, Hand hand) {
    //     System.out.println(player.toString() + " " + world.toString() + " " + hand.toString());
    //     if (hand != Hand.MAIN_HAND) return TypedActionResult.pass(player.getStackInHand(hand));
    //     if (player.getStackInHand(hand).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
    //     if (!(GenericUtils.isPlayerInGame((ServerPlayerEntity)player))) return TypedActionResult.pass(player.getStackInHand(hand));
    //     Table table = GenericUtils.getTableByPlayer((ServerPlayerEntity)player);
    //     if (table.getGame().getGameState() != GameState.INGAME) return TypedActionResult.pass(player.getStackInHand(hand));
    //     if (player.getStackInHand(hand).getNbt() != null) 
    //         if (player.getStackInHand(hand).getNbt().getInt("CustomModelData") != 6000 && (!(player.getStackInHand(hand).getItem().equals(Items.STRUCTURE_BLOCK))))
    //             return TypedActionResult.pass(player.getStackInHand(hand));
    //     Inventories.remove(player.getInventory(), GenericUtils.UNO_BUTTON, 64, false);
    //     ((ServerPlayerEntityMixinAccess)((ServerPlayerEntity)player)).unomod$toggleUno();
    //     for (ServerPlayerEntity lplayer : table.getPlayerStorage().getPlayers()) {
    //         if (lplayer.equals((ServerPlayerEntity)player)) continue;
    //         ScoreboardPlayerScore lplayerScore = world.getScoreboard().getPlayerScore(lplayer.getEntityName(), world.getScoreboard().getObjective("table"+table.getId()));
    //         if (lplayerScore.getScore() == 1 && (!((ServerPlayerEntityMixinAccess)lplayer).unomod$isUnoPressed())) {
    //             Managers.getCardManager().giveCard(lplayer, 2, false);
    //         }
    //     }
    //     return TypedActionResult.pass(player.getStackInHand(hand));
    // }
}
