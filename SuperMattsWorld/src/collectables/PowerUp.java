package collectables;

import org.flixel.FlxBlock;
import org.flixel.FlxCore;
import org.myname.flixeldemo.Player;

public abstract class PowerUp extends FlxBlock
{
	/** Type for a cigarette power-up */
	public static final String TYPE_CIGARETTE = "cigarette";
	/** Type for a beer power-up */
	public static final String TYPE_BEER = "beer";
	/** Type for a energy drink power-up */
	public static final String TYPE_ENERGY_DRINK = "energy_drink";

	protected PowerUp(int X, int Y, int Width, int Height)
	{
		super(X, Y, Width, Height);
	}

	/**
	 * Returns an active instance of the PowerUp (what class depends on type) with the graphic
	 * pre-loaded.
	 * 
	 * @param X
	 * @param Y
	 * @param Width
	 * @param Height
	 * @param type
	 * @return
	 */
	public static PowerUp getInstance(int X, int Y, String type)
	{
		final PowerUp p;

		/*
		 * Instantiation should provide all the necessary prerequisites
		 * for the power up to be placed on the screen.
		 */
		if(TYPE_CIGARETTE.equals(type))
			p = new Cigarette(X, Y, 16, 16);
		else if(TYPE_BEER.equals(type))
			p = new Beer(X, Y, 16, 16);
		else if(TYPE_ENERGY_DRINK.equals(type))
			p = new EnergyDrink(X, Y, 16, 16);
		else
			p = null;

		return p;
	}
	/**
	 * Called when the <code>Player</code> collects a power-up. This is where the
	 * attributes of the Player change.
	 * 
	 * @param p
	 */
	protected abstract void onCollect(Player p);

	/**
	 * Plays the "collect" sound for the item.
	 */
	protected abstract void playSound();

	@Override
	/**
	 * Determines when the Player collects the Power-up on screen.
	 */
	public final boolean overlaps(FlxCore Core)
	{
		final boolean overlap = this.onScreen() && super.overlaps(Core);

		if(overlap && Core instanceof Player)
		{
			//-- kill the item and call the onCollect.
			this.playSound();
			this.onCollect((Player)Core);
			this.exists = false;
		}

		return false;
	}
}