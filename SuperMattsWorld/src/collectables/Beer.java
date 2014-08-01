package collectables;

import org.myname.flixeldemo.Player;
import org.myname.flixeldemo.R;
import org.myname.flixeldemo.parsing.Level;

import audio.SFXPool;

public final class Beer extends PowerUp 
{
	public static float TIME_GAINED = 10F;

	protected Beer(int X, int Y, int Width, int Height)
	{
		super(X, Y, Width, Height);
		super.loadGraphic(R.drawable.beer);
	}

	@Override
	protected void onCollect(Player p)
	{
		Level.timeRemaining += TIME_GAINED;
	}

	@Override
	protected void playSound()
	{
		SFXPool.playSound(R.raw.beersfx);
	}
}