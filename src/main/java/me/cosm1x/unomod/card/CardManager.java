package me.cosm1x.unomod.card;

import java.util.List;

import me.cosm1x.unomod.enums.CardColor;
import me.cosm1x.unomod.enums.CardValue;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.PlayerStorage;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.ScoreboardCriterion.RenderType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;

public class CardManager {

    public CardManager() {}

    public void giveCard(ServerPlayerEntity player, int count, boolean startup) {
        for (int i = 0; i < count; i++) {
            this.giveCard(player, startup);
        }
    }

    private void giveCard(ServerPlayerEntity player, boolean startup) {
        int color = player.getRandom().nextBetween(1, 5);
        int value = player.getRandom().nextBetween(color == 5 ? 12 : 0, color == 5 ? 13 : 12);
        CardColor cardColor = CardColor.valueOf(color).get();
        CardValue cardValue = CardValue.valueOf(value).get();
        Card card = new Card(cardColor, cardValue);
        player.giveItemStack(card.toItemStack());
        ServerWorld world = player.getWorld();
        if (!startup) {
            ScoreboardPlayerScore score = world.getScoreboard().getPlayerScore(player.getEntityName(), world.getScoreboard().getObjective("table"+GenericUtils.getTableByPlayer(player).getId()));
            score.incrementScore(1);
        }
    }

    public void setupCardCounters(ServerWorld world, Table table, List<ServerPlayerEntity> players) {
        Scoreboard scoreboard = world.getScoreboard(); 
        ScoreboardObjective objective = scoreboard.addObjective("table" + table.getId(), ScoreboardCriterion.DUMMY, Text.of("table"+table.getId()), RenderType.INTEGER);
        scoreboard.setObjectiveSlot(0, objective);

        for (ServerPlayerEntity player : players) {
            if (player == null) continue;
            ScoreboardPlayerScore playerScore = scoreboard.getPlayerScore(player.getEntityName(), objective);
            playerScore.setScore(6);
        }
    }

    public void onItemUse(ServerPlayerEntity player, ServerWorld world, Hand hand) {
        if (hand != Hand.MAIN_HAND) return;
        if (player.getStackInHand(hand).isEmpty()) return;
        if (!(GenericUtils.isPlayerInGame(player))) return;
        Table table = GenericUtils.getTableByPlayer(player);
        PlayerStorage playerStorage = table.getPlayerStorage();
        if (!(playerStorage.getCurrentPlayer().equals(player))) return;
        Game game = table.getGame();
        ItemStack cardItem = player.getStackInHand(hand);
        NbtCompound cardNbt = cardItem.getNbt();
        int customModelData = cardNbt.getInt("CustomModelData");
        int cardColor = Integer.parseInt("" + ("" + customModelData).charAt(0));
        int cardValue = customModelData % 100;

        Card topCard = game.getTopCard();
        Card card = new Card(CardColor.valueOf(cardColor).get(), CardValue.valueOf(cardValue).get());
        
        if (card.getColor().equals(CardColor.WILD)) {
            card.doAction(table, game, player, hand, playerStorage);
        } else if (topCard.getValue().equals(card.getValue())) {
            card.doAction(table, game, player, hand, playerStorage);
            playerStorage.turn();
        } else if (topCard.getColor().equals(card.getColor())) {
            card.doAction(table, game, player, hand, playerStorage);
            playerStorage.turn();
        } else if (player.getScoreboardTags().contains("unomod_wild")) {
            card.doAction(table, game, player, hand, playerStorage);
        } else {
            checkForSpecialCard(topCard, card, table, game, player, hand, playerStorage, false);
            playerStorage.turn();
        }
    }

    protected static boolean checkForSpecialCard(Card topCard, Card card, Table table, Game game, ServerPlayerEntity player, Hand hand, PlayerStorage playerStorage, boolean fromCard) {
        switch(topCard.getValue()) {
            case REDCHANGE:
            case REDDRAW:
                if (card.getColor() == CardColor.RED) {
                    game.setNextTopCard(card);
                    if (!(fromCard)) {
                        card.doAction(table, game, player, hand, playerStorage);
                        return true;
                    }
                }
                break;
            case BLUECHANGE:
            case BLUEDRAW:
                if (card.getColor() == CardColor.BLUE) {
                    game.setNextTopCard(card);
                    if (!(fromCard)) {
                        card.doAction(table, game, player, hand, playerStorage);
                        return true;
                    }
                }
                break;
            case YELLOWCHANGE:
            case YELLOWDRAW:
                if (card.getColor() == CardColor.YELLOW) {
                    game.setNextTopCard(card);
                    if (!(fromCard)) {
                        card.doAction(table, game, player, hand, playerStorage);
                        return true;
                    }
                }
                break;
            case GREENCHANGE:
            case GREENDRAW:
                if (card.getColor() == CardColor.GREEN) {
                    game.setNextTopCard(card);
                    if (!(fromCard)) {
                        card.doAction(table, game, player, hand, playerStorage);
                        return true;
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void setupTopCardEntity(ServerWorld world, Table table) {
        Game game = table.getGame();
        BlockPos center = table.getCenter();
        AreaEffectCloudEntity entity = EntityType.AREA_EFFECT_CLOUD.create(world, null, null, center, SpawnReason.COMMAND, false, false);
        int color = world.getRandom().nextBetween(1, 4);
        int value = world.getRandom().nextBetween(0, 12);
        entity.setCustomName(Text.literal(" " + CardValue.valueOf(value).get().getSymbol()).setStyle(Style.EMPTY.withFont(new Identifier("unomod", CardColor.valueOf(color).get().getName()))));
        entity.setCustomNameVisible(true);
        entity.refreshPositionAndAngles(center.offset(Axis.Y, 2), 0, 0);
        entity.setRadius(0);
        entity.setDuration(999999);
        entity.setWaitTime(-1);
        game.setTopCard(new Card(CardColor.valueOf(color).get(), CardValue.valueOf(value).get()));
        game.setNextTopCard(new Card(CardColor.valueOf(color).get(), CardValue.valueOf(value).get()));
        game.setCardEntity(entity);
        world.spawnEntity(entity);
    }
}   