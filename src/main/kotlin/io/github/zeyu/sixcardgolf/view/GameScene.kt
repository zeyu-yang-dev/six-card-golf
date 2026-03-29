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

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Represents the state of a turn, mainly for UI.
     */
    enum class StateOfUI {
        TURN_START, // the turn just started or a reveal-action was made
        HAS_DRAWN,
        HAS_DRAWN_DISCARDED,
        HAS_DISCARDED,
        GAME_END
    }

    private var state = StateOfUI.TURN_START



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
    
    // The coordinates of the center of the six-card-deck of the player on the left:
    private val leftPlayerCenterX = 400
    private val leftPlayerCenterY = 450

    // There are 4 labels for player status:
    // 1. leftPlayerLabel, which shows "Player Name:"
    // 2. leftPlayerName
    // 3. leftPlayerLable2, which shows "Visible Score:"
    // 4. leftPlayerScore
    // Of which 1 and 3 are fix, 2 and 4 are changeable and thus need to be refreshed

    // leftPlayerLabel is the first label shown for the player status:
    private val leftPlayerLabel: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = leftPlayerCenterX + playerLabelOffsetX - leftPlayerStatusOffsetX,
        posY = leftPlayerCenterY - playerLabelOffsetY,
        text = "Player Name:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT,
        visual = ColorVisual.BLUE
    )

    private val leftPlayerName: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = leftPlayerCenterX + playerLabelOffsetX - leftPlayerStatusOffsetX,
        posY = leftPlayerCenterY - playerLabelOffsetY + offsetOneLine,
        text = "Cold Sheep",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val leftPlayerLabel2: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = leftPlayerCenterX + playerLabelOffsetX - leftPlayerStatusOffsetX,
        posY = leftPlayerCenterY - playerLabelOffsetY + 3 * offsetOneLine,
        text = "Visible Score:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val leftPlayerScore: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = leftPlayerCenterX + playerLabelOffsetX - leftPlayerStatusOffsetX,
        posY = leftPlayerCenterY - playerLabelOffsetY + 4 * offsetOneLine,
        text = "10000",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    //-----------------------------------------------------
    private val leftPlayerCard1 = CardView(
        posX = leftPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = leftPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val leftPlayerCard2 = CardView(
        posX = leftPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = leftPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.TWO)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val leftPlayerCard3 = CardView(
        posX = leftPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = leftPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.THREE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

     private val leftPlayerCard4 = CardView(
        posX = leftPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = leftPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FOUR)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val leftPlayerCard5 = CardView(
        posX = leftPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = leftPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FIVE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val leftPlayerCard6 = CardView(
        posX = leftPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = leftPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.SIX)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    //------------------------------------------------------------------------------------------------------------------
    // The coordinates of the center of the six-card-deck of the player on the right:
    private val rightPlayerCenterX = 1200
    private val rightPlayerCenterY = 450
    
    private val rightPlayerLabel: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = rightPlayerCenterX - playerLabelOffsetX,
        posY = rightPlayerCenterY - playerLabelOffsetY,
        text = "Player Name:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val rightPlayerName: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = rightPlayerCenterX - playerLabelOffsetX,
        posY = rightPlayerCenterY - playerLabelOffsetY + offsetOneLine,
        text = "Alex",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val rightPlayerLabel2: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = rightPlayerCenterX - playerLabelOffsetX,
        posY = rightPlayerCenterY - playerLabelOffsetY + 3 * offsetOneLine,
        text = "Visible Score:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val rightPlayerScore: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = rightPlayerCenterX - playerLabelOffsetX,
        posY = rightPlayerCenterY - playerLabelOffsetY + 4 * offsetOneLine,
        text = "10000",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )
    
    //-----------------------------------------------------
    private val rightPlayerCard1 = CardView(
        posX = rightPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = rightPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val rightPlayerCard2 = CardView(
        posX = rightPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = rightPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.TWO)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val rightPlayerCard3 = CardView(
        posX = rightPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = rightPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.THREE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

     private val rightPlayerCard4 = CardView(
        posX = rightPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = rightPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FOUR)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val rightPlayerCard5 = CardView(
        posX = rightPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = rightPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FIVE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val rightPlayerCard6 = CardView(
        posX = rightPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = rightPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.SIX)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }
    
    //------------------------------------------------------------------------------------------------------------------
    // The coordinates of the center of the six-card-deck of the player on the top:
    private val topPlayerCenterX = 800
    private val topPlayerCenterY = 150

    private val topPlayerLabel: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = topPlayerCenterX - playerLabelOffsetX,
        posY = topPlayerCenterY - playerLabelOffsetY,
        text = "Player Name:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val topPlayerName: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = topPlayerCenterX - playerLabelOffsetX,
        posY = topPlayerCenterY - playerLabelOffsetY + offsetOneLine,
        text = "Alex",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val topPlayerLabel2: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = topPlayerCenterX - playerLabelOffsetX,
        posY = topPlayerCenterY - playerLabelOffsetY + 3 * offsetOneLine,
        text = "Visible Score:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val topPlayerScore: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = topPlayerCenterX - playerLabelOffsetX,
        posY = topPlayerCenterY - playerLabelOffsetY + 4 * offsetOneLine,
        text = "10000",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )


    //-----------------------------------------------------
    private val topPlayerCard1 = CardView(
        posX = topPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = topPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val topPlayerCard2 = CardView(
        posX = topPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = topPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.TWO)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val topPlayerCard3 = CardView(
        posX = topPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = topPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.THREE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

     private val topPlayerCard4 = CardView(
        posX = topPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = topPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FOUR)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val topPlayerCard5 = CardView(
        posX = topPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = topPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FIVE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    private val topPlayerCard6 = CardView(
        posX = topPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = topPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.SIX)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
    }

    //------------------------------------------------------------------------------------------------------------------
    // The coordinates of the center of the six-card-deck of the player on the bottom:
    private val bottomPlayerCenterX = 800
    private val bottomPlayerCenterY = 750

    private val bottomPlayerLabel: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = bottomPlayerCenterX - playerLabelOffsetX,
        posY = bottomPlayerCenterY - playerLabelOffsetY,
        text = "Current Player:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val bottomPlayerName: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = bottomPlayerCenterX - playerLabelOffsetX,
        posY = bottomPlayerCenterY - playerLabelOffsetY + offsetOneLine,
        text = "Alex",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val bottomPlayerLabel2: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = bottomPlayerCenterX - playerLabelOffsetX,
        posY = bottomPlayerCenterY - playerLabelOffsetY + 3 * offsetOneLine,
        text = "Visible Score:",
        font = Font(size = 25, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val bottomPlayerScore: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = bottomPlayerCenterX - playerLabelOffsetX,
        posY = bottomPlayerCenterY - playerLabelOffsetY + 4 * offsetOneLine,
        text = "10000",
        font = Font(size = 25, color = changeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )



    //-----------------------------------------------------
    private val bottomPlayerCard1 = CardView(
        posX = bottomPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = bottomPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) playerActionService.revealAction(0)
            if (state == StateOfUI.HAS_DRAWN || state == StateOfUI.HAS_DRAWN_DISCARDED) playerActionService.swap(0)
        }
    }

    private val bottomPlayerCard2 = CardView(
        posX = bottomPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = bottomPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.TWO)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) playerActionService.revealAction(1)
            if (state == StateOfUI.HAS_DRAWN || state == StateOfUI.HAS_DRAWN_DISCARDED) playerActionService.swap(1)
        }
    }

    private val bottomPlayerCard3 = CardView(
        posX = bottomPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = bottomPlayerCenterY - verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.THREE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) playerActionService.revealAction(2)
            if (state == StateOfUI.HAS_DRAWN || state == StateOfUI.HAS_DRAWN_DISCARDED) playerActionService.swap(2)
        }
    }

     private val bottomPlayerCard4 = CardView(
        posX = bottomPlayerCenterX - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = bottomPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FOUR)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) playerActionService.revealAction(3)
            if (state == StateOfUI.HAS_DRAWN || state == StateOfUI.HAS_DRAWN_DISCARDED) playerActionService.swap(3)
        }
    }

    private val bottomPlayerCard5 = CardView(
        posX = bottomPlayerCenterX - cardWidth / 2 - scaleOffsetX,
        posY = bottomPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.FIVE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) playerActionService.revealAction(4)
            if (state == StateOfUI.HAS_DRAWN || state == StateOfUI.HAS_DRAWN_DISCARDED) playerActionService.swap(4)
        }
    }

    private val bottomPlayerCard6 = CardView(
        posX = bottomPlayerCenterX + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = bottomPlayerCenterY + verticalDistanceInDeck - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.SIX)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) playerActionService.revealAction(5)
            if (state == StateOfUI.HAS_DRAWN || state == StateOfUI.HAS_DRAWN_DISCARDED) playerActionService.swap(5)
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    //-------- Define the components for the Handcard, Drawstack and Discardstack --------------------------------------
    //------------------------------------------------------------------------------------------------------------------

    private val handCard = CardView(
        posX = 800 - horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = 450 - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        this.isDisabled = true
        this.opacity = 0.5
    }

    private val drawStackCard = CardView(
        posX = 800 - cardWidth / 2 - scaleOffsetX,
        posY = 450 - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) {
                playerActionService.drawCardAction()
                state = StateOfUI.HAS_DRAWN
            }
        }
    }

    private val discardStackCard = CardView(
        posX = 800 + horizontalDistanceInDeck - cardWidth / 2 - scaleOffsetX,
        posY = 450 - cardHeight / 2 - scaleOffsetY,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage)
    ).apply {
        this.showFront()
        this.scale(cardScale)
        onMouseClicked = {
            if (state == StateOfUI.TURN_START) {
                playerActionService.drawDiscardedCardAction()
                state = StateOfUI.HAS_DRAWN_DISCARDED
            }
            if (state == StateOfUI.HAS_DRAWN) {
                playerActionService.discard(true)
                state = StateOfUI.TURN_START
            }
        }
    }

    private val middleLabel: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = 680,
        posY = 502,
        text = "   Hand             Draw          Discard",
        font = Font(size = 15, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER_LEFT
    )

    private val statusLabel: Label = Label(
        height = playerLabelHeight,
        width = playerLabelWidth,
        posX = 650,
        posY = 345,
        text = "This is a status bar.",
        font = Font(size = 15, color = unchangeableLabelColor, fontWeight = Font.FontWeight.SEMI_BOLD),
        alignment = Alignment.CENTER
    )
    //------------------------------------------------------------------------------------------------------------------

    private val leftPlayerCards: List<CardView> =
        listOf(leftPlayerCard1, leftPlayerCard2, leftPlayerCard3, leftPlayerCard4, leftPlayerCard5, leftPlayerCard6)
    private val rightPlayerCards: List<CardView> =
        listOf(rightPlayerCard1, rightPlayerCard2, rightPlayerCard3,
            rightPlayerCard4, rightPlayerCard5, rightPlayerCard6)
    private val topPlayerCards: List<CardView> =
        listOf(topPlayerCard1, topPlayerCard2, topPlayerCard3, topPlayerCard4, topPlayerCard5, topPlayerCard6)
    private val bottomPlayerCards: List<CardView> =
        listOf(bottomPlayerCard1, bottomPlayerCard2, bottomPlayerCard3,
            bottomPlayerCard4, bottomPlayerCard5, bottomPlayerCard6)

    private val leftPlayerLabels: List<Label> =
        listOf(leftPlayerName, leftPlayerScore, leftPlayerLabel, leftPlayerLabel2)
    private val rightPlayerLabels: List<Label> =
        listOf(rightPlayerName, rightPlayerScore, rightPlayerLabel, rightPlayerLabel2)
    private val topPlayerLabels: List<Label> =
        listOf(topPlayerName, topPlayerScore, topPlayerLabel, topPlayerLabel2)
    private val bottomPlayerLabels: List<Label> =
        listOf(bottomPlayerName, bottomPlayerScore, bottomPlayerLabel, bottomPlayerLabel2)

    private val middleCards: List<CardView> = listOf(handCard, drawStackCard, discardStackCard)
    //------------------------------------------------------------------------------------------------------------------


    private fun refreshOnePlayer(playerObject: Player, playerCards: List<CardView>, playerName: Label, playerScore: Label) {
        // for tracking players with color:
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val players = currentGame.players
        val colors = listOf(Color.CYAN, Color.YELLOW, Color.LIGHT_GRAY, Color.MAGENTA)


        val cards = playerObject.topRow + playerObject.bottomRow

        for (i in 0..5) {
            // refresh the six deck-cards:
            // refresh the six card-components one by one according to actual player deck-card:
            playerCards[i].apply {
                cards[i]?.let { card ->
                    // if cards[i] is not null:
                    frontVisual = ImageVisual(cardImageLoader.frontImageFor(card.cardSuit, card.cardValue))
                    if (card.isRevealed) this.showFront() else this.showBack()
                    isVisible = true
                } ?: run {
                    // if cards[i] is null:
                    isVisible = false
                }
            }
            // refresh player name:
            playerName.text = playerObject.playerName
            playerName.font =
                Font(size = 25, color = colors[players.indexOf(playerObject)], fontWeight = Font.FontWeight.SEMI_BOLD)
            // refresh player score:
            playerScore.text = "${playerObject.deckScore}"
            playerScore.font =
                Font(size = 25, color = colors[players.indexOf(playerObject)], fontWeight = Font.FontWeight.SEMI_BOLD)
        }

    }

    /**
     * Refreshes six cards, name and score for players on every position.
     */
    private fun refreshPlayers() {

        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}

        val players = currentGame.players
        val currentPlayerIndex = currentGame.currentPlayerIndex

        when(currentGame.players.size) {

            2 -> {
                val bottomPlayer = players[currentPlayerIndex]
                val topPlayer = players[(currentPlayerIndex + 1) % 2]

                refreshOnePlayer(bottomPlayer, bottomPlayerCards, bottomPlayerName, bottomPlayerScore)
                refreshOnePlayer(topPlayer, topPlayerCards, topPlayerName, topPlayerScore)
            }

            3 -> {
                val bottomPlayer = players[currentPlayerIndex]
                val rightPlayer = players[(currentPlayerIndex + 1) % 3]
                val topPlayer = players[(currentPlayerIndex + 2) % 3]

                refreshOnePlayer(bottomPlayer, bottomPlayerCards, bottomPlayerName, bottomPlayerScore)
                refreshOnePlayer(topPlayer, topPlayerCards, topPlayerName, topPlayerScore)
                refreshOnePlayer(rightPlayer, rightPlayerCards, rightPlayerName, rightPlayerScore)
            }

            4 -> {
                val bottomPlayer = players[currentPlayerIndex]
                val rightPlayer = players[(currentPlayerIndex + 1) % 4]
                val topPlayer = players[(currentPlayerIndex + 2) % 4]
                val leftPlayer = players[(currentPlayerIndex + 3) % 4]

                refreshOnePlayer(bottomPlayer, bottomPlayerCards, bottomPlayerName, bottomPlayerScore)
                refreshOnePlayer(leftPlayer, leftPlayerCards, leftPlayerName, leftPlayerScore)
                refreshOnePlayer(topPlayer, topPlayerCards, topPlayerName, topPlayerScore)
                refreshOnePlayer(rightPlayer, rightPlayerCards, rightPlayerName, rightPlayerScore)
            }

        }


    }

    private fun refreshMiddle() {

        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val players = currentGame.players
        val currentPlayerIndex = currentGame.currentPlayerIndex
        val currentPlayer = players[currentPlayerIndex]

        // refresh handcard:
        if (currentPlayer.handCard != null) {
            val currentHandCard = currentPlayer.handCard
            handCard.apply {
                frontVisual =
                    ImageVisual(cardImageLoader.frontImageFor(currentHandCard!!.cardSuit, currentHandCard.cardValue))
                this.showFront()
            }
        } else {
            handCard.apply {
                frontVisual = ImageVisual(cardImageLoader.blankImage)
                this.showFront()
            }
        }

        // refresh the card representing draw-stack:
        val drawStack = currentGame.drawStack
        if (!drawStack.isEmpty()) {
            drawStackCard.apply {
                frontVisual = ImageVisual(cardImageLoader.backImage)
                this.showFront()
            }
        } else {
            drawStackCard.apply {
                frontVisual = ImageVisual(cardImageLoader.blankImage)
                this.showFront()
            }
        }

        // refresh the card representing discard-stack:
        val discardStack = currentGame.discardStack
        if (!discardStack.isEmpty()) {
            val cardOnTopOfDiscardStack = discardStack.peek()
            discardStackCard.apply {
                frontVisual = ImageVisual(cardImageLoader.frontImageFor(cardOnTopOfDiscardStack!!.cardSuit,
                                                                        cardOnTopOfDiscardStack.cardValue))
                this.showFront()
            }
        } else {
            discardStackCard.apply {
                frontVisual = ImageVisual(cardImageLoader.blankImage)
                this.showFront()
            }
        }
    }


    /**
     * Enables draw-stack, discard-stack and deck-cards, except the null deck-cards.
     * When a component is enabled, its opacity will be set to 1.0, which means totally not transparent, to indicate
     * that this component is interactive.
     */
    private fun enableAllComponents() {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}

        val players = currentGame.players
        val currentPlayerIndex = currentGame.currentPlayerIndex
        val currentPlayer = players[currentPlayerIndex]
        val cards = currentPlayer.topRow + currentPlayer.bottomRow

        for (i in 0..5) {
            // enable all deck-cards that are not null
            if (cards[i] != null) {
                bottomPlayerCards[i].isDisabled = false
                bottomPlayerCards[i].opacity = 1.0
            }
        }

        drawStackCard.isDisabled = false
        drawStackCard.opacity = 1.0
        discardStackCard.isDisabled = false
        discardStackCard.opacity = 1.0
    }

    /**
     * Disables all components that are not supposed to be interactive in a specific phase of a turn.
     * Uses an opacity of 0.5 to show the components that are not supposed to be clicked upon.
     * Displays reminder for player via status bar.
     */
    private fun guideUserBehavior() {
        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val players = currentGame.players
        val currentPlayerIndex = currentGame.currentPlayerIndex
        val currentPlayer = players[currentPlayerIndex]
        val cards = currentPlayer.topRow + currentPlayer.bottomRow

        // Firstly turn on all components:
        enableAllComponents()

        // Then disable components, which are not supposed to be interactive:

        // Condition 1: In the first round
        if (currentGame.isFirstRound) {
            drawStackCard.isDisabled = true
            drawStackCard.opacity = 0.5
            discardStackCard.isDisabled = true
            discardStackCard.opacity = 0.5


            for (i in 0..5) {
                // enable all deck-cards that are not null
                if (cards[i]?.isRevealed == true) {
                    bottomPlayerCards[i].isDisabled = true
                    bottomPlayerCards[i].opacity = 0.5
                }
            }

            statusLabel.text = "First Round: Please reveal 2 cards."
            statusLabel.font = Font(size = 15, color = Color.GREEN, fontWeight = Font.FontWeight.SEMI_BOLD)
        }

        // Condition 2: Not in the first round and a turn just started
        if (state == StateOfUI.TURN_START) {
            for (i in 0..5) {
                // enable all deck-cards that are not null
                if (cards[i]?.isRevealed == true) {
                    bottomPlayerCards[i].isDisabled = true
                    bottomPlayerCards[i].opacity = 0.5
                }
            }
        }

        // Condition 3: Just drawn from draw-stack
        if (state == StateOfUI.HAS_DRAWN) {
            drawStackCard.isDisabled = true
            drawStackCard.opacity = 0.5
        }

        // Condition 4: Just drawn from discard-stack
        if (state == StateOfUI.HAS_DRAWN_DISCARDED) {
            discardStackCard.isDisabled = true
            discardStackCard.opacity = 0.5
            drawStackCard.isDisabled = true
            drawStackCard.opacity = 0.5
        }

        // Condition 5: Just discarded the handcard, which was drawn from draw-stack
        if (state == StateOfUI.HAS_DISCARDED) {
            drawStackCard.isDisabled = true
            drawStackCard.opacity = 0.5
            discardStackCard.isDisabled = true
            discardStackCard.opacity = 0.5

            for (i in 0..5) {
                // enable all deck-cards that are not null
                if (cards[i]?.isRevealed == true) {
                    bottomPlayerCards[i].isDisabled = true
                    bottomPlayerCards[i].opacity = 0.5
                }
            }
        }

        // Condition 6: When game ends, disable all components:
        if (state == StateOfUI.GAME_END) {
            drawStackCard.isDisabled = true
            drawStackCard.opacity = 0.5
            discardStackCard.isDisabled = true
            discardStackCard.opacity = 0.5

            for (i in 0..5) {
                bottomPlayerCards[i].isDisabled = true
                bottomPlayerCards[i].opacity = 0.5
            }
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
        // super.refreshAfterStartNewGame()

        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}

        // Add Components according to the number of players:
        // Notice that if restartButton is pressed, this action will be taken once more,
        // to avoid exceptions, all components have to be removed after game ends

        //--------------------------------------------------------------------------------------------------------------
        
        // // The performance of this way is better compared with the following when.
        // when(currentGame.players.size) {
        //
        //     2 -> {
        //         addComponents(
        //             *topPlayerLabels.toTypedArray(),
        //             *topPlayerCards.toTypedArray(),
        //             *bottomPlayerLabels.toTypedArray(),
        //             *bottomPlayerCards.toTypedArray(),
        //             *middleCards.toTypedArray(),
        //             middleLabel,
        //             statusLabel
        //         )
        //     }
        //
        //     3 -> {
        //         addComponents(
        //             *topPlayerLabels.toTypedArray(),
        //             *topPlayerCards.toTypedArray(),
        //             *bottomPlayerLabels.toTypedArray(),
        //             *bottomPlayerCards.toTypedArray(),
        //             *rightPlayerLabels.toTypedArray(),
        //             *rightPlayerCards.toTypedArray(),
        //             *middleCards.toTypedArray(),
        //             middleLabel,
        //             statusLabel
        //         )
        //     }
        //
        //     4 -> {
        //         addComponents(
        //             *topPlayerLabels.toTypedArray(),
        //             *topPlayerCards.toTypedArray(),
        //             *bottomPlayerLabels.toTypedArray(),
        //             *bottomPlayerCards.toTypedArray(),
        //             *rightPlayerLabels.toTypedArray(),
        //             *rightPlayerCards.toTypedArray(),
        //             *leftPlayerLabels.toTypedArray(),
        //             *leftPlayerCards.toTypedArray(),
        //             *middleCards.toTypedArray(),
        //             middleLabel,
        //             statusLabel
        //         )
        //     }
        // }

        //--------------------------------------------------------------------------------------------------------------

        // Create a list to store components to add to this scene
        val components = mutableListOf<ComponentView>()

        // Add common Components
        components.addAll(topPlayerLabels)
        components.addAll(topPlayerCards)
        components.addAll(bottomPlayerLabels)
        components.addAll(bottomPlayerCards)
        components.addAll(middleCards)
        components.add(middleLabel)
        components.add(statusLabel)

        when (currentGame.players.size) {
            2 -> {
                // 2 Players: Only use common components
                for (i in components.indices) {
                    addComponents(components[i])
                }
            }
            3 -> {
                // 3 Players:
                components.addAll(rightPlayerLabels)
                components.addAll(rightPlayerCards)
                for (i in components.indices) {
                    addComponents(components[i])
                }
            }
            4 -> {
                // 4 Players:
                components.addAll(rightPlayerLabels)
                components.addAll(rightPlayerCards)
                components.addAll(leftPlayerLabels)
                components.addAll(leftPlayerCards)
                for (i in components.indices) {
                    addComponents(components[i])
                }
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        state = StateOfUI.TURN_START

        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()


    }


    override fun refreshAfterReveal() {
        // super.refreshAfterReveal()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()
    }

    override fun refreshAfterNextTurn() {

        // before the delay animation, show a reminder via status bar:
        statusLabel.text = "The seats will rotate in 2 seconds."
        statusLabel.font = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)

        // before refresh for the next turn, play a delay animation
        this.playAnimation(
            DelayAnimation(duration = 2000).apply {
                onFinished = {

                    // clear the status bar before next turn begins:
                    statusLabel.text = "Your turn: Draw a card or reveal one."
                    statusLabel.font = Font(size = 15, color = Color.GREEN, fontWeight = Font.FontWeight.SEMI_BOLD)

                    state = StateOfUI.TURN_START

                    // super.refreshAfterNextTurn()
                    refreshPlayers()
                    refreshMiddle()
                    guideUserBehavior()
                }
            }
        )


    }

    override fun refreshOnLastRound() {
        // super.refreshOnLastRound()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()
    }

    override fun refreshAfterDrawCard() {
        state = StateOfUI.HAS_DRAWN

        // super.refreshAfterDrawCard()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()

        statusLabel.text = "Choose a card to swap or discard hand."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
    }

    override fun refreshAfterDrawDiscardedCard() {
        state = StateOfUI.HAS_DRAWN_DISCARDED

        // super.refreshAfterDrawDiscardedCard()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()

        statusLabel.text = "Choose a card to swap."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
    }

    override fun refreshAfterDiscard() {
        statusLabel.text = "You have to reveal a card to continue."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)

        state = StateOfUI.HAS_DISCARDED

        // super.refreshAfterDiscard()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()
    }

    override fun refreshAfterSwap() {
        // super.refreshAfterSwap()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()
    }

    override fun refreshAfterFirstReveal() {
        // super.refreshAfterFirstReveal()
        refreshPlayers()
        refreshMiddle()
        guideUserBehavior()

        statusLabel.text = "Reveal one more card to continue."
        statusLabel.font = Font(size = 15, color = Color.WHITE, fontWeight = Font.FontWeight.SEMI_BOLD)
    }




    override fun refreshBeforeGameEnd() {
        state = StateOfUI.GAME_END
        guideUserBehavior()

        // before the delay animation, show a reminder via status bar:
        statusLabel.text = "All cards will be revealed in 3 seconds."
        statusLabel.font = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)

        this.playAnimation(
            DelayAnimation(duration = 3000).apply {
                onFinished = {

                    revealAllCards()
                    removeIdenticalRow()

                    refreshPlayers()
                    refreshMiddle()

                    statusLabel.text = "Show result in 5 seconds."
                    statusLabel.font = Font(size = 15, color = Color.PINK, fontWeight = Font.FontWeight.SEMI_BOLD)
                    playAnimation(
                        DelayAnimation(duration = 5000).apply {
                            onFinished = {

                                refreshPlayers()
                                refreshMiddle()

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
        )


        println(leftPlayerCard1.posX - (leftPlayerLabel.posX + leftPlayerLabel.width))
        println(leftPlayerLabel.actualPosY)
        println(leftPlayerName.actualPosY)
        println(PPL_HEIGHT)

    }
}














