package me.cosm1x.unomod.eventhandler;

import java.util.List;

import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.game.BlockStateStorage;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.game.TableManager;
import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBreakEvent {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(BlockBreakEvent::beforeButtonBreak);
        PlayerBlockBreakEvents.BEFORE.register(BlockBreakEvent::beforeCenterBlockBreak);
        PlayerBlockBreakEvents.AFTER.register(BlockBreakEvent::afrerCenterBlockBreak);
    }

    private static boolean beforeCenterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
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

    private static void afrerCenterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
        TableManager tableManager = Managers.getTableManager();
        if (tableManager.getTables().isEmpty()) {
            return;
        }
        tableManager.onTableBreak(pos, world);
    }

    private static boolean beforeButtonBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity entity) {
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
