package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.entity.*

import io.github.zeyu.sixcardgolf.service.RootService
import io.github.zeyu.sixcardgolf.service.CardImageLoader

import io.github.zeyu.sixcardgolf.view.panes.*

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


/**
 * The game scene for the six card golf game, which inherits from [BoardGameScene].
 *
 */
class GameScene(private val rootService: RootService) : BoardGameScene(1600, 900), Refreshable {



    private val cardImageLoader = CardImageLoader()

    private val playerActionService = rootService.playerActionService

    val panePlayerLeft = PanePlayerLeft(rootService, this)
    val panePlayerRight = PanePlayerRight(rootService, this)
    val panePlayerTop = PanePlayerTop(rootService, this)
    val panePlayerBottom = PanePlayerBottom(rootService, this)
    val paneMiddleCards = PaneMiddleCards(rootService, this)

    var state = StateOfUI.TURN_START

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Represents the state of UI, mainly to control interactivity of components.
     */
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
    //-------- Define the components for status and deck-cards for players on every position ---------------------------
    //------------------------------------------------------------------------------------------------------------------
    // The parameters for positioning deck-cards and status-lines for all players:

    // Positioning setup for deck-cards of all players:
    private val cardScale = 0.65

    // The distance from the deck-center and the center of a card:
    private val horizontalDistanceInDeck = 90
    private val verticalDistanceInDeck = 67

    // The actual card size
    // The 4 parameters below are for [cardScale] = 0.65
    // The actual size of a card:
    private val cardWidth = 130 * cardScale
    private val cardHeight = 200 * cardScale
    // Offset for positioning due to the scaling of a card:
    private val scaleOffsetX = 22.75
    private val scaleOffsetY = 35

    // Offset for positioning for player name label:
    private val playerLabelOffsetX = -180
    private val playerLabelOffsetY = 85
    // Size of player status labels:
    private val playerLabelHeight = 50
    private val playerLabelWidth = 300

    // The distance between lines in player status display:
    private val offsetOneLine = 30

    // Offset for positioning the status labels of the left player:
    // Increase this value will move the status labels to left.
    private val leftPlayerStatusOffsetX = 140

    // Colors for status labels:
    private val unchangeableLabelColor = Color.WHITE
    private val changeableLabelColor = Color.CYAN

    //------------------------------------------------------------------------------------------------------------------
    // The components for one player, including:
    // 4 labels to show status, and
    // 6 deck-cards



    //------------------------------------------------------------------------------------------------------------------





    private val statusLabel: Label = Label(
        posX = 650,
        posY = 345,
        width = playerLabelWidth,
        height = playerLabelHeight,


        text = "This is a status bar.",
        font = Font(size = 15, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER
    )
    //------------------------------------------------------------------------------------------------------------------





    //------------------------------------------------------------------------------------------------------------------











    /**
     * Disables all components that are not supposed to be interactive in a specific phase of a turn.
     * Uses an opacity of 0.5 to show the components that are not supposed to be clicked upon.
     * Displays reminder for player via status bar.
     */
    private fun guideUserBehavior() {
        val currentGame = rootService.currentGame




        // Condition 1: In the first round
        if (currentGame.isFirstRound) {

            statusLabel.text = "First Round: Please reveal 2 cards."
            statusLabel.font = Font(size = 15, color = Color.GREEN, fontWeight = Font.FontWeight.SEMI_BOLD)
        }


        // Condition 7: During the last round, show a reminder via status bar:
        if (currentGame.isLastRound && state == StateOfUI.TURN_START) {
            statusLabel.text = "Last Round!"
            statusLabel.font = Font(size = 15, color = Color.RED, fontWeight = Font.FontWeight.SEMI_BOLD)
        }

    }



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







    override fun refreshAfterStartNewGame() {

        state = StateOfUI.TURN_START

        guideUserBehavior()

    }

    override fun refreshAfterFirstReveal() {

        guideUserBehavior()

        statusLabel.text = "Reveal one more card to continue."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
    }


    override fun refreshAfterReveal() {

        guideUserBehavior()
    }

    override fun refreshAfterDrawCard() {

        guideUserBehavior()

        statusLabel.text = "Choose a card to swap or discard hand."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
    }

    override fun refreshAfterDrawDiscardedCard() {


        guideUserBehavior()

        statusLabel.text = "Choose a card to swap."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
    }

    override fun refreshAfterSwap() {

        guideUserBehavior()
    }

    override fun refreshAfterDiscard() {
        statusLabel.text = "You have to reveal a card to continue."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)


        guideUserBehavior()
    }

    override fun refreshAfterNextTurn() {

        // before the delay animation, show a reminder via status bar:
        statusLabel.text = "The seats will rotate in 2 seconds."
        statusLabel.font = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)

        // before refresh for the next turn, play a delay animation
        this.playAnimation(
            DelayAnimation(duration = DELAY_BTW_TURNS).apply {
                onFinished = {

                    // clear the status bar before next turn begins:
                    statusLabel.text = "Your turn: Draw a card or reveal one."
                    statusLabel.font = Font(size = 15, color = Color.GREEN, fontWeight = Font.FontWeight.SEMI_BOLD)


                    setUIState(StateOfUI.TURN_START) // 这里是必要的，从bottom中改变状态可能不及时
                    // super.refreshAfterNextTurn()


                    guideUserBehavior()
                }
            }
        )


    }

    override fun refreshOnLastRound() {

        guideUserBehavior()
    }




    override fun refreshBeforeGameEnd() {

        guideUserBehavior()

        // before the delay animation, show a reminder via status bar:
        statusLabel.text = "All cards will be revealed in 3 seconds."
        statusLabel.font = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)

        this.playAnimation(
            DelayAnimation(duration = DELAY_BEFORE_REVEAL_ALL).apply {
                onFinished = {

                    revealAllCards()
                    removeIdenticalRow()

                    // refreshPlayers()
                    // refreshMiddle()

                    statusLabel.text = "Show result in 5 seconds."
                    statusLabel.font = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)

                    playAnimation(
                        DelayAnimation(duration = 5000).apply {
                            onFinished = {

                                // refreshPlayers()
                                // refreshMiddle()

                                rootService.gameService.endGame()
                            }
                        }
                    )
                }
            }
        )
    }




    init {

        val image : BufferedImage = ImageIO.read(CardImageLoader::class.java.getResource("/game_background.jpg"))
        this.background = ImageVisual(image)

        addComponents(
            panePlayerLeft,
            panePlayerRight,
            panePlayerTop,
            panePlayerBottom,
            paneMiddleCards,
            statusLabel
        )


    }
}














