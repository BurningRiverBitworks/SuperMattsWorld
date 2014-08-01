package org.myname.flixeldemo.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.flixel.FlxBlock;
import org.flixel.FlxCollideListener;
import org.flixel.FlxCore;
import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.myname.flixeldemo.Hud;
import org.myname.flixeldemo.Player;
import org.myname.flixeldemo.R;

import audio.SFXPool;

import collectables.EnergyDrink;
import enemies.Enemy;
import enemies.Projectile;
import flash.geom.Point;

public class Level extends FlxState
{
	public static final String DEFAULT_START_LABEL = "start";
	public static final float DEFAULT_LEVEL_TIME = 60F;

	public static final HashMap<Integer, Level> levelSaves = new HashMap<Integer, Level>();

	/** The next level to be loaded upon SwitchState to this class.*/
	protected static int nextLevel = R.raw.story_begin_slide1;
	/** The current level to already loaded in this class. */
	protected static int currentLevel = R.raw.lvl_jump_test;
	/** The label to start the player at in this class. start if not set. */
	protected static String startLabel;
	/** Only set by LevelParser and incremented with power-ups.*/
	public static float timeRemaining = DEFAULT_LEVEL_TIME;

	/* Only one creation. */	
	public static final Player player = new Player();

	protected int defaultTexture = R.drawable.tech_tiles;
	//protected int background;
	protected String name;
	protected int width, height;
	protected int music = 0;
	protected ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	protected ArrayList<Projectile> enemyMissiles = new ArrayList<Projectile>();
	protected HashMap<String, Point> labels = new HashMap<String, Point>();
	//-- TODO needs a container Object for inner variables.
	protected ArrayList<FlxBlock> jump = new ArrayList<FlxBlock>();
	protected ArrayList<FlxBlock> movingBlocks = new ArrayList<FlxBlock>();
	protected ArrayList<FlxBlock> stationaryBlocks = new ArrayList<FlxBlock>();
	protected ArrayList<FlxBlock> hurtBlocks = new ArrayList<FlxBlock>();
	protected ArrayList<FlxCore> deathBlocks = new ArrayList<FlxCore>();
	protected ArrayList<FlxBlock> powerUps = new ArrayList<FlxBlock>();
	protected ArrayList<FlxCore> backgrounds = new ArrayList<FlxCore>();
	protected ArrayList<FlxCore> middle_grounds = new ArrayList<FlxCore>();
	protected ArrayList<FlxText> texts = new ArrayList<FlxText>();
	protected Hud headsUp;
	
	public Level()
	{
		if (Level.levelSaves.containsKey(nextLevel))
			//-- Copy the contents of the saved level DOOD!
			this.copyContents(Level.levelSaves.get(nextLevel));
		else
			new LevelParser(this);

		this.headsUp = new Hud(this.name);
		this.addComponentsToLayer();
		this.setCameraFollow();

		/*
		 * SET START POINT - Check to see if we provided a new start point for the player. This is
		 * set in the Level.switchLevel function. If the specified point is not
		 * found, then start them at the beginning of the level.s
		 */

		Point startPoint = labels.containsKey(startLabel) ? labels.get(startLabel) : labels.get(DEFAULT_START_LABEL);
		//-- Just in case we died last turn.
		if(player.dead)
		{
			player.reset(startPoint.x, startPoint.y);
		}else
		{
			player.x = startPoint.x;
			player.y = startPoint.y;
		}

		if (music != 0)
			FlxG.playMusic(music, FlxG.getVolume());

		//-- Save?
		currentLevel = LevelParser.KEY_RESOURCE_ADDR.get(this.name);
		levelSaves.put(currentLevel, this);
	}

	/**
	 * 
	 * Sets the nextLevel and startLabel static members.
	 * 
	 * 
	 * PRE: nextLevelName is the name of the next level that is being switched to.
	 * 				This is required, but can be the same name as the current level.
	 * 		nextLevelLabel is the labeled point to with the Player will jump to in the next level.
	 * 				string can be empty, it will default to the start label of the level
	 * 				when level is opened.
	 * 
	 * POST: nextLevel addresses by nextLavelName is loaded as current FlxG.state,
	 * 			 and the player is located at the nextLevelLabel. If the current "level" is a Level,
	 * 				the current state is stored. 
	 * 
	 * @param nextLevelName
	 * @param nextLevelLabel
	 */
	public static void switchLevel(String nextLevelName, String nextLevelLabel)
	{
		nextLevel = LevelParser.KEY_RESOURCE_ADDR.get(nextLevelName);

		// only save Levels, not others such as MenuState
		if (FlxG.state instanceof Level)
			levelSaves.put(LevelParser.KEY_RESOURCE_ADDR.get(((Level)FlxG.state).name), ((Level)FlxG.state));

		//-- need to know where to start when we swap the levels!
		//   if this is not found we will default to "start"
		Level.startLabel = nextLevelLabel;

		FlxG.switchState(Level.class);
	}

	@Override
	public void update()
	{
		super.update();

		/*
		 * PLAYER COLLISIONS
		 */
		FlxG.collideArrayList(stationaryBlocks, player);
		FlxG.collideArrayList(movingBlocks, player);
		FlxG.collideArrayList(hurtBlocks, player);
		FlxG.overlapArrayList(movingBlocks, player, new FlxCollideListener()
		{
			public void Collide(FlxCore object1, FlxCore object2)
			{
				//-- squashed by a moving block!
				SFXPool.playSound(R.raw.squishsfx);
				player.kill();
			}
		}
		);
		FlxG.overlapArrayList(stationaryBlocks, player, new FlxCollideListener()
		{
			public void Collide(FlxCore object1, FlxCore object2)
			{
				//-- squashed by a stationary block???
				//   maybe this also needs to be here for the moving
				//   blocks! Ask Grant!
				SFXPool.playSound(R.raw.squishsfx);
				player.kill();
			}
		}
		);
		FlxG.overlapArrayList(hurtBlocks, player, new FlxCollideListener()
		{
			public void Collide(FlxCore object1, FlxCore object2)
			{
				player.hurt(1F);
			}
		}
		);
		FlxG.overlapArrayList(deathBlocks, player, new FlxCollideListener()
		{
			public void Collide(FlxCore object1, FlxCore object2)
			{
				player.kill();
				//Level.switchLevel("lvl_test3","");
			}
		}
		);

		/*
		 * ENEMY COLLISIONS
		 */
		FlxG.collideArrayLists(enemies, stationaryBlocks);
		FlxG.collideArrayLists(enemies, movingBlocks);
		FlxG.overlapArrayList(enemies, player, new FlxCollideListener()
		{
			/*
			 * Enemy specific handling is done in the enemy classes. 
			 * An empty collide function is needed so the default overlaps function doesn't kill 
			 *		the enemy and the player automatically.
			*/
			public void Collide(FlxCore object1, FlxCore object2)
			{}
		}		
		);
		
		/*
		 * ENEMY PROJECTILE COLLISIONS
		 */
		FlxG.collideArrayLists(stationaryBlocks, enemyMissiles);
		FlxG.collideArrayList(enemyMissiles, player);

		/*
		 * JUMP BLOCKS - have to trigger the overridden overlaps function.
		 */
		FlxG.overlapArrayList(jump, player);

		/*
		 * POWER UPS - just overlap these... on overlap they
		 * will set exists to false and viola! Gone!
		 */
		FlxG.overlapArrayList(powerUps, player);
	}

	/**
	 * Adds Level compenents to the layer in the superclass FlxState.
	 */
	private void addComponentsToLayer()
	{
		for(Iterator<FlxCore> it = backgrounds.iterator(); it.hasNext();)
			super.add(it.next());

		for(Iterator<FlxText> it = texts.iterator(); it.hasNext();)
			super.add(it.next());

		for(Iterator<FlxCore> it = middle_grounds.iterator(); it.hasNext();)
			super.add(it.next());
	
		for(Iterator<FlxBlock> it = movingBlocks.iterator(); it.hasNext();)
			super.add(it.next());

		for(Iterator<FlxBlock> it = stationaryBlocks.iterator(); it.hasNext();)
			super.add(it.next());

		for(Iterator<FlxBlock> it = hurtBlocks.iterator(); it.hasNext();)
			super.add(it.next());

		for(Iterator<FlxCore> it = deathBlocks.iterator(); it.hasNext();)
			super.add(it.next());

		super.add(player);
		super.add(Player.CHUNKIES);
		super.add(EnergyDrink.SPARKS);

		for(Iterator<Enemy> it = enemies.iterator(); it.hasNext();)
			super.add(it.next());
		
		for(Projectile p : enemyMissiles)
		{
			super.add(p);
		}

		for(Iterator<FlxBlock> it = jump.iterator(); it.hasNext();)
			super.add(it.next());

		for(Iterator<FlxBlock> it = powerUps.iterator(); it.hasNext();)
			super.add(it.next());

		super.add(headsUp);
	}

	/**
	 * PRE: Player is in the FlxState layer, height/width set.
	 * POST: Camera focuses and follows based on level width + height
	 */
	private void setCameraFollow()
	{
		FlxG.follow(player, 2.5f);
		//-- Follow a bit lower
		FlxG.followAdjust(0.5f, 0.3f);
		// Changed to stay within bounds of screen for storyboards
		//FlxG.followBounds(0, 0, this.width + 100, this.height + 100);
		FlxG.followBounds(0, 0, this.width, this.height);
	}

	/**
	 * Copies all mutable objects from the provided Level to this instance
	 * @param level
	 */
	private final void copyContents(Level level)
	{
		this.enemies = level.enemies;
		this.labels = level.labels;
		this.jump = level.jump;
		this.movingBlocks = level.movingBlocks;
		this.stationaryBlocks = level.stationaryBlocks;
		this.hurtBlocks = level.hurtBlocks;
		this.deathBlocks = level.deathBlocks;
		this.name = level.name;
		//this.background = level.background;
		this.width = level.width;
		this.height = level.height;
		this.defaultTexture = level.defaultTexture;
		this.music = level.music;
		this.powerUps = level.powerUps;
		this.headsUp = level.headsUp;
		this.backgrounds = level.backgrounds;
		this.middle_grounds = level.middle_grounds;
		this.texts = level.texts;
	}
}