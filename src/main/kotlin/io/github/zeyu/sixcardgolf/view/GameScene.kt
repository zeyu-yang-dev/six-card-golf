package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.service.RootService
import io.github.zeyu.sixcardgolf.service.CardImageLoader
import io.github.zeyu.sixcardgolf.view.panes.*

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


/**
 * The game scene for the six card golf game, which inherits from [BoardGameScene].
 *
 */
class GameScene(private val rootService: RootService) : BoardGameScene(1600, 900), Refreshable {

    val panePlayerLeft = PanePlayerLeft(rootService, this)
    val panePlayerRight = PanePlayerRight(rootService, this)
    val panePlayerTop = PanePlayerTop(rootService, this)
    val panePlayerBottom = PanePlayerBottom(rootService, this)
    val paneMiddleCards = PaneMiddleCards(rootService, this)

    private val instructionLabel: Label = Label(
        posX = PMC_POS_X - 17.75,
        posY = PMC_POS_Y - 40,
        width = MIDDLE_LABEL_WIDTH,
        height = MIDDLE_LABEL_HEIGHT,
        text = "This is an instruction bar.",
        font = DEFAULT_INSTRUCTION_FONT,
        alignment = Alignment.CENTER
    )

    //------------------------------------------------------------------------------------------------------------------
    var state = StateOfUI.TURN_START

    // Represents the state of UI, mainly to control interactivity of components.
    enum class StateOfUI {
        TURN_START, // the turn just started or a reveal-action was made
        HAS_DRAWN,
        HAS_DRAWN_DISCARDED,
        HAS_DISCARDED,
        GAME_END
    }

    fun setUIState(newState: StateOfUI) {
        this.state = newState
    }
    //------------------------------------------------------------------------------------------------------------------
    enum class InstructionType {
        FIRST_ROUND_BEFORE_REVEAL,
        FIRST_ROUND_REVEALED_ONE,
        LAST_ROUND,
        AFTER_DRAW,
        AFTER_DRAW_DISCARDED,
        AFTER_DISCARD,
        ROTATING_SEAT,
        TURN_START,
        BEFORE_REVEAL_ALL,
        AFTER_REVEAL_ALL
    }

    private fun updateInstruction(type: InstructionType) {
        when (type) {
            InstructionType.FIRST_ROUND_BEFORE_REVEAL -> {
                instructionLabel.text = "First Round: Please reveal 2 cards."
                instructionLabel.font = GREEN_INSTRUCTION_FONT
            }
            InstructionType.FIRST_ROUND_REVEALED_ONE -> {
                instructionLabel.text = "Reveal one more card to continue."
                instructionLabel.font = DEFAULT_INSTRUCTION_FONT
            }
            InstructionType.LAST_ROUND -> {
                instructionLabel.text = "Last Round!"
                instructionLabel.font = RED_INSTRUCTION_FONT
            }
            InstructionType.AFTER_DRAW -> {
                instructionLabel.text = "Choose a card to swap or discard hand."
                instructionLabel.font = DEFAULT_INSTRUCTION_FONT
            }
            InstructionType.AFTER_DRAW_DISCARDED -> {
                instructionLabel.text = "Choose a card to swap."
                instructionLabel.font = DEFAULT_INSTRUCTION_FONT
            }
            InstructionType.AFTER_DISCARD -> {
                instructionLabel.text = "You have to reveal a card to continue."
                instructionLabel.font = DEFAULT_INSTRUCTION_FONT
            }
            InstructionType.ROTATING_SEAT -> {
                instructionLabel.text = "The seats will rotate in 2 seconds."
                instructionLabel.font = PINK_INSTRUCTION_FONT
            }
            InstructionType.TURN_START -> {
                instructionLabel.text = "Your turn: Draw a card or reveal one."
                instructionLabel.font = GREEN_INSTRUCTION_FONT
            }
            InstructionType.BEFORE_REVEAL_ALL -> {
                instructionLabel.text = "All cards will be revealed in 3 seconds."
                instructionLabel.font = PINK_INSTRUCTION_FONT
            }
            InstructionType.AFTER_REVEAL_ALL -> {
                instructionLabel.text = "Show result in 5 seconds."
                instructionLabel.font = PINK_INSTRUCTION_FONT
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------













    private fun revealAllCards() {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val players = currentGame.players
        
        for (player in players) {
            for (i in 0..2) {
                player.topRow[i]?.isRevealed = true
                player.bottomRow[i]?.isRevealed = true
            }
        }
    }

    /**
     * By the time this function is called, nextTurn will not be called anymore,
     * so the winning player must be checked in this function.
     */
    private fun removeIdenticalRow() {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val players = currentGame.players
        
        for (player in players) {
            
            // Check top row for identical card value, the cards must all be revealed.
            if (player.topRow.all { it != null && it.isRevealed && it.cardValue == player.topRow[0]?.cardValue }) {
                // Set all cards to null and give them to discard-stack if they have identical values
                for (i in 0..2) {
                    currentGame.discardStack.push(player.topRow[i])
                    player.topRow[i] = null
                }
            }
    
            // Check bottom row for identical card value, the cards must all be revealed.
            if (player.bottomRow.all { it != null && it.isRevealed && it.cardValue == player.bottomRow[0]?.cardValue }) {
                // Set all cards to null and give them to discard-stack if they have identical values
                for (i in 0..2) {
                    currentGame.discardStack.push(player.bottomRow[i])
                    player.bottomRow[i] = null
                }
            }

            // DISABLED:
            // // Checks whether both rows of the current player are removed,
            // // if so, mark the winning player and call last round.
            // if (player.topRow[0] == null && player.bottomRow[0] == null && currentGame.winningPlayer == null) {
            //     currentGame.winningPlayer = player
            //     // But this time, no need to call last round anymore:
            //     // callLastRound()
            // }
            
        }

    } 


    //------------------------------------------------------------------------------------------------------------------
    init {
        val image : BufferedImage = ImageIO.read(CardImageLoader::class.java.getResource("/game_background.jpg"))
        this.background = ImageVisual(image)

        addComponents(
            panePlayerLeft,
            panePlayerRight,
            panePlayerTop,
            panePlayerBottom,
            paneMiddleCards,
            instructionLabel
        )
    }

    override fun refreshAfterStartNewGame() {
        state = StateOfUI.TURN_START
        updateInstruction(InstructionType.FIRST_ROUND_BEFORE_REVEAL)
    }

    override fun refreshAfterFirstReveal() {
        updateInstruction(InstructionType.FIRST_ROUND_REVEALED_ONE)
    }


    override fun refreshAfterReveal() {}

    override fun refreshAfterDrawCard() {
        updateInstruction(InstructionType.AFTER_DRAW)
    }

    override fun refreshAfterDrawDiscardedCard() {
        updateInstruction(InstructionType.AFTER_DRAW_DISCARDED)
    }

    override fun refreshAfterSwap() {}

    override fun refreshAfterDiscard() {
        updateInstruction(InstructionType.AFTER_DISCARD)
    }

    override fun refreshAfterNextTurn() {
        // before the delay animation, show a reminder via instruction bar:
        updateInstruction(InstructionType.ROTATING_SEAT)

        // before refresh for the next turn, play a delay animation
        this.playAnimation(
            DelayAnimation(duration = DELAY_BTW_TURNS).apply {
                onFinished = {
                    // normal TURN_START instruction:
                    updateInstruction(InstructionType.TURN_START)

                    // special TURN_START instruction for the first round:
                    if (rootService.currentGame.isFirstRound) {
                        updateInstruction(InstructionType.FIRST_ROUND_BEFORE_REVEAL)
                    }

                    // special TURN_START instruction for the last round:
                    if (rootService.currentGame.isLastRound) {
                        updateInstruction(InstructionType.LAST_ROUND)
                    }
                }
            }
        )


    }

    override fun refreshOnLastRound() {}

    override fun refreshBeforeGameEnd() {
        // before the delay animation, show a reminder via instruction bar:
        updateInstruction(InstructionType.BEFORE_REVEAL_ALL)

        this.playAnimation(
            DelayAnimation(duration = DELAY_BEFORE_REVEAL_ALL).apply {
                onFinished = {

                    revealAllCards()
                    removeIdenticalRow()

                    updateInstruction(InstructionType.AFTER_REVEAL_ALL)

                    playAnimation(
                        DelayAnimation(duration = 5000).apply {
                            onFinished = {
                                rootService.gameService.endGame()
                            }
                        }
                    )
                }
            }
        )
    }
}














