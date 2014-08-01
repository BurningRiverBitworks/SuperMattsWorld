package audio;
import java.util.HashMap;

import org.flixel.FlxG;
import org.myname.flixeldemo.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * A lightweight pool class that caches up all of the small sounds in the game. This keeps resources free, yet
 * allows sounds to be played without delay.
 * 
 * @author Tony Greer
 */
public final class SFXPool
{
	/* ADD ALL/ADDITIONAL SFX HERE ONLY... */
	private static final int[] AVAILABLE_SOUNDS = new int[]{R.raw.beersfx, R.raw.cigarettesfx,
		R.raw.death1, R.raw.death2, R.raw.energysfx,
		R.raw.head_blocksfx, R.raw.jumpsfx, R.raw.squishsfx};

	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundMap;

	private SFXPool(){}

	/**
	 * Build the pool. It is not recommended this is done mid-game.
	 * 
	 * @param c
	 */
	public static final void init(Context c)
	{
		release();

		soundPool = new SoundPool(9, AudioManager.STREAM_MUSIC, 20);
		soundMap = new HashMap<Integer, Integer>();

		for(final int i : AVAILABLE_SOUNDS)
			soundMap.put(i, soundPool.load(c, i, 1));
	}

	/**
	 * Release pool resources and cache
	 */
	public static final void release()
	{
		if(soundPool != null)
		{
			soundPool.release();
			soundPool = null;
		}

		if(soundMap != null)
		{
			soundMap.clear();
			soundMap = null;
		}
	}

	/**
	 * Plays the provided sound by it's resource ID
	 * 
	 * @param sound
	 */
	public static final void playSound(int sound)
	{
		if(!isInit() || !soundMap.containsKey(sound))
			return;

		soundPool.play(soundMap.get(sound), FlxG.getVolume(), FlxG.getVolume(), 1, 0, 1F);
	}

	/**
	 * Returns whether or not the pool is active.
	 * 
	 * @return boolean
	 */
	public static boolean isInit()
	{
		return soundMap != null && soundMap.size() > 0
			&& soundPool != null;
	}

	@Override
	protected void finalize() throws Throwable
	{
		release();
		super.finalize();
	}
}