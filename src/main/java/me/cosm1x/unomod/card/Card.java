package me.cosm1x.unomod.card;

import me.cosm1x.unomod.enums.CardColor;
import me.cosm1x.unomod.enums.CardValue;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.PlayerStorage;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.managers.CardManager;
import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import java.util.List;

import me.cosm1x.unomod.access.ServerPlayerEntityMixinAccess;

public class Card {
    private CardColor color;
    private CardValue value;
    private int customModelData;
    
    public Card(CardColor color, CardValue value) {
        this.color = color;
        this.value = value;
        this.customModelData = (this.color.getid() * 1000) + this.value.getId();
    }
    
    public int getCustomModelData() {
        return this.customModelData;
    }
    
    public void doAction(Table table, Game game, ServerPlayerEntity player, Hand hand, PlayerStorage playerStorage) {
        if (!(player.getScoreboardTags().contains("unomod_wild"))) {
            switch (this.value) {
                case BLOCK:
                    playerStorage.skipPlayer();
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    return;
                case REVERSE:
                    playerStorage.flipDirection();
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    return;
                case DRAW:
                    if (this.color == CardColor.WILD) {
                        this.setupWild(player);
                        game.setNextTopCard(this);
                        this.removeOneCard(player, hand, player.getWorld(), table);
                        return;
                    }
                    playerStorage.skipPlayer();
                    Managers.getCardManager().giveCard(playerStorage.getNextPlayer(), 2, false);
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    return;
                case WILD:
                    this.setupWild(player);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    return;
                default:
                    if (CardManager.checkForSpecialCard(game.getTopCard(), this, table, game, player, hand, playerStorage, true)) return;
                    break;
            }

            switch (this.color) {
                case WILD:
                    if (this.value != CardValue.WILD || this.value != CardValue.DRAW) this.doWildAction(player, game, playerStorage);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    break;
                case RED:
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    break;
                case BLUE:
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    break;
                case GREEN:
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    break;
                case YELLOW:
                    game.setNextTopCard(this);
                    this.removeOneCard(player, hand, player.getWorld(), table);
                    break;
                default:
                    break;  
            }
        } else {
            doWildAction(player, game, playerStorage);
        }
    }

    public void unoButton(Table table, Game game, ServerPlayerEntity player, Hand hand, PlayerStorage playerStorage) {
        if (this.color != CardColor.UNO) return; 
        ServerWorld world = player.getWorld();
        Inventories.remove(player.getInventory(), GenericUtils.UNO_BUTTON, 64, false);
        ScoreboardPlayerScore playerScore = world.getScoreboard().getPlayerScore(player.getEntityName(), world.getScoreboard().getObjective("table"+table.getId()));
        if (playerScore.getScore() == 2) {
            ((ServerPlayerEntityMixinAccess)((ServerPlayerEntity)player)).unomod$toggleUno();
            player.getStackInHand(hand).decrement(1);
        }
        for (ServerPlayerEntity lplayer : table.getPlayerStorage().getPlayers()) {
            if (lplayer.equals((ServerPlayerEntity)player)) continue;
            ScoreboardPlayerScore lplayerScore = world.getScoreboard().getPlayerScore(lplayer.getEntityName(), world.getScoreboard().getObjective("table"+table.getId()));
            if (lplayerScore.getScore() == 1 && (!((ServerPlayerEntityMixinAccess)lplayer).unomod$isUnoPressed())) {
                Managers.getCardManager().giveCard(lplayer, 2, false);
                clearUnoButton(playerStorage.getPlayers());
            }
        }
    }

    public static void clearUnoButton(List<ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            Inventories.remove(player.getInventory(), GenericUtils.UNO_BUTTON, 64, false);
        }
    }

    public static boolean checkForSpecialWild(Card topCard, Card card, Game game) {
        switch (topCard.getValue()) {
            case REDCHANGE:
            case REDDRAW:
                if (card.getColor() == CardColor.RED) {
                    game.setNextTopCard(card);
                }
                return true;
            case BLUECHANGE:
            case BLUEDRAW:
                if (card.getColor() == CardColor.BLUE) {
                    game.setNextTopCard(card);
                }
                return true;
            case GREENCHANGE:
            case GREENDRAW:
                if (card.getColor() == CardColor.GREEN) {
                    game.setNextTopCard(card);
                }
                return true;
            case YELLOWCHANGE:
            case YELLOWDRAW:
                if (card.getColor() == CardColor.YELLOW) {
                    game.setNextTopCard(card);
                }
                return true;
            default:
                break;
        }
        return false;
    }

    private void removeOneCard(ServerPlayerEntity player, Hand hand, ServerWorld world, Table table) {
        ItemStack itemStack = player.getStackInHand(hand);
        itemStack.decrement(1);

        ScoreboardPlayerScore score = world.getScoreboard().getPlayerScore(player.getEntityName(), world.getScoreboard().getObjective("table"+table.getId()));
        score.incrementScore(-1);
    }

    private static boolean checkForWildChange(Card card) {
        return card.getValue() == CardValue.REDCHANGE || card.getValue() == CardValue.BLUECHANGE || card.getValue() == CardValue.GREENCHANGE || card.getValue() == CardValue.YELLOWCHANGE;
    }

    private static boolean checkForWildDraw(Card card) {
        return card.getValue() == CardValue.REDDRAW || card.getValue() == CardValue.BLUEDRAW || card.getValue() == CardValue.GREENDRAW || card.getValue() == CardValue.YELLOWDRAW;
    }

    private void doWildAction(ServerPlayerEntity player, Game game, PlayerStorage playerStorage) {
        if      (checkForWildChange(this)) this.doWildChange(player, game, playerStorage);
        else if (checkForWildDraw(this))   this.doWildDraw(player, game, playerStorage);
    }

    private void doWildDraw(ServerPlayerEntity player, Game game, PlayerStorage playerStorage) {
        switch (this.value) {
            case REDDRAW:
                game.setNextTopCard(GenericUtils.ingameCards[4]);
                break;
            case BLUEDRAW:
                game.setNextTopCard(GenericUtils.ingameCards[5]);
                break;
            case GREENDRAW:
                game.setNextTopCard(GenericUtils.ingameCards[6]);
                break;
            case YELLOWDRAW:
                game.setNextTopCard(GenericUtils.ingameCards[7]);
                break;
            default:
                break;
        }
        Inventories.remove(player.getInventory(), GenericUtils.WILD_DRAW, 64, false);
        player.removeScoreboardTag("unomod_wild");
        Managers.getCardManager().giveCard(playerStorage.getNextPlayer(), 4, false);
        playerStorage.skipPlayer();
        playerStorage.turn();

    }

    private void doWildChange(ServerPlayerEntity player, Game game, PlayerStorage playerStorage) {
        switch (this.value) {
            case REDCHANGE:
                game.setNextTopCard(GenericUtils.ingameCards[0]);
                break;
            case BLUECHANGE:
                game.setNextTopCard(GenericUtils.ingameCards[1]);
                break;
            case GREENCHANGE:
                game.setNextTopCard(GenericUtils.ingameCards[2]);
                break;
            case YELLOWCHANGE:
                game.setNextTopCard(GenericUtils.ingameCards[3]);
                break;
            default:
                break;
        }
        Inventories.remove(player.getInventory(), GenericUtils.WILD_CHANGE, 64, false);
        player.removeScoreboardTag("unomod_wild");
        playerStorage.turn();
    }
    
    private void setupWild(ServerPlayerEntity player) {
        if (this.color == CardColor.WILD && this.value == CardValue.WILD) {
            for (int i = 0; i<4; i++) {
                player.giveItemStack(GenericUtils.ingameCards[i].toItemStack());
            }
        } else if (this.color == CardColor.WILD && this.value == CardValue.DRAW) {
            for (int i = 4; i<8; i++) {
                player.giveItemStack(GenericUtils.ingameCards[i].toItemStack());
            }
        }
        player.addScoreboardTag("unomod_wild");
    }

    public CardColor getColor() {
        return color;
    }
    
    public CardValue getValue() {
        return value;
    }

    public String toString() {
        return "Card[" + this.color + "," + this.value + "]";
    }

    public ItemStack toItemStack() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"text\":\" ");
        stringBuilder.append(this.value.getSymbol());
        stringBuilder.append("\", \"font\":\"unomod:");
        stringBuilder.append(this.color.name().toLowerCase());
        stringBuilder.append("\", \"color\":\"white\", \"italic\":false}");

        NbtCompound itemNbt = new NbtCompound();
        itemNbt.putString("id", "minecraft:structure_block");
        itemNbt.putByte("Count", (byte)1);

        NbtCompound tag = new NbtCompound();
        tag.putInt("CustomModelData", customModelData);

        NbtList lore = new NbtList();
        for (int j = 0; j < 6; j++) {
            lore.add(NbtString.of("{\"text\":\"\"}"));
        }

        NbtCompound display = new NbtCompound();
        display.putString("Name", stringBuilder.toString());
        display.put("Lore", lore);
        
        tag.put("display", display);
        
        itemNbt.put("tag", tag);

        return ItemStack.fromNbt(itemNbt);
    }
}
