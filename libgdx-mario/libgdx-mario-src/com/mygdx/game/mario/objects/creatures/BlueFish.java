package com.mygdx.game.mario.objects.creatures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.mario.MarioResourceManager;
import com.mygdx.game.mario.game.TileMap;
import com.mygdx.game.mario.objects.Animation;
import com.mygdx.game.mario.objects.Creature;
import com.mygdx.game.mario.objects.particles.BubbleParticle;

/**
 * Kills mario on collision and can't be killed by jumping over it
 * @author maheshkurmi
 *
 */
public class BlueFish extends Creature {
	
	private Animation leftSwim,rightSwim, dead, flip;
	private static TextureRegion[]v;
	private static boolean initialized=false;
	private int initY;
	
	public BlueFish(int x, int y) {
		
		super(x, y);
		inWater=true;
		initY=y;
		if (!initialized){
			v = MarioResourceManager.instance.creatures.BlueFish;
			initialized=true;
		}
		leftSwim = new Animation(200).addFrame(v[0]).addFrame(v[1]);
		rightSwim = new Animation(200).addFrame(v[2]).addFrame(v[3]);
		flip = new Animation().addFrame(v[4]).addFrame(v[4]);
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		dead = new DeadAfterAnimation().setDAL(100).addFrame(v[4]).setDAL(20).addFrame(v[4]);
		setAnimation(leftSwim);
	}
	
	@Override
	public void xCollide(Vector2 p) {
		super.xCollide(p);
		if(currentAnimation() == leftSwim) {
			setAnimation(rightSwim);
		} else {
			setAnimation(leftSwim);
		}
	}
	
	@Override
	public void creatureXCollide() {
		if(dx > 0) {
			x = x - 2;
			setAnimation(leftSwim);
		} else {
			setAnimation(rightSwim);
			x = x + 2;
		}
		dx = -dx;
	}
	
	@Override
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
		if(isLeft) {
			dx=-0.04f ;
			setAnimation(leftSwim);
		} else {
			dx=0.04f;
			setAnimation(rightSwim);
		}
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
		if (isFlipped() || !inWater){
			gravityFactor=1;
			super.updateCreature(map, time);
			
		}else{
			gravityFactor=0.00f;
			super.updateCreature(map, time);
			//x=x+dx;
			if (x<0 || x>map.getWidth()*16)xCollide(null);
			//super.update(time);
			if (inWater){
				if (Math.random()>0.98){
	        	   map.creaturesToAdd().add(new BubbleParticle((int)getX(), (int)getY()));
				}
				y=(float) (initY+6*Math.sin(x/7));

			}
		}
	}
	
	@Override
	public void jumpedOn() {
		setAnimation(dead);
		setIsCollidable(false);
		dx = 0;
		dy = 0;
	}
	
	@Override
	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.1f;
		dx = 0;
	}

}