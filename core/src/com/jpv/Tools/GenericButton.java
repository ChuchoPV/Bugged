package com.jpv.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GenericButton {
    Texture textBtnHelp;
    TextureRegionDrawable trdHelp;
    Texture textBtnOprimidoHelp;
    TextureRegionDrawable trdOpHelp;
    ImageButton btnHelp;
    float x, y;
    String boton, botonOp;


    public GenericButton(float x, float y, String boton, String botonOp){
        this.x = x;
        this.y = y;
        this.boton = boton;
        this.botonOp = botonOp;
        createButton();
    }

    public void createButton(){
        textBtnHelp = new Texture(boton);
        trdHelp = new TextureRegionDrawable(new TextureRegion(textBtnHelp));
        //imagenBotonOprimido
        textBtnOprimidoHelp = new Texture(botonOp);
        trdOpHelp = new TextureRegionDrawable(new TextureRegion(textBtnOprimidoHelp));
        btnHelp = new ImageButton(trdHelp, trdOpHelp);
        btnHelp.setPosition(this.x, this.y);
    }

    public ImageButton button(){
        return btnHelp;
    }

    public void setPlace(float x, float y){
        this.x = x;
        this.y = y;
        createButton();
    }
}
