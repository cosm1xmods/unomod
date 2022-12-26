package me.cosm1x.unomod.card;

import me.cosm1x.unomod.UnoMod;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AbstractCard {
    private Text name;
    private Identifier texture = new Identifier(UnoMod.MODID, "textures/gui/cards/abstract_card.png");
    
    

    // public AbstractCard(Text name, Identifier texture, int type) {
    //     this.name = name;
    //     this.texture = texture;
    //     this.type = type;
    // }

    public AbstractCard(Text name, Identifier texture) {
        this.name = name;
        this.texture = texture;
    }

    public AbstractCard(Text name) {
        this.name = name;
    }

    public Text getName() {
        return this.name;
    }

    public Identifier getTexture() {
        return this.texture;
    }
 
    public abstract boolean isActiveAtNight();
    public abstract int getCardType();


}