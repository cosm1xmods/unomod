package me.cosm1x.unomod.game;

import java.util.Collections;
import java.util.List;

import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.World;

public class GameManager {
    private List<Game> games;
    
    public void onBigTableForm(ItemFrameEntity entity, World world) {
        List<ServerPlayerEntity> players = Collections.emptyList();
        Game game = new Game(players, GameState.WAITING, GenericUtils.getMaxGameId(this));
        games.add(game);
        this.setupBigTable(entity, world);
    }
    
    private void setupBigTable(ItemFrameEntity entity, World world) {
        BlockPos tableCenter = entity.getDecorationBlockPos();
        BlockPos poseNegZ = tableCenter.offset(Axis.Z, -4);
        BlockPos posePosZ = tableCenter.offset(Axis.Z, 4);
        BlockPos poseNegX = tableCenter.offset(Axis.X, -4);
        BlockPos posePosX = tableCenter.offset(Axis.X, 4);
        BlockPos[] poses = {poseNegZ, posePosZ, poseNegX, posePosX};
        for (int i = 0; i < 4; i++) {
            Entity lentity = EntityType.PIG.create((ServerWorld)world, null, null, tableCenter, SpawnReason.COMMAND, false, false);
            BlockPos pose = poses[i];
            switch (i) {
                case 0:
                    pose.add(tableCenter);
                    lentity.refreshPositionAndAngles(pose, 0, 0);
                case 1:
                    pose.add(tableCenter);
                    lentity.refreshPositionAndAngles(pose, 180, 0);
                case 2:
                    pose.add(tableCenter);
                    lentity.refreshPositionAndAngles(pose, 90, 0);
                case 3:
                    pose.add(tableCenter);
                    lentity.refreshPositionAndAngles(pose, -90, 0);
            }

            
            world.spawnEntity(lentity);
        }
        BlockPos buttonNegZ = tableCenter.offset(Axis.Z, -2);
        BlockPos buttonPosZ = tableCenter.offset(Axis.Z, 2);
        BlockPos buttonNegX = tableCenter.offset(Axis.X, -2);
        BlockPos buttonPosX = tableCenter.offset(Axis.X, 2);
        BlockPos[] buttons = {buttonNegZ, buttonPosZ, buttonNegX, buttonPosX};
        for (int i = 0; i < 4; i++) {
            BlockPos button = buttons[i].offset(Axis.Y, 1);
            switch (i) {
                case 0:
                    world.setBlockState(button, Blocks.OAK_BUTTON.getDefaultState().with(Properties.FACING, Direction.NORTH));
                case 1:
                    world.setBlockState(button, Blocks.OAK_BUTTON.getDefaultState().with(Properties.FACING, Direction.SOUTH));
                case 2:
                    world.setBlockState(button, Blocks.OAK_BUTTON.getDefaultState().with(Properties.FACING, Direction.WEST));
                case 3:
                    world.setBlockState(button, Blocks.OAK_BUTTON.getDefaultState().with(Properties.FACING, Direction.EAST));
            }
        }


    }

    public void onSmallTableForm(ItemFrameEntity entity) {
        
    }
    
    public void tickEntity(ServerWorld world) {
        
    }
    
    public void tick(ServerWorld world) {
        if (this.games != null) {
            for (Game game : this.games) {
                if (game.getGameState() == GameState.WAITING) {
                    this.tickWaitingGame(world, game);
                }
                if (game.getGameState() == GameState.PREGAME) {
                    this.tickPreGame(world, game);
                }
                if (game.getGameState() == GameState.PLAY) {
                    this.tickPlay(world, game);
                }
                if (game.getGameState() == GameState.ENDGAME) {
                    this.tickEndGame(world, game);
                }
            }
        }
    }
    
    
    private void tickPreGame(ServerWorld world, Game game) {
        if (game.getPlayers().size() == 4) {
            game.setGameState(GameState.PLAY);
            game.setStartState(true);
        }
    }
    
    private void tickPlay(ServerWorld world, Game game) {
        if (game.getStartState() == true) {
            this.setupGame(world, game);
            this.tickGame(world, game);
        } else {
            this.tickGame(world, game);
        }    
    }
    
    
    
    private void tickEndGame(ServerWorld world, Game game) {
        
    }
    
    private void tickWaitingGame(ServerWorld world, Game game) {
        
    }

    private void setupGame(ServerWorld world, Game game) {
        game.setStartState(false);
        
    }
    
    private void tickGame(ServerWorld world, Game game) {
    
    }

    public GameManager() {}
    
    public List<Game> getGames() {
        return this.games;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    protected NbtList toNbtList(double ... values) {
        NbtList nbtList = new NbtList();
        for (double d : values) {
            nbtList.add(NbtDouble.of(d));
        }
        return nbtList;
    }
}