package com.huawangxin.aircraft;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.loon.framework.javase.game.GameDeploy;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;
import org.loon.framework.javase.game.core.timer.LTimerContext;

public class GameScreen extends Screen {
	
	// 玩家飞机的移动速度
	private int player_speed = 5;
	
	// 玩家按下了哪个方向键
	private boolean to_up = false;
	private boolean to_down = false;
	private boolean to_left = false;
	private boolean to_right = false;
	
	// 玩家飞机
	private AircraftSprite player;

	// 敌机集合
	private List<AircraftSprite> foeList = new ArrayList<AircraftSprite>();

	// 子弹集合
	private List<AircraftSprite> bulletList = new ArrayList<AircraftSprite>();

	// 敌机定时器，用于控制敌机出现的频率（800毫秒）
	private LTimer foeTimer = new LTimer(800);

	// 子弹定时器，用于控制子弹出现的频率（300毫秒）
	private LTimer bulletTimer = new LTimer(200);

	// 控制敌机降落和子弹飞行的频率
	private LTimer timer = new LTimer(20);

	// 销毁定时器
	private LTimer destroyTimer = new LTimer(150);

	// 玩家分数
	private int score = 0;

	private GameDeploy deploy;

	public GameScreen(GameDeploy deploy) {
		this.deploy = deploy;
	}

	@Override
	public void onLoaded() {
		super.onLoad();
		
		//  初始化背景
		initBackGround();
		
		//  初始化玩家的飞机
		initPlayer();
		
	}

	/**
	 *  初始化背景
	 */
	private void initBackGround(){
		// 加载背景
		LImage background = new LImage("assets/bg_480_720.png");
		this.setBackground(background);
	}
	/**
	 *  初始化玩家的飞机
	 */
	private void initPlayer(){
		// 创建玩家飞机
		player = new AircraftSprite("assets/player_aircraft.png",
				"assets/player_aircraft_destroy.png", 110, 120);
		add(player);
		player.setLocation(200, 500);
	}
	
	
	
	@Override
	public void alter(LTimerContext timerCtx) {
		/**
		 * 每帧刷新时，先看玩家的飞机是否初始化完毕，初始化好才开始游戏的逻辑
		 */
		if(player == null){
			return;
		}
		if (timer.action(timerCtx)) {
			// 移动玩家的飞机
			movePlayer();
			
			// 移动敌机
			moveEnemy();
			
			// 移动子弹
			moveBullet();

			// 检测子弹是否打到敌机
			checkHitEnemy();

			// 删除加了删除标记的子弹
			removeBullet();

			// 判断敌人是否撞到飞机
			checkHitPlayer();
		}

		// 创建敌机
		if (foeTimer.action(timerCtx)) {
			createEnemy();
		}
		
		// 创建子弹,每次创建两个
		if (bulletTimer.action(timerCtx)) {
			createBullet();
		}
		
		// 显示销毁动画
		if (destroyTimer.action(timerCtx)) {
			playDestoryEnemy();			
			playDestoryPlayer();
		}

	}

	
	/**
	 * 移动玩家的飞机
	 */
	private void movePlayer(){
		if(player != null){
			if (to_up&&player.getY() > 0) {
				player.move_up(player_speed);
			}
			if (to_down&&player.getY() < (getHeight() - player.getHeight())) {
				player.move_down(player_speed);
			}
			if (to_right&&player.getX() < (getWidth() - player.getWidth())) {
				player.move_right(player_speed);
			}
			if (to_left&&player.getX() > 0) {
				player.move_left(player_speed);
			}
		}
	}
	
	/**
	 *  移动敌机
	 */
	private void moveEnemy(){
		for (int i = foeList.size() - 1; i >= 0; i--) {
			foeList.get(i).move_down(4);
			if (foeList.get(i).getY() > getHeight()) {
				// 从场景中删除
				remove(foeList.get(i));
				// 从集合里删除
				foeList.remove(i);
			}
		}
	}
	
	/**
	 *  移动子弹
	 */
	private void moveBullet(){
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).move_up(10);
			if (bulletList.get(i).getY() < 0) {
				bulletList.get(i).setDestroy(true);
			}
		}
	}
	
	/**
	 *  检查子弹是否有击中敌机
	 */
	private void checkHitEnemy(){
		for (AircraftSprite foe : foeList) {
			for (AircraftSprite bullet : bulletList) {
				boolean isCollision = bullet.checkCollision(foe);
				if (isCollision) {
					// 给敌机和子弹加上删除标记
					foe.setDestroy(true);
					foe.setShowDestroyAnimation(true);
					bullet.setDestroy(true);
					score += 100;
				}
			}
		}
	}
	
	/**
	 *   删除子弹
	 */
	private void removeBullet(){
		for (int i = bulletList.size() - 1; i >= 0; i--) {
			if (bulletList.get(i).isDestroy()) {
				// 从场景中删除
				remove(bulletList.get(i));
				// 从集合里删除
				bulletList.remove(i);
			}

		}
	}
	
	/**
	 * 创建敌机
	 * @param timerCtx
	 */
	private void createEnemy(){
		// 创建一个敌人
		System.out.println("创建一个敌人");
		// 制定其显示图片和销毁的动画图片
		AircraftSprite foeAircraft = new AircraftSprite(
				"assets/foe_aircraft.png",
				"assets/foe_aircraft_destroy.png", 69, 94);
		foeAircraft.setLocation(Math.random()
				* (getWidth() - foeAircraft.getWidth()), -94);
		// 加入集合
		foeList.add(foeAircraft);
		// 加入场景
		add(foeAircraft);
	}
	
	/**
	 * 创建子弹
	 * @param timerCtx
	 */
	private void createBullet(){
		AircraftSprite leftBullet = new AircraftSprite(
				"assets/bullet.png");
		AircraftSprite rightBullet = new AircraftSprite(
				"assets/bullet.png");
		leftBullet.setLocation(player.getX() + 23, player.getY() + 25);
		rightBullet.setLocation(player.getX() + 80, player.getY() + 25);
		add(leftBullet);
		add(rightBullet);
		bulletList.add(leftBullet);
		bulletList.add(rightBullet);
	}
	
	/**
	 *   判断敌人是否碰到了玩家
	 */
	private void checkHitPlayer(){
		for (AircraftSprite foe : foeList) {
			boolean isCollision = foe.checkCollision(player);
			if (isCollision) {
				player.setDestroy(true);
				player.setShowDestroyAnimation(true);
				break;
			}

		}
	}
	
	/**
	 *   显示销毁敌机动画
	 */
	private void playDestoryEnemy(){
		for (int i = foeList.size() - 1; i >= 0; i--) {
			AircraftSprite foe = foeList.get(i);
			if (foe.isDestroy() && foe.isShowDestroyAnimation()) {
				boolean isRemove = foe.showDestroyFrame();
				if (isRemove) {
					remove(foe);
					foeList.remove(i);
				}
			}
		}
	}
	
	/**
	 *   显示销毁玩家飞机的动画
	 */
	private void playDestoryPlayer(){
		if (player != null && player.isDestroy()
				&& player.isShowDestroyAnimation()) {
			boolean b = player.showDestroyFrame();
			if (b) {
				timer.stop();
				foeTimer.stop();
				bulletTimer.stop();
				remove(player);
				player = null;
				deploy.setScreen(new EndScreen(deploy, score));
				return;
			}
		}
	}
	
	@Override
	public void onKeyUp(LKey e) {
		// 防止player还没有加载时，用户按键
		if (player != null) {
			// 获取按键的键值
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				to_up = false;
				break;
			case KeyEvent.VK_DOWN:
				to_down = false;
				break;
			case KeyEvent.VK_RIGHT:
				to_right = false;
				break;
			case KeyEvent.VK_LEFT:
				to_left = false;
			}
		}
	}
	@Override
	public void onKeyDown(LKey e) {
		super.onKeyDown(e);
		// 防止player还没有加载时，用户按键
		if (player != null) {
			// 获取按键的键值
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				to_up = true;
				to_down = false;
				break;
			case KeyEvent.VK_DOWN:
				to_down = true;
				to_up = false;
				break;
			case KeyEvent.VK_RIGHT:
				to_right = true;
				to_left = false;
				break;
			case KeyEvent.VK_LEFT:
				to_left = true;
				to_right = false;
			}
		}
	}
	
	@Override
	public void draw(LGraphics lg) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onTouchMove(LTouch e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchUp(LTouch e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchDown(LTouch e) {
		// TODO Auto-generated method stubF
	}

}
