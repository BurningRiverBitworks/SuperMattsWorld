package org.myname.flixeldemo.parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.flixel.FlxBlock;
import org.flixel.FlxG;
import org.myname.flixeldemo.BackgroundBlock;
import org.myname.flixeldemo.DeathBlock;
import org.myname.flixeldemo.GameView;
import org.myname.flixeldemo.JumpBlock;
import org.myname.flixeldemo.MovingBlock;
import org.myname.flixeldemo.R;
import org.myname.flixeldemo.Text;

import android.graphics.Color;
import android.util.Log;
import collectables.PowerUp;
import enemies.EnemyType;
import enemies.ShootingEnemy;
import enemies.SmartEnemy;
import flash.geom.Point;

/**
 * Parses Level data from text files. These files are used to create <code>Level</code>
 * objects to run the game.
 * 
 * 3/11/2010 - Tony Greer Implemented jump blocks and added lvl_jump_test
 *  
 * @author Matt's World Design Team
 */
public final class LevelParser
{
	private static final String TAG = "MattsWorldLevelParser";

	/* STATES */
	private static final short STATE_NONE = -1;
	private static final short STATE_LEVEL = 1;
	private static final short STATE_BACKGROUND = 2;
	private static final short STATE_MIDDLEGROUND = 3;
	private static final short STATE_STATIONARY_BLOCK = 4;
	private static final short STATE_MOVING_BLOCK = 5;
	private static final short STATE_HURT_BLOCK = 6;
	private static final short STATE_DEATH_BLOCK = 7;
	private static final short STATE_ENEMY = 8;
	private static final short STATE_LABEL = 9;
	private static final short STATE_JUMP = 10;
	private static final short STATE_POWER_UP = 11;
	private static final short STATE_KENEMY = 12;
	private static final short STATE_MUSIC = 13;
	private static final short STATE_TIME = 14;
	private static final short STATE_TEXT = 15;

	/* TAGS */
	private static final String TAG_LEVEL = "[level]";
	private static final String TAG_BACKGROUND = "[background]";
	private static final String TAG_MIDDLEGROUND = "[middle_ground]";
	private static final String TAG_STATIONARY_BLOCK = "[stationary_block]";
	private static final String TAG_MOVING_BLOCK = "[moving_block]";
	private static final String TAG_HURT_BLOCK = "[hurt_block]";
	private static final String TAG_DEATH_BLOCK = "[death_block]";
	private static final String TAG_ENEMY = "[enemy]";
	private static final String TAG_LABEL = "[label]";
	private static final String TAG_JUMP= "[jump]";
	private static final String TAG_POWER_UP = "[power_up]";
	private static final String TAG_KENEMY = "[kenemy]"; // Don't use.
	private static final String TAG_MUSIC = "[music]";
	private static final String TAG_TIME = "[time]";
	private static final String TAG_TEXT = "[text]";

	/** 
	 * Map for taking text resource names and converting them into the integer address value.
	 * Take not that no sound effects are stored in this map. they should be stored with the objects that use them.
	 */
	public static final Map<String, Integer> KEY_RESOURCE_ADDR;
	
	static
	{		
		/*
		 * TODO - Add all resources that will be referenced as a memory
		 * location in the Droid.
		 */
		final HashMap<String, Integer> temp = new HashMap<String, Integer>(100, 1.1F);

		/* TEXTURES (Tiled) */
		temp.put("fire", R.drawable.fire);
		temp.put("tech_tiles", R.drawable.tech_tiles); // Needs to go away.
		temp.put("spike", R.drawable.spike);
		temp.put("water", R.drawable.water);
		temp.put("water_deep", R.drawable.water_deep);
		temp.put("flame", R.drawable.flame);
		temp.put("sand", R.drawable.sand);
		temp.put("rock", R.drawable.rock);
		temp.put("boulder", R.drawable.boulder);
		temp.put("brick",R.drawable.brick);
		temp.put("invisible", R.drawable.invisible);

		/* CHARACTERS (Tiled) */
		temp.put("matt", R.drawable.matt);
		temp.put("enemy", R.drawable.enemy);
		temp.put("kenemy", R.drawable.enemy);
		temp.put("fish", R.drawable.fish);
		temp.put("beach_goer", R.drawable.beach_goer);
		temp.put("seaweed", R.drawable.seaweed); // -- ha ha seaweed is a character.
		temp.put("hall_runner", R.drawable.hall_runner);
		temp.put("heath", R.drawable.heath);
		temp.put("majoros", R.drawable.majoros);
		temp.put("rat", R.drawable.rat);

		/* LEVELS (Text)*/
		temp.put("lvl_test", R.raw.lvl_test);
		temp.put("lvl_test2", R.raw.lvl_test2);
		temp.put("lvl_test3", R.raw.lvl_test3);
		temp.put("lvl_jump_test", R.raw.lvl_jump_test);
		temp.put("lvl_story_test", R.raw.lvl_story_test);
		temp.put("lvl_story_test2", R.raw.lvl_story_test2);
		temp.put("lvl_story_test3", R.raw.lvl_story_test3);
		temp.put("story_begin_slide1", R.raw.story_begin_slide1);
		temp.put("story_begin_slide2", R.raw.story_begin_slide2);
		temp.put("story_begin_slide3", R.raw.story_begin_slide3);

		/* MUSIC */
		temp.put("death1", R.raw.death1);
		temp.put("death2", R.raw.death2);
		temp.put("level1_music", R.raw.level1_music);
		temp.put("level2_music", R.raw.level2_music);

		/* MIDDLEGROUND */
		temp.put("tiki_bar", R.drawable.tiki_bar);
		temp.put("umbrella", R.drawable.umbrella);
		temp.put("palm_tree", R.drawable.palm_tree);
		temp.put("cloud",R.drawable.cloud);
		temp.put("cloud_large",R.drawable.cloud_large);
		temp.put("locker", R.drawable.lockers);
		temp.put("locker_large", R.drawable.lockers_large);
		temp.put("car", R.drawable.car);
		temp.put("torch", R.drawable.torch);
		temp.put("backpack", R.drawable.backpack);
		temp.put("sofa_fire", R.drawable.sofa_fire); // sofa on fire (death_block)
		temp.put("sofa", R.drawable.sofa_fire);
		temp.put("cream_tiles", R.drawable.cream_tiles);
		temp.put("vending_machine", R.drawable.vending_machine);
		temp.put("warning", R.drawable.warning); // for heath
		temp.put("window", R.drawable.warning); // to outdoors

		/* BACKGROUND */
		temp.put("background_beach", R.drawable.background_beach);

		/* CIG HUD */
		temp.put("cig_end_hud", R.drawable.cig_end_hud);
		temp.put("cig_filt_hud", R.drawable.cig_filt_hud);
		temp.put("cig_midd_hud", R.drawable.cig_midd_hud);

		/* STORY BOARDING (Images and LVL files) */
		temp.put("titlescreen", R.drawable.titlescreen);
		temp.put("story_matt_beach", R.drawable.story_matt_beach);
		temp.put("story_dave_phone", R.drawable.story_dave_phone);
		temp.put("story_office", R.drawable.story_office);
		temp.put("story_test",R.drawable.story_test);
		temp.put("story_test2",R.drawable.story_test2);

		KEY_RESOURCE_ADDR = Collections.unmodifiableMap(temp);
	}

	public LevelParser(Level level)
	{
		String str = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try
		{
			isr = new InputStreamReader(GameView.res.openRawResource(Level.nextLevel), Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			
			int state = STATE_NONE;
			String[] strParts;

			String name, lvl_name, text, enemy_type;
			int xInit, yInit, width, height,
				maxXMovement, maxYMovement, horizSpeed,
				verticSpeed, texture;

			while((str = br.readLine()) != null)
			{
				/*
				 * Comments!
				 */
				if(str.trim().startsWith("#") || "".equals(str.trim()))
					continue;

				/*
				 * Determine if there was a change of state
				 * for this particular line of text.
				 */
				if(str.equalsIgnoreCase(TAG_LEVEL))
				{
					state = STATE_LEVEL;
					continue;
				}else if(str.equalsIgnoreCase(TAG_BACKGROUND))
				{				
					state = STATE_BACKGROUND;
					continue;
				}else if(str.equalsIgnoreCase(TAG_MIDDLEGROUND))
				{				
					state = STATE_MIDDLEGROUND;
					continue;
				}else if(str.equalsIgnoreCase(TAG_STATIONARY_BLOCK))
				{
					state = STATE_STATIONARY_BLOCK;
					continue;
				}else if(str.equalsIgnoreCase(TAG_MOVING_BLOCK))
				{				
					state = STATE_MOVING_BLOCK;					
					continue;
				}else if(str.equalsIgnoreCase(TAG_HURT_BLOCK))
				{
					state = STATE_HURT_BLOCK;
					continue;
				}else if(str.equalsIgnoreCase(TAG_DEATH_BLOCK))
				{
					state = STATE_DEATH_BLOCK;
					continue;
				}else if(str.equalsIgnoreCase(TAG_ENEMY))
				{
					state = STATE_ENEMY;
					continue;
				}else if(str.equalsIgnoreCase(TAG_KENEMY))
				{
					state= STATE_KENEMY;
					continue;
				}else if(str.equalsIgnoreCase(TAG_LABEL))
				{
					state = STATE_LABEL;
					continue;	
				}else if(str.equalsIgnoreCase(TAG_JUMP))
				{
					state = STATE_JUMP;
					continue;
				}else if(str.equalsIgnoreCase(TAG_POWER_UP))
				{
					state = STATE_POWER_UP;
					continue;	
				}else if(str.equalsIgnoreCase(TAG_MUSIC))
				{
					state = STATE_MUSIC;
					continue;
				}else if(str.equalsIgnoreCase(TAG_TEXT))
				{
					state = STATE_TEXT;
					continue;
				}else if(str.equalsIgnoreCase(TAG_TIME))
				{
					state = STATE_TIME;
					continue;
				}

				strParts = str.split("\\s+");

				switch(state)
				{
					case STATE_LEVEL:
						level.name = strParts[0];
						level.width = Integer.parseInt(strParts[1]);
						level.height = Integer.parseInt(strParts[2]);
						level.defaultTexture = KEY_RESOURCE_ADDR.get(strParts[3]);

						// if(strParts.length == 5)
						//		level.background = KEY_RESOURCE_ADDR.get(strParts[4]);

					break;

					case STATE_BACKGROUND:
						xInit = Integer.parseInt(strParts[0]);
						yInit = Integer.parseInt(strParts[1]);
						width = Integer.parseInt(strParts[2]);
						height = Integer.parseInt(strParts[3]);
						texture = KEY_RESOURCE_ADDR.get(strParts[4]);
						level.backgrounds.add(new BackgroundBlock(xInit, yInit, width, height).loadGraphic(texture));

					break;	

					case STATE_MIDDLEGROUND:
						xInit = Integer.parseInt(strParts[0]);
						yInit = Integer.parseInt(strParts[1]);
						width = Integer.parseInt(strParts[2]);
						height = Integer.parseInt(strParts[3]);
						texture = KEY_RESOURCE_ADDR.get(strParts[4]);
						level.middle_grounds.add(new BackgroundBlock(xInit, yInit, width, height).loadGraphic(texture));

					break;

					case STATE_STATIONARY_BLOCK:
						 xInit = Integer.parseInt(strParts[0]);
						 yInit = Integer.parseInt(strParts[1]);
						 width = Integer.parseInt(strParts[2]);
						 height = Integer.parseInt(strParts[3]);
						 texture = level.defaultTexture;

						 if(strParts.length == 5)
							 texture = KEY_RESOURCE_ADDR.get(strParts[4]);

						level.stationaryBlocks.add(new FlxBlock(xInit, yInit, width, height).loadGraphic(texture));

					break;

					case STATE_MOVING_BLOCK:
						/*
						 * Question about oneway... should the thing 
						 * just travel right off the screen without even seeing
						 * it??? WTF
						 */
						 xInit = Integer.parseInt(strParts[0]);
						 yInit = Integer.parseInt(strParts[1]);
						 width = Integer.parseInt(strParts[2]);
						 height = Integer.parseInt(strParts[3]);
						 maxXMovement = Integer.parseInt(strParts[4]);
						 maxYMovement = Integer.parseInt(strParts[5]);
						 horizSpeed = Integer.parseInt(strParts[6]);
						 verticSpeed = Integer.parseInt(strParts[7]);
						 texture = level.defaultTexture;

						if(strParts.length == 9)
								texture = KEY_RESOURCE_ADDR.get(strParts[8]);

						level.movingBlocks.add(new MovingBlock(maxYMovement, verticSpeed, maxXMovement, horizSpeed,
								xInit, yInit, width, height, false).loadGraphic(texture));

					break;

					case STATE_HURT_BLOCK:
						/*
						 * Hurt Blocks cause harm to Matt. The default damage is 1. The max life Matt
						 * Can have is 2 (Super Matt)
						 */
						 xInit = Integer.parseInt(strParts[0]);
						 yInit = Integer.parseInt(strParts[1]);
						 width = Integer.parseInt(strParts[2]);
						 height = Integer.parseInt(strParts[3]);
						 texture = level.defaultTexture;

						 if(strParts.length == 6)
							 texture = KEY_RESOURCE_ADDR.get(strParts[5]);

						level.hurtBlocks.add(new FlxBlock(xInit, yInit, width, height).loadGraphic(texture));

					break;

					case STATE_DEATH_BLOCK:
						 xInit = Integer.parseInt(strParts[0]);
						 yInit = Integer.parseInt(strParts[1]);
						 width = Integer.parseInt(strParts[2]);
						 /*
						  * It is not currently possible to set the height of a deathblock due to
						  * issues with the appearance and animation. We need to come up with a way 
						  * to use height in a future version.
						  */
						 height = Integer.parseInt(strParts[3]);
						 texture = level.defaultTexture;

						if(strParts.length == 5)
							texture = KEY_RESOURCE_ADDR.get(strParts[4]);

						level.deathBlocks.add(new DeathBlock(xInit, yInit, width, texture));

					break;

					case STATE_ENEMY:
						 xInit = Integer.parseInt(strParts[0]);
						 yInit = Integer.parseInt(strParts[1]);
						 enemy_type = strParts[2];

						 level.enemies.add(EnemyType.getInstance(xInit, yInit, enemy_type));

					break;

					case STATE_LABEL:
						name = strParts[0];
						xInit = Integer.parseInt(strParts[1]);
						yInit = Integer.parseInt(strParts[2]);

						level.labels.put(name, new Point(xInit, yInit));

					break;

					case STATE_JUMP:
						/* MANDATORY */
						xInit = Integer.parseInt(strParts[0]);
						yInit = Integer.parseInt(strParts[1]);
						width = Integer.parseInt(strParts[2]);
						height = Integer.parseInt(strParts[3]);
						lvl_name = strParts[4];
						name = strParts[5];
						texture = R.drawable.invisible;

						if(strParts.length > 6) // e.g. has more.
							texture = KEY_RESOURCE_ADDR.get(strParts[6]);

						/*
						 * TODO - this only allows one time jumps... to modify, put code here.
						 */
						level.jump.add(new JumpBlock(xInit, yInit, width, height, lvl_name, name, true).loadGraphic(texture));

					break;

					case STATE_POWER_UP:
						/* MANDATORY */
						xInit = Integer.parseInt(strParts[0]);
						yInit = Integer.parseInt(strParts[1]);
						name = strParts[2];

						level.powerUps.add(PowerUp.getInstance(xInit, yInit, name));
					break;

					case STATE_MUSIC:
						level.music = KEY_RESOURCE_ADDR.get(strParts[0]);

					break;

					case STATE_TIME:
						Level.timeRemaining = Float.parseFloat(strParts[0]);

					break;				

					case STATE_TEXT:
						xInit = Integer.parseInt(strParts[0]);
						yInit = Integer.parseInt(strParts[1]);
						String color = strParts[2];
						int fontColor = Color.BLACK;
						if (color.equalsIgnoreCase("blue")){
							fontColor = Color.BLUE;
						}else if (color.equalsIgnoreCase("green")){
							fontColor = Color.GREEN;
						}else if (color.equalsIgnoreCase("yellow")){
							fontColor = Color.YELLOW;
						}else if (color.equalsIgnoreCase("red")){
							fontColor = Color.RED;
						}else if (color.equalsIgnoreCase("white")){
							fontColor = Color.WHITE;
						}

						int size = Integer.parseInt(strParts[3]);
						text = "";
						for (int i = 4; i < strParts.length; i++)
							text += strParts[i] + " ";

						Text tempText = new Text(xInit, yInit, FlxG.width);
						tempText.setText(text);
						tempText.setSize(size);
						tempText.setColor(fontColor);
						level.texts.add(tempText);

					break;					

					default:
						throw new ParseException("F!", -1);
				}
			}

			/*
			 * CHECK START LABEL
			 */
			if(!level.labels.containsKey(Level.DEFAULT_START_LABEL))
				throw new Exception("Missing start label for Level");

			//Need to add a reference to the player to smart and shooting enemies so that they will respond to the player
			for(int i = 0; i < level.enemies.size(); i++)
			{
				if(level.enemies.get(i).getClass() == SmartEnemy.class)
				{
					((SmartEnemy)level.enemies.get(i)).setPlayer(Level.player);
				}
				else if(level.enemies.get(i).getClass() == ShootingEnemy.class)
				{
					((ShootingEnemy)level.enemies.get(i)).setPlayer(Level.player);
					//need to add the shooting enemy's projectile to the level
					level.enemyMissiles.add(((ShootingEnemy)level.enemies.get(i)).getProjectile());
				}
			}
		}catch(Throwable ioe)
		{
			/*
			 * Break Point Here... if the parser is blowing up.
			 */
			Log.e(TAG, "Problem parsing string: " + str + " " + ioe, ioe);
		}finally
		{
			try{if(br != null) br.close();}catch(Exception sq){}
			try{if(isr != null) isr.close();}catch(Exception sq){}
		}
	}
}