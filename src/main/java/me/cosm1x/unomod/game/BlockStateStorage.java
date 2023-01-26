package me.cosm1x.unomod.game;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStateStorage {
    private BlockState southButton;
    private BlockState eastButton;
    private BlockState northButton;
    private BlockState westButton;

    private List<BlockState> buttons = new ArrayList<>();
    private boolean southButtonPressed;
    private boolean eastButtonPressed;
    private boolean northButtonPressed;
    private boolean westButtonPressed;
    
    protected BlockStateStorage() {}
    
    protected BlockStateStorage(BlockState southButton, BlockState eastButton, BlockState northButton, BlockState westButton) {
        this.southButton = southButton;
        this.eastButton = eastButton;
        this.northButton = northButton;
        this.westButton = westButton;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    this.buttons.add(southButton);
                    break;
                case 1:
                    this.buttons.add(eastButton);
                    break;
                case 2:
                    this.buttons.add(northButton);
                    break;
                case 3:
                    this.buttons.add(westButton);
                    break;
            }
        }
    }

    protected BlockState getSouthButton() {
        return this.southButton;
    }

    protected BlockState getEastButton() {
        return this.eastButton;
    }

    protected BlockState getNorthButton() {
        return this.northButton;
    }

    protected BlockState getWestButton() {
        return this.westButton;
    }

    public void pressSouthButton(World world, BlockPos blockPos) {
        this.southButtonPressed = true;
        this.southButton = this.southButton.with(Properties.POWERED, true);
        world.setBlockState(blockPos, this.southButton);
    }

    public void pressEastButton(World world, BlockPos blockPos) {
        this.eastButtonPressed = true;
        this.eastButton = this.eastButton.with(Properties.POWERED, true);
        world.setBlockState(blockPos, this.eastButton);
    }

    public void pressNorthButton(World world, BlockPos blockPos) {
        this.northButtonPressed = true;
        this.northButton = this.northButton.with(Properties.POWERED, true);
        world.setBlockState(blockPos, this.northButton);
    }

    public void pressWestButton(World world, BlockPos blockPos) {
        this.westButtonPressed = true;
        this.westButton = this.westButton.with(Properties.POWERED, true);
        world.setBlockState(blockPos, westButton);
    }

    public boolean isSouthButtonPressed() {
        return this.southButtonPressed;
    }

    public boolean isEastButtonPressed() {
        return this.eastButtonPressed;
    }

    public boolean isNorthButtonPressed() {
        return this.northButtonPressed;
    }

    public boolean isWestButtonPressed() {
        return this.westButtonPressed;
    }
    
    protected void setSouthButton(BlockState southButton) {
        this.southButton = southButton;
    }
    
    protected void setEastButton(BlockState eastButton) {
        this.eastButton = eastButton;
    }
    
    protected void setNorthButton(BlockState northButton) {
        this.northButton = northButton;
    }
    
    protected void setWestButton(BlockState westButton) {
        this.westButton = westButton;
    }

    public List<BlockState> getButtons() {
        return this.buttons;
    }

    public void updateButtons() {
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    this.buttons.add(this.southButton);
                    break;
                case 1:
                    this.buttons.add(this.eastButton);
                    break;
                case 2:
                    this.buttons.add(this.northButton);
                    break;
                case 3:
                    this.buttons.add(this.westButton);
                    break;
            }
        }
    }
}
