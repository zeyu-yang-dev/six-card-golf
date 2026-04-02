package io.github.zeyuyangdev.sixcardgolf.view

import io.github.zeyuyangdev.sixcardgolf.service.RootService
import io.github.zeyuyangdev.sixcardgolf.service.Refreshable
import io.github.zeyuyangdev.sixcardgolf.view.panes.*

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * The game scene for the six card golf game, which inherits from [BoardGameScene].
 */
class GameScene(
    private val rootService: RootService
) : BoardGameScene(
    width = SCREEN_WIDTH,
    height = SCREEN_HEIGHT
), Refreshable {

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
    init {
        val image : BufferedImage = ImageIO.read(javaClass.getResource("/game_background.jpg"))
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
    //------------------------------------------------------------------------------------------------------------------
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
                    /**
                     * The function nextTurn will check for identical rows and remove them.
                     * However, after all cards are revealed automatically before switching to result scene,
                     * nextTurn will not be called anymore.
                     *
                     * It's possible that one row of a player is identical after all cards revealed automatically.
                     * So it's necessary to check for identical rows here.
                     * Optionally, after automatic reveal of all cards, if both rows of a player are removed,
                     * the player can be assigned as the winner, which is DISABLED here.
                     */
                    rootService.gameService.revealAllCards()
                    rootService.gameService.removeIdenticalRows()

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














