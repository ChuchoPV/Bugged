package com.jpv.Level1.Level1.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GenericButton {
    private ImageButton btnHelp;
    private float x, y;
    private String boton, botonOp;

    public GenericButton(float x, float y, String boton, String botonOp){
        this.x = x;
        this.y = y;
        this.boton = boton;
        this.botonOp = botonOp;
        createButton();
    }

    private void createButton(){
        Texture textBtnHelp = new Texture(boton);
        TextureRegionDrawable trdHelp = new TextureRegionDrawable(new TextureRegion(textBtnHelp));
        //imagenBotonOprimido
        Texture textBtnOprimidoHelp = new Texture(botonOp);
        TextureRegionDrawable trdOpHelp = new TextureRegionDrawable(new TextureRegion(textBtnOprimidoHelp));
        btnHelp = new ImageButton(trdHelp, trdOpHelp);
        btnHelp.setPosition(this.x, this.y);
    }

    public ImageButton button(){
        return btnHelp;
    }

    public void reziseImage(float x, float y){
        
    }

    public void setPlace(float x, float y){
        this.x = x;
        this.y = y;
        createButton();
    }
}
