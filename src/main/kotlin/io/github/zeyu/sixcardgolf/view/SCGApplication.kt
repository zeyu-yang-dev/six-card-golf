package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.service.RootService
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
            if (event.keyCode in listOf(KeyCode.M, KeyCode.ENTER)) {
                rootService.gameService.showMiracle()
            }
        }
    }

    // 3. End-Game Scene:
    private val resultMenuScene = ResultMenuScene(rootService).apply {

        restartButton.onMouseClicked = {

            // remove all components in case restartButton is pressed,
            // because when [refreshAfterStartNewGame] is called, proper number of components will be added
            gameScene.clearComponents()
            // remove all components in case restartButton is pressed,
            // because when [refreshAfterGameEnd] is called, proper number of components will be added
            this.clearComponents()

            this@SCGApplication.showMenuScene(mainMenuScene)
        }

        exitButton.onMouseClicked = {
            exit()
        }
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
            gameScene.panePlayerTop
        )

        // This is just done so that the blurred background when showing the new game menu
        // has content and looks nicer
        // rootService.gameService.startNewGame(listOf("Martin", "Erich", "Paul", "Detlef"))

        this.showGameScene(gameScene)
        this.showMenuScene(mainMenuScene)
    }


    override fun refreshAfterStartNewGame() {

        gameScene.panePlayerLeft.isVisible = true
        gameScene.panePlayerRight.isVisible = true

        // If there are 2 players, hide the player panes on the left and right sides:
        if(rootService.currentGame.players.size == 2) {
            gameScene.panePlayerLeft.isVisible = false
            gameScene.panePlayerRight.isVisible = false
        }

        // If there are 3 players, hide the player pane on the right side:
        if(rootService.currentGame.players.size == 3) {
            gameScene.panePlayerRight.isVisible = false
        }


        this.hideMenuScene()
    }

    override fun refreshAfterGameEnd() {
        this.showMenuScene(resultMenuScene)
    }

}

