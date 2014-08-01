package org.myname.flixeldemo;

import java.text.MessageFormat;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.myname.flixeldemo.parsing.Level;

import android.graphics.Color;

/**
 * Shows the level information necessary for the user including the time left in
 * the level. If time runs out, the HUD will kill the player.
 * 
 * @author Roger Walker, Tony Greer
 */
public final class Hud extends FlxSprite
{
	/* Format String for HUD */
	private static final String HUD_TXT_FMT = "Time: {0}     Level: {1}     x: {2}  y: {3}";

	private final Text hudText;
	private final String levelName;
	private final BackgroundBlock cigLeft;
	private BackgroundBlock cigMiddle;
	private final BackgroundBlock cigRight;
	private int currentTime;
	
	
	public Hud(String lvlName)
	{
		super((int)(-FlxG.scroll.x), (int)(-FlxG.scroll.y),
				null, false, (int)(FlxG.width), 50, Color.GRAY);
		super.setAlpha(0.4f);
		super.width = FlxG.width;

		this.hudText = new Text((int)(-FlxG.scroll.x), (int)(-FlxG.scroll.y), (int)(FlxG.width));
		this.hudText.setSize(18);
		this.hudText.setColor(Color.RED);

		this.levelName = lvlName;
		
		// Cigarette HUD
		this.cigLeft = new BackgroundBlock((int)this.x + 40,
							(int)this.y + 9, 120 , 32);
		cigLeft.loadGraphic(R.drawable.cig_filt_hud);
		this.cigMiddle = new BackgroundBlock((int)cigLeft.x + (int)cigLeft.width, (int)this.y + 9,
							(Level.timeRemaining > 70 ? 210 : (int)Level.timeRemaining * 3),
							32);
		cigMiddle.loadGraphic(R.drawable.cig_midd_hud);
		this.cigRight = new BackgroundBlock((int)cigMiddle.x + (int)cigMiddle.width,
							(int)this.y + 9, 24, 32);
		cigRight.loadGraphic(R.drawable.cig_end_hud);
		currentTime = (int)Level.timeRemaining;
		
	}

	@Override
	public void update()
	{
		super.update();

		hudText.x =  -FlxG.scroll.x/2 * 2; 
		hudText.y =  -FlxG.scroll.y/2 * 2;
		hudText.width = FlxG.width;
		super.x =  -FlxG.scroll.x/2 * 2;
		super.y =  -FlxG.scroll.y/2 * 2;

		
		
		// Update HUD
		
		if(Level.timeRemaining > 0)
		{
			
			this.hudText.setText(MessageFormat.format(HUD_TXT_FMT,
					(int)(Level.timeRemaining -= FlxG.elapsed), //{0}
					this.levelName,								//{1}
					(int)Level.player.x,						//{2}
					(int)Level.player.y));						//{3}
			
			// Time Remaining Cigarette updates
			cigLeft.x = this.x + 40;
			cigLeft.y = this.y + 9;
			cigMiddle.x  = cigLeft.x + cigLeft.width;
			cigMiddle.y = this.y + 9;
			if (currentTime != (int)Level.timeRemaining){
				cigMiddle.width = (Level.timeRemaining > 70 ? 210 : (int)Level.timeRemaining * 3);
				cigMiddle.loadGraphic(R.drawable.cig_midd_hud);
				currentTime = (int)Level.timeRemaining;
	
			}
			cigRight.x = cigMiddle.x + cigMiddle.width;
			cigRight.y = this.y + 9;

		
		}else if(Level.timeRemaining <= 0 && !Level.player.dead)
		{
			Level.player.kill();
		}

		//-- Update components
		
		this.cigLeft.update();
		this.cigMiddle.update();
		this.cigRight.update();
		this.hudText.update();
	}

	@Override
	public void render()
	{
		//-- Render components
		super.render();
		
		this.cigLeft.render();
		this.cigMiddle.render();
		this.cigRight.render();
		this.hudText.render();
	}
}