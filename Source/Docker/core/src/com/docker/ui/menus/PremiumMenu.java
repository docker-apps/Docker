package com.docker.ui.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.docker.Docker;
import com.docker.domain.user.IInventory.IInventoryCallback;

public class PremiumMenu extends AbstractMenu {
	private static final String buyPleaseText = "Disable Ads and \nget a special ship by \nbuying us a coffee!";
	private static final String thanksMuchText = "You already have Premium.\nThank you for supporting us!";
	
	private Button backButton = createBackButton(skin);
	private Button buyPremiumButton = new TextButton("Buy Premium!", skin);
	private Label text;
	
	public PremiumMenu(final Docker application) {
		super(application);
		
		backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                application.returnToLastScreen();
            }
        });
		buyPremiumButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	Docker.getInventory().buyPremium(new IInventoryCallback() {
					@Override
					public void call() {
						application.showAds(true);
						application.returnToMenu();
					}
				});
            }
        });
		
		boolean hasPremium = Docker.getInventory().hasPremium();

		
		text = new Label(hasPremium ? thanksMuchText : buyPleaseText, skin);
		text.setAlignment(Align.center);

		Table premiumTable = new Table();
		premiumTable.add(text).center().padBottom(10f).row();
		if(!hasPremium)
			premiumTable.add(buyPremiumButton).center().padBottom(5f).row();
		premiumTable.add(backButton).width(100).center();
        AbstractScrollPane scrollpane = new AbstractScrollPane(premiumTable);
        table.add(scrollpane);
	}
}
