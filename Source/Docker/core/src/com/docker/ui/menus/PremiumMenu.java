package com.docker.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.ui.menus.AbstractMenu.AbstractScrollPane;

public class PremiumMenu extends AbstractMenu {
	private static final String buyPleaseText = "Disable Ads and \nget a special ship by \nbuying us a coffee!";
	private static final String thanksMuchText = "Thank you for\nsupporting us!";
	
	
	private Button buyPremiumButton = new TextButton("Buy Premium!", skin);
	private Label text;
	
	public PremiumMenu(final Docker application) {
		super(application);
		
		buyPremiumButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	application.getInventory().buyPremium();
            	
            	//TODO: Do this as a call back
            	updateMenu(new PremiumMenu(application));
            }
        });
		
		boolean hasPremium = application.getInventory().hasPremium();

		
		text = new Label(hasPremium ? thanksMuchText : buyPleaseText, skin);
		text.setAlignment(Align.center);

		Table premiumTable = new Table();
		premiumTable.add(text).center().padBottom(10f).row();
		if(!hasPremium)
			premiumTable.add(buyPremiumButton).center();
        AbstractScrollPane scrollpane = new AbstractScrollPane(premiumTable);
        table.add(scrollpane);
	}
}
