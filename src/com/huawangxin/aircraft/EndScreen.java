package com.huawangxin.aircraft;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.loon.framework.javase.game.GameDeploy;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimerContext;

/**
 * 游戏结束场景，用于显示游戏得分
 * 
 */
public class EndScreen extends Screen {

	private GameDeploy deploy;
	private int score;

	public EndScreen(GameDeploy deploy, int score) {
		this.deploy = deploy;
		this.score = score;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		initBackGround();
	}
	
	/**
	 * 初始化背景
	 */
	private void initBackGround(){
		LImage background = new LImage("assets/bg_start_480_720.png");
		BufferedImage bgImg = background.getBufferedImage();
		Graphics g = bgImg.getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.BOLD, 20));
		g.drawString("你的分数是：" + score + "，按空格键重新开始游戏", 50, 500);
		this.setBackground(bgImg);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_SPACE) {
			deploy.setScreen(new GameScreen(deploy));
		}
	}

	@Override
	public void draw(LGraphics g) {
	}

	@Override
	public void alter(LTimerContext timerCtx) {
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
