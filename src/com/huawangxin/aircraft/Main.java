package com.huawangxin.aircraft;

import org.loon.framework.javase.game.GameDeploy;
import org.loon.framework.javase.game.GameScene;

public class Main {
	public static void main(String[] args) {
		// GameScene封装一个完整的游戏
		GameScene gs = new GameScene("飞机大战", 480, 720);
		// 导演，负责场景的切换
		GameDeploy deploy = gs.getDeploy();

		// 设置刷新频率
		deploy.setFPS(100);
		// 加载场景
		deploy.setScreen(new StartScreen(deploy));
		
		// 开始游戏主循环
		deploy.mainLoop();
		// 显示游戏画面
		gs.showFrame();
	}
}
