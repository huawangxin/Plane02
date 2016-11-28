package com.huawangxin.aircraft;

import org.loon.framework.javase.game.GameDeploy;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;
import org.loon.framework.javase.game.core.timer.LTimerContext;

public class StartScreen extends Screen {
	private Sprite aircraft;
	private GameDeploy deploy;

	// 计时器 20毫秒
	private LTimer timer = new LTimer(20);

	public StartScreen(GameDeploy deploy) {
		this.deploy = deploy;
	}

	@Override
	public void onLoaded() {
		super.onLoaded();
		//初始化背景
		initBackGround();
		//初始化精灵
		initSprite();
	}

	@Override
	public void alter(LTimerContext timerCtx) {
		/**
		 * 每隔20毫秒移动一次飞机
		 */
		if (timer.action(timerCtx)) {
			moveSprite();
		}
	}
	/**
	 * 初始化界面背景
	 */
	private void initBackGround(){
		// LImage封装图片信息
		// 加载图片
		LImage background = new LImage("assets/bg_start_480_720.png");
		// 设置背景方法Screen
		setBackground(background);
	}
	/**
	 * 初始化精灵
	 */
	private void initSprite(){
		// 创建精灵
		aircraft = new Sprite("assets/start_aircraft.png");

		// 精灵加入场景
		add(aircraft);

		aircraft.setLocation(0, 400);
	}
	/**
	 * 移动精灵
	 */
	private void moveSprite(){
		aircraft.move_right(5);
		if (aircraft.getX() > getWidth()) {
			timer.stop();
			System.out.println("切换到主场景");
			deploy.setScreen(new GameScreen(deploy));
		}
	}
	@Override
	public void draw(LGraphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchDown(LTouch e) {
	}

	@Override
	public void onTouchUp(LTouch e) {

	}

	@Override
	public void onTouchMove(LTouch e) {
	}

}
