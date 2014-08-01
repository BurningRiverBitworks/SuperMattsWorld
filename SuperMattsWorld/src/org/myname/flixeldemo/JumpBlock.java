package org.myname.flixeldemo;

import org.flixel.FlxBlock;
import org.flixel.FlxCore;
import org.myname.flixeldemo.parsing.Level;

/**
 * This block is designed to allow the player to jump either to a location inside the level
 * or to another level altogether.
 * 
 * @author Tony
 */
public final class JumpBlock extends FlxBlock
{
	/** The label to jump to within the level specified */
	protected final String label;
	/** The level to which the label is contained. Cannot be changed. */
	protected final String goToLevel;
	/** Only hit this block once then it is ineffective. default: <tt>false</tt> */
	protected final boolean oneTimeOnly;

	//-- Internal variable used for determining whether or
	//   not this bad boy has been hit/used already.
	private boolean hitAlready = false;

	public JumpBlock(int X, int Y, int Width, int Height, String goToLevel, String label, boolean oneTimeOnly)
	{
		super(X, Y, Width, Height);
		this.oneTimeOnly = oneTimeOnly;
		this.goToLevel = goToLevel;
		this.label = label;
	}

	@Override
	/**
	 * Checks properties of the JumpBlcok to see if we should activate a
	 * level/state switch for the jump.
	 */
	public boolean overlaps(FlxCore Core)
	{
		//-- If there is an overlap, check
		//   the instance variables and switch state if necessary
		if(!(hitAlready && oneTimeOnly))
		{
			if(super.overlaps(Core))
			{
				this.hitAlready = true;
				Level.switchLevel(this.goToLevel, this.label);
			}
		}

		return false;   
	}
}
