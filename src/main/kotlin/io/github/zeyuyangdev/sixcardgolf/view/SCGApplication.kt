package io.github.zeyuyangdev.sixcardgolf.view

import io.github.zeyuyangdev.sixcardgolf.service.Refreshable
import io.github.zeyuyangdev.sixcardgolf.service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode

/**
 * The view controller class, which inherits from [BoardGameApplication].
 * This class implements the Refreshable interface.
 * This class must implement all the methods defined in the Refreshable interface.
 */
class SCGApplication : BoardGameApplication("Six Card Golf"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes:
    // 1. New Game Menu Scene:
    private val mainMenuScene = MainMenuScene(rootService).apply {
        exitButton.onMouseClicked = {
            exit()
        }
    }

    // 2. In-Game Scene:
    private val gameScene = GameScene(rootService).apply {
        onKeyPressed = { event ->
            if (event.keyCode in listOf(KeyCode.ENTER)) {
                rootService.debugGameService.triggerRowRemoval()
            }
        }
    }

    // 3. End-Game Scene:
    private val resultMenuScene = ResultMenuScene(rootService).apply {
        restartButton.onMouseClicked = { showMenuScene(mainMenuScene) }
        exitButton.onMouseClicked = { exit() }
    }

    init {

        // all scenes and the application itself need to react to changes done in the service layer
        rootService.addRefreshables(
            this,
            mainMenuScene,
            gameScene,
            resultMenuScene,
            gameScene.panePlayerLeft,
            gameScene.panePlayerRight,
            gameScene.panePlayerTop,
            gameScene.panePlayerBottom,
            gameScene.paneMiddleCards
        )

        // This is just done so that the blurred background when showing the menu has content and looks nicer
        this.showGameScene(gameScene)
        this.showMenuScene(mainMenuScene)
    }

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }

    override fun refreshAfterGameEnd() {
        this.showMenuScene(resultMenuScene)
    }
}

