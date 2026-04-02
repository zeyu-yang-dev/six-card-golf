package io.github.zeyuyangdev.sixcardgolf.view.panes

import io.github.zeyuyangdev.sixcardgolf.service.*
import io.github.zeyuyangdev.sixcardgolf.view.*

class PanePlayerRight(
    rootService: RootService,
    gameScene: GameScene
) : PanePlayer(
    rootService,
    gameScene,
    SCREEN_WIDTH - PPL_DIS_TO_LEFT - PPL_WIDTH,
    PPL_POS_Y,
    PPL_WIDTH,
    PPL_HEIGHT
) {
    override fun getPlayerOffset(numOfPlayers: Int): Int {
        // This pane is only shown, when there are 4 players.
        // In this case, this pane always shows the deck of the next player.
        return 1
    }
}