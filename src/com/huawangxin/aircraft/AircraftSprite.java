package com.huawangxin.aircraft;

import java.awt.Image;

import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.action.sprite.SpriteImage;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.utils.GraphicsUtils;

/**
 * 飞行器精灵类，继承自LGame的Sprite类，添加如下方法： 1. 用于显示销毁时动画的方法 2. 用于检测碰撞的方法
 * 
 */
public class AircraftSprite extends Sprite {

	private static final long serialVersionUID = 1L;
	// 是否销毁标记
	private boolean destroy = false;
	// 销毁时是否要显示动画标记
	private boolean showDestroyAnimation = false;
	// 销毁时动画对象
	private Animation destroyAnimation;
	// 销毁时显示动画的当前帧
	private int currentDestroyFrame;
	// 销毁时显示动画的帧数
	private int destroyFrameNum;

	/**
	 * 销毁时显示动画中的一帧，如果已经显示完全部帧返回true，否则返回false
	 * 
	 * @return 是否已经显示完所有帧，即销毁动画是否已经播放完成
	 */
	public boolean showDestroyFrame() {
		if (showDestroyAnimation) {
			this.setRunning(false);
			setAnimation(destroyAnimation);
			if (currentDestroyFrame < destroyFrameNum) {
				this.setCurrentFrameIndex(currentDestroyFrame);
			}
			return ++currentDestroyFrame > destroyFrameNum;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param img
	 * @param destroyImg
	 * @param w
	 * @param h
	 */
	public AircraftSprite(String img, String destroyImg, int w, int h) {
		super(img, w, h);
		// 加载动画的图片
		Image[] images = GraphicsUtils.getSplitImages(destroyImg, w, h);

		destroyFrameNum = images.length;

		currentDestroyFrame = 0;
		destroyAnimation = new Animation();
		for (int i = 0; i < images.length; i++) {
			// 设置该动画的每一帧
			destroyAnimation.addFrame(new SpriteImage(images[i]), 200);
		}
	}

	/**
	 * 
	 * @param img
	 */
	public AircraftSprite(String img) {
		super(img);
	}

	/**
	 * 
	 * @param img
	 * @param w
	 * @param h
	 */
	public AircraftSprite(String img, int w, int h) {
		super(img, w, h);
	}

	/**
	 * 检测与某个Sprite是否碰撞
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean checkCollision(AircraftSprite sprite) {
		if (rect == null) {
			rect = new RectBox((int) this.getX(), (int) this.getY(),
					getWidth(), getHeight());
		} else {
			rect.setBounds((int) this.getX(), (int) this.getY(), getWidth(),
					getHeight());
		}
		return rect.intersects((int) sprite.getX(), (int) sprite.getY(),
				sprite.getWidth(), sprite.getHeight());
	}

	public boolean isDestroy() {
		return destroy;
	}

	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}

	public boolean isShowDestroyAnimation() {
		return showDestroyAnimation;
	}

	public void setShowDestroyAnimation(boolean showDestroyAnimation) {
		this.showDestroyAnimation = showDestroyAnimation;
	}
	

}
