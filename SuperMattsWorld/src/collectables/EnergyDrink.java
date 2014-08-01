package collectables;

import org.flixel.FlxEmitter;
import org.myname.flixeldemo.Player;
import org.myname.flixeldemo.R;

import audio.SFXPool;

/**
 * Turns Matt into SuperMatt and plays the appropriate sound
 * 
 * @author Tony
 * @see PowerUp
 */
public final class EnergyDrink extends PowerUp
{
	public static final FlxEmitter SPARKS = new FlxEmitter(0, 0, -0.5F)
	.setXVelocity(-550.0F, 550.0F)
	.setYVelocity(-550.0F, 100F)
	.setRotation(0, 720)
	.setGravity(50F);

	protected EnergyDrink(int X, int Y, int Width, int Height)
	{
		super(X, Y, Width, Height);
		super.loadGraphic(R.drawable.red_bull);
	}

	@Override
	protected void onCollect(Player p)
	{
		//-- Make him into Steve..
		SPARKS.createSprites(R.drawable.yellow_box);
		SPARKS.x = p.x + (p.width >> 1);
		SPARKS.y = p.y + (p.height >> 1);
		SPARKS.restart();
		p.health = Player.HEALTH_STEVE;
		p.flicker(0.5F);
	}

	@Override
	protected void playSound()
	{
		SFXPool.playSound(R.raw.energysfx);
	}
}