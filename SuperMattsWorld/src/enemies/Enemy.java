package enemies;

import java.util.ArrayList;
import java.util.Arrays;

import org.flixel.FlxCore;
import org.flixel.FlxResourceManager;
import org.flixel.FlxSprite;
import org.myname.flixeldemo.Player;

//An enemy that moves sideways
public class Enemy extends FlxSprite
{	
	private static final float GRAVITY_ACCELERATION = 500;
	protected final int VELOCITY_AFTER_HIT = -150;
	protected static final float DAMAGE_FROM_PLAYER = 1;
	//private static final FlxSound yupyup = new FlxSound().loadEmbedded(R.raw.yup_yup);

	//set to true if you want the enemy to be hurt by the player jumping on it
	protected boolean killable = false;
	//set to true if overriding the default sideways enemy movement
	protected boolean doingSomethingElse = false;
	protected float speed;

	public Enemy(int startX, int startY, float horizontalSpeed, int SimpleGraphic, boolean isKillable, float health)
	{
		super(startX, startY, SimpleGraphic, true);
		this.killable = isKillable;
		this.health = health;
		this.speed = horizontalSpeed;
		this.velocity.x = speed;
		this.acceleration.y = GRAVITY_ACCELERATION;
		
		if(speed >=0)
		{
			setFacing(LEFT);
		}
		else
		{
			setFacing(RIGHT);
		}
		
		//Get the number of frames for the animation
		int orininalImageWidth = (FlxResourceManager.getImage(SimpleGraphic)).width;
		int numberOfFrames = orininalImageWidth / this.width;
		Integer[] animationFrames = new Integer[numberOfFrames];
		for(int i = 0; i < numberOfFrames; i++)
		{
			animationFrames[i] = i;
		}
		
		super.addAnimation("moving", new ArrayList<Integer>(Arrays.asList(animationFrames)), 12);
	}
	
	public void update()
	{
		if(this.onScreen())
		{
			super.play("moving");
			if(!doingSomethingElse)
			{
				this.velocity.x = speed;
			}
		}
		else
		{
			this.velocity.x = 0;
		}
		super.update();
	}

	public boolean collideX(FlxCore core)
	{
		if(this.onScreen() && core.onScreen())
		{
			boolean collidedX = super.collideX(core);
	
			//on a collision in the x direction, change directions
			if(collidedX)
			{
				//yupyup.play();
				this.speed = -this.speed;
				this.velocity.x = this.speed;
				if(this.speed >= 0)
				{
					setFacing(LEFT);
				}
				else
				{
					setFacing(RIGHT);
				}
			}
	
			return collidedX;
		}
		else
		{
			return false;
		}
	}
	
	public boolean overlaps(FlxCore Core)
	{
		if(this.onScreen() && Core.onScreen())
		{
			boolean overlaps = super.overlaps(Core);
			
			//if the player overlaps the enemy
			if(overlaps && Core instanceof Player)
			{
				//check if player overlapped from the top
				if(this.topCollision(Core) && this.killable)
				{
					this.hurt(DAMAGE_FROM_PLAYER);
					
					if(!this.dead)
					{
						//if this was not the killing blow then we need to move the player above the enemy
						//so that the objects are no longer overlapping
						Core.y = this.y - Core.height;
					}
					((Player)Core).velocity.y = VELOCITY_AFTER_HIT;
				}
				else
				{
					Core.kill();
				}
			}
			return overlaps;
		}
		else
		{
			return false;
		}
	}
	
	private boolean topCollision(FlxCore Core)
	{
		boolean collision = false;
		//Below code was taken from FlxCore.
		float ctp = Core.y - Core.last.y;
		float ttp = y - last.y;
		boolean tco = (Core.y < y + height) && (Core.y + Core.height > y);
		if(	( (ctp > 0) && (ttp <= 0) ) ||
			( (ctp >= 0) && ( ( ctp >  ttp) && tco ) ) ||
			( (ctp <= 0) && ( (-ctp < -ttp) && tco ) ) )
		{
			if(Core.y + Core.height > this.y)
			{
				collision = true;
			}
		}
		
		return collision;
	}
}