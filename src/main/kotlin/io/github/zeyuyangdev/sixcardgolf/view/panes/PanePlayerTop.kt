package io.github.zeyuyangdev.sixcardgolf.view.panes

import io.github.zeyuyangdev.sixcardgolf.service.*
import io.github.zeyuyangdev.sixcardgolf.view.*

class PanePlayerTop(
    rootService: RootService,
    gameScene: GameScene
) : PanePlayer(
    rootService,
    gameScene,
    (SCREEN_WIDTH - CARD_WIDTH * 3 - HORIZ_DIS_BTW_CARDS * 2) / 2,
    PPT_DIS_TO_TOP,
    PPL_WIDTH,
    PPL_HEIGHT
) {
    init {
        addPaneComponents()
    }

    override fun getPlayerOffset(numOfPlayers: Int): Int {
        // This pane is shown, when there are 2, 3 or 4 players.
        // If there are 2 or 3 players, this pane shows the next player.
        // If there are 4 players, this pane shows the player two positions ahead.
        return if (numOfPlayers == 4) 2 else 1
    }
}