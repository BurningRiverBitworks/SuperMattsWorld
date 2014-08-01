package enemies;


//--An enemy that bounces while moving sideways
public class BouncingEnemy extends Enemy
{
	protected float verticalVelocity;
	
	public BouncingEnemy(int startX, int startY, float horizontalSpeed, float verticalSpeed, int SimpleGraphic, boolean isKillable, float health)
	{
		super(startX, startY, horizontalSpeed, SimpleGraphic, isKillable, health);
		
		float positiveVerticalSpeed = Math.abs(verticalSpeed);
		//don't set maxVelocity.y if verticalSpeed is 0 because we still want this to be affected by gravity
		if(verticalSpeed != 0)
		{
			maxVelocity.y = positiveVerticalSpeed;
		}
		this.verticalVelocity =  positiveVerticalSpeed * -1;	
	}
	
	public void update()
	{
		if(this.onScreen())
		{
			//if on the ground, then bounce
			if(this.velocity.y ==0)
			{
				this.velocity.y = this.verticalVelocity;
			}
		}
		super.update();
	}
}
