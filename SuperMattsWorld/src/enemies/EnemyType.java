package enemies;

import org.myname.flixeldemo.R;

public class EnemyType 
{
	//Fish enemies
	public static final String FISH = "fish";
	public static final float FISH_SPEED = 25;
	public static final float FISH_VERTICAL_SPEED = 100;
	public static final Integer FISH_GRAPHIC = R.drawable.fish;
	public static final boolean FISH_KILLABLE = false;
	public static final float FISH_HEALTH = 1;
	
	//beach goer enemies
	public static final String BEACH_GOER = "beach_goer";
	public static final float BEACH_GOER_SPEED = 50;
	public static final Integer BEACH_GOER_GRAPHIC = R.drawable.beach_goer;
	public static final boolean BEACH_GOER_KILLABLE = true;
	public static final float BEACH_GOER_HEALTH = 1;
	
	//student enemies
	public static final String STUDENT = "student";
	public static final float STUDENT_SPEED = 40;
	public static final Integer STUDENT_GRAPHIC = R.drawable.enemy;//R.drawable.student;
	public static final boolean STUDENT_KILLABLE = true;
	public static final float STUDENT_HEALTH = 2;
	
	//teacher enemies
	public static final String TEACHER = "teacher";
	public static final float TEACHER_SPEED = 40;
	public static final Integer TEACHER_GRAPHIC = R.drawable.enemy;//R.drawable.teacher;
	public static final boolean TEACHER_KILLABLE = true;
	public static final float TEACHER_HEALTH = 2;
	public static final Integer TEACHER_AMMO_GRAPHIC = R.drawable.yellow_box;//R.drawable.book;
	
	protected EnemyType(){}
	
	public static Enemy getInstance(int startX, int startY, String type)
	{
		final Enemy e;
		
		if(FISH.equalsIgnoreCase(type))
		{
			e = new BouncingEnemy(startX, startY, FISH_SPEED, FISH_VERTICAL_SPEED, FISH_GRAPHIC, FISH_KILLABLE, FISH_HEALTH);
		}
		else if(BEACH_GOER.equalsIgnoreCase(type))
		{
			e = new Enemy(startX, startY, BEACH_GOER_SPEED, BEACH_GOER_GRAPHIC, BEACH_GOER_KILLABLE, BEACH_GOER_HEALTH);
		}
		else if(STUDENT.equalsIgnoreCase(type))
		{
			e = new SmartEnemy(startX, startY, STUDENT_SPEED, STUDENT_GRAPHIC, STUDENT_KILLABLE, STUDENT_HEALTH);
		}
		else if(TEACHER.equalsIgnoreCase(type))
		{
			Projectile teacherAmmo = new Projectile(startX, startY, TEACHER_AMMO_GRAPHIC);
			e = new ShootingEnemy(startX, startY, TEACHER_SPEED, TEACHER_GRAPHIC, teacherAmmo, TEACHER_KILLABLE, TEACHER_HEALTH);
		}
		else
		{
			e = null;
		}
		
		return e;
	}
}
