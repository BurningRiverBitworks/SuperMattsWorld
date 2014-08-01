package enemies;

import org.myname.flixeldemo.Player;

//--An enemy that moves sideways. If the player is within MAXIMUM_DISTANCE then it will move
//	horizontally toward the player
//--A SmartEnemy contains a reference to the player. This is so that when the player is on screen, the 
//	enemy will move toward the player
public class SmartEnemy extends Enemy
{
	protected Player player;
	protected float runningSpeed;
	protected final int VELOCITY_AFTER_KILL = -150;
	protected final int MAXIMUM_DISTANCE = 200;
	
	public SmartEnemy(int x, int y, float horizontalSpeed, Integer Graphic, boolean isKillable, float health)
	{
		super(x, y, horizontalSpeed, Graphic, isKillable, health);
		runningSpeed = speed + 20;
		this.player = null;
	}
	
	public SmartEnemy(int x, int y, float horizontalSpeed, Integer Graphic, boolean isKillable, float health, Player player)
	{
		super(x, y, horizontalSpeed, Graphic, isKillable, health);
		runningSpeed = speed + 20;
		this.player = player;
	}
	
	//Add a reference to the player if not done so in the constructor
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public void update()
	{
		if(this.onScreen())
		{
			if(this.player != null)
			{
				boolean playerInRange = Math.abs(this.player.x - this.x) <= MAXIMUM_DISTANCE //player within horizontal distance
										&& this.player.y < this.y + this.height //player not below this
										&& this.player.y + this.player.height >= this.y - MAXIMUM_DISTANCE; //player not too far up
				if(playerInRange)
				{
					this.doingSomethingElse = true;
					float centerOfPlayer = this.player.x + (this.player.width / 2);
					if(centerOfPlayer < this.x)
					{
						//center of player to the left of this, so move left
						this.velocity.x = -Math.abs(runningSpeed);
						this.setFacing(RIGHT);
					}
					else if(centerOfPlayer > this.x + this.width)
					{
						// center of player to the right of this, so move right
						this.velocity.x = Math.abs(runningSpeed);
						this.setFacing(LEFT);
					}
					else
					{
						//player directly above, so don't move
						this.velocity.x = 0;
					}
				}
				else
				{
					this.doingSomethingElse = false;
				}
			}
			else
			{
				this.doingSomethingElse = false;
			}		
		}
		super.update();
	}
}
