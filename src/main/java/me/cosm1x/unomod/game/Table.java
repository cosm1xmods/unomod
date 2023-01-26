package me.cosm1x.unomod.game;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.BlockPos;

public class Table {
    private ItemFrameEntity itemFrame;
    private BlockPos center;
    private int id;
    private Game game;
    private PlayerStorage playerStorage;
    private BlockStateStorage blockStateStorage;
    
    protected Table(ItemFrameEntity itemFrame, BlockPos center, int id, Game game,
        @Nullable PlayerStorage playerStorage, 
        @Nullable BlockStateStorage blockStateStorage)
    {
        
        this.itemFrame = itemFrame;
        this.center = center;
        this.id = id;
        this.game = game;
        this.playerStorage = playerStorage;
        this.blockStateStorage = blockStateStorage;
    }
    
    public BlockPos getCenter() {
        return this.center;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Game getGame() {
        return this.game;
    }
    
    public ItemFrameEntity getItemFrame() {
        return this.itemFrame;
    }
    
    public PlayerStorage getPlayerStorage() {
        return this.playerStorage;
    }

    protected void setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
    }

    public BlockStateStorage getBlockStateStorage() {
        return this.blockStateStorage;
    }

    protected void setBlockStateStorage(BlockStateStorage blockStateStorage) {
        this.blockStateStorage = blockStateStorage;
    }
}