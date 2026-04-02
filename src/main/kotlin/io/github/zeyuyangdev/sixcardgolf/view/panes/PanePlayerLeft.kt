package io.github.zeyuyangdev.sixcardgolf.view.panes

import io.github.zeyuyangdev.sixcardgolf.service.*
import io.github.zeyuyangdev.sixcardgolf.view.*

class PanePlayerLeft(
    rootService: RootService,
    gameScene: GameScene
) : PanePlayer(
    rootService,
    gameScene,
    PPL_POS_X,
    PPL_POS_Y,
    PPL_WIDTH,
    PPL_HEIGHT
) {
    override val cardViewStartX = PLAYER_LABEL_WIDTH + DIS_BTW_LABEL_AND_CARD
    override val labelStartX = 0.0

    // To make the overridden positioning parameters effective,
    // components must be added here rather than in the father class.
    init {
        addPaneComponents()
    }

    override fun getPlayerOffset(numOfPlayers: Int): Int {
        // This pane is shown, when there are 3 or 4 players.
        // If there are 3 players, this pane shows the player two positions ahead.
        // If there are 4 players, this pane shows the player three positions ahead.
        return if (numOfPlayers == 4) 3 else 2
    }
}