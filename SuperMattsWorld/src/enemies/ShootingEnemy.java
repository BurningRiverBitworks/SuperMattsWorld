package enemies;

import org.myname.flixeldemo.Player;

//--An enemy that moves sideways. If the player is within MAXIMUM_DISTANCE then it will stop
//	moving and fire its missile left or right depending on the position of the player
//--A ShootingEnemy contains a reference to the player. This is so that when the player is on screen, the 
//	enemy will shoot toward the player
public class ShootingEnemy extends Enemy
{
	protected Projectile missile;
	protected Player player;
	protected int missleWidth;
	protected final int MAX_MISSILE_VELOCITY = 100;
	protected final int MISSILE_VERTICAL_VELOCITY = 50;
	protected final int MAXIMUM_DISTANCE = 200;
	protected final int VELOCITY_AFTER_KILL = -150;
	
	public ShootingEnemy(int x, int y, float horizontalSpeed, Integer EnemyGraphic, Projectile ammo, boolean isKillable, float health)
	{
		super(x, y, horizontalSpeed, EnemyGraphic, isKillable, health);
		
		this.missile = ammo;
		this.missleWidth = this.missile.width;
		this.player = null;
	}
	
	public ShootingEnemy(int x, int y, float horizontalSpeed, Integer EnemyGraphic, Projectile ammo, boolean isKillable, float health, Player player)
	{
		super(x, y, horizontalSpeed, EnemyGraphic, isKillable, health);
		
		this.missile = ammo;
		this.missleWidth = this.missile.width;
		this.player = player;
	}
	
	//Add a reference to the player if not done so in the constructor
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public Projectile getProjectile()
	{
		return this.missile;
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
					if(!this.missile.exists)
					{
						if(this.player.x < this.x)
						{
							//player is to the left, so shoot left
							this.missile.shoot((int)this.x - missleWidth, (int)this.y, -MAX_MISSILE_VELOCITY, -MISSILE_VERTICAL_VELOCITY);
							
							setFacing(RIGHT);
						}
						else
						{
							//player is to the right, so shoot right
							this.missile.shoot((int)this.x + this.width + missleWidth, (int)this.y, MAX_MISSILE_VELOCITY, -MISSILE_VERTICAL_VELOCITY);
							
							setFacing(LEFT);
						}
					}
					//don't move while shooting
					this.velocity.x = 0;
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
