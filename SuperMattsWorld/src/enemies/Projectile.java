package enemies;

import org.flixel.FlxCore;
import org.flixel.FlxSprite;
import org.myname.flixeldemo.Player;

//A basic projectile that will kill a player on contact
public class Projectile extends FlxSprite
{		
	protected int GRAVITY_ACCELERATION = 200;
	
	public Projectile(int x, int y, Integer graphic)
	{
		super(x, y, graphic, false);
		this.exists = false;
		this.acceleration.y = GRAVITY_ACCELERATION;
	}
	
	public void update()
	{
		if(this.dead && this.finished)
		{
			this.exists = false;
		}
		else
		{
			super.update();
		}
	}
	
	public boolean hitWall(FlxCore Core)
	{
		this.hurt(0);
		if(Core.getClass() == Player.class)
		{
			Core.kill();
		}
		return true;
	}
	
	public boolean hitCeiling(FlxCore Core)
	{
		this.hurt(0);
		if(Core.getClass() == Player.class)
		{
			//hit the player, so kill it
			Core.kill();
		}
		return true;
	}
	
	public boolean hitFloor(FlxCore Core)
	{
		this.hurt(0);
		if(Core.getClass() == Player.class)
		{
			//hit the player, so kill it
			Core.kill();
		}
		return true;
	}
	
	public void hurt(float damage)
	{
		//we need to 'kill' the projectile when it hits something
		//we don't want to use the kill function because we want to be able to fire this multiple times
		if(this.dead) 
			return;
		this.velocity.x = 0;
		this.velocity.y = 0;
		this.dead = true;
		this.exists = false;
	}
	
	public void shoot(int startX, int startY, int velocityX, int velocityY)
	{
		//Bring the projectile into existance
		super.reset(startX, startY);
		
		//Fire with these velocities
		this.velocity.x = velocityX;
		this.velocity.y = velocityY;
	}
}
