package com.github.thethingyee.StuffyTwo.player.manager;

public class MainManager {

    private final PlayerManager playerManager;

    public MainManager() {
        playerManager = new PlayerManager();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
