package io.github.zeyu.sixcardgolf.view

import io.github.zeyu.sixcardgolf.entity.Player
import io.github.zeyu.sixcardgolf.service.CardImageLoader
import io.github.zeyu.sixcardgolf.service.RootService
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


/**
 * The End Menu.
 */
class SCGResultsScene(private val rootService: RootService) : MenuScene(width = 500, height = 900), Refreshable {

    private fun placePlayers(): List<Player> {

        val orderedPlayers: MutableList<Player> = mutableListOf()

        val currentGame = rootService.currentGame
        requireNotNull(currentGame) {"Current game not available!"}
        val players = currentGame.players
        val winner = currentGame.winningPlayer

        if (winner != null) {
            orderedPlayers.add(winner)

            val otherOrderedPlayers = players.filter { it != winner }.sortedBy { it.deckScore }

            orderedPlayers.addAll(otherOrderedPlayers)


        } else {
            orderedPlayers.addAll(players.sortedBy { it.deckScore } )
        }

        return orderedPlayers
    }




    // // The headline of the scene.
    // private val headlineLabel = Label(
    //     width = 500, height = 50,
    //     posX = 0, posY = 0,
    //     text = "Game End",
    //     font = Font(size = 35)
    // )

    //------------------------------------------------------------------------------------------------------------------
    private val placeLabelOffset = 10
    private val allComponentsOffsetY = 40

    // private val firstPlaceLabel = Label(
    //     width = 500, height = 50,
    //     posX = 0, posY = 50 + placeLabelOffset + allComponentsOffsetY,
    //     text = "Winner:",
    //     font = Font(size = 25, color = Color.ORANGE, fontWeight = Font.FontWeight.SEMI_BOLD)
    // )

    private val firstPlacePlayer = Label(
        width = 500, height = 100,
        posX = 0, posY = 100 + allComponentsOffsetY,
        text = "",
        font = Font(size = 17)
    )
    //------------------------------------------------------------------------------------------------------------------
    private val secondPlaceLabel = Label(
        width = 500, height = 50,
        posX = 0, posY = 200 + placeLabelOffset + allComponentsOffsetY,
        text = "2nd Place:",
        font = Font(size = 25, color = Color.MAGENTA, fontWeight = Font.FontWeight.SEMI_BOLD)
    )

    private val secondPlacePlayer = Label(
        width = 500, height = 100,
        posX = 0, posY = 250 + allComponentsOffsetY,
        text = "",
        font = Font(size = 17)
    )
    //------------------------------------------------------------------------------------------------------------------
    private val thirdPlaceLabel = Label(
        width = 500, height = 50,
        posX = 0, posY = 350 + placeLabelOffset + allComponentsOffsetY,
        text = "3rd Place:",
        font = Font(size = 25, color = Color.BLUE, fontWeight = Font.FontWeight.SEMI_BOLD)
    )

    private val thirdPlacePlayer = Label(
        width = 500, height = 100,
        posX = 0, posY = 400 + allComponentsOffsetY,
        text = "",
        font = Font(size = 17)
    )
    //------------------------------------------------------------------------------------------------------------------
    private val fourthPlaceLabel = Label(
        width = 500, height = 50,
        posX = 0, posY = 500 + placeLabelOffset + allComponentsOffsetY,
        text = "4th Place:",
        font = Font(size = 25, color = Color.YELLOW, fontWeight = Font.FontWeight.SEMI_BOLD)
    )

    private val fourthPlacePlayer = Label(
        width = 500, height = 100,
        posX = 0, posY = 550 + allComponentsOffsetY,
        text = "",
        font = Font(size = 17)
    )
    //------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------
    private val buttonsPosOffsetY = 70

    val restartButton = Button(
        width = 200, height = 75,
        posX = 150, posY = 542.5 + 100 + buttonsPosOffsetY,
        text = "REPLAY",
        font = Font(size = 30, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL),
        visual = ColorVisual(131, 184, 24).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
    }

    val exitButton = Button(
        width = 150, height = 56.25,
        posX = 175, posY = 542.5 + 200 + buttonsPosOffsetY,
        text = "EXIT",
        font = Font(size = 22.5, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL),
        visual = ColorVisual(197, 0, 45).apply {
            style = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
        }
    ).apply {
        componentStyle = "-fx-background-radius: $BUTTON_BACKGROUND_RADIUS;"
    }
    //------------------------------------------------------------------------------------------------------------------




    init {

        val image : BufferedImage = ImageIO.read(CardImageLoader::class.java.getResource("/result_background.png"))
        this.background = ImageVisual(image)

        opacity = 1.0

    }

    override fun refreshAfterGameEnd() {
        val orderedPlayers = placePlayers()

        // The List to store label-components to display [Player.toString]
        val playerLabels: MutableList<Label> = mutableListOf()

        when(orderedPlayers.size) {

            2 -> {
                addComponents(

                    firstPlacePlayer,
                    secondPlaceLabel, secondPlacePlayer,

                    restartButton, exitButton
                )

                playerLabels.add(firstPlacePlayer)
                playerLabels.add(secondPlacePlayer)
            }

            3 -> {
                addComponents(

                    firstPlacePlayer,
                    secondPlaceLabel, secondPlacePlayer,
                    thirdPlaceLabel, thirdPlacePlayer,

                    restartButton, exitButton
                )

                playerLabels.add(firstPlacePlayer)
                playerLabels.add(secondPlacePlayer)
                playerLabels.add(thirdPlacePlayer)
            }

            4 -> {
                addComponents(

                    firstPlacePlayer,
                    secondPlaceLabel, secondPlacePlayer,
                    thirdPlaceLabel, thirdPlacePlayer,
                    fourthPlaceLabel, fourthPlacePlayer,

                    restartButton, exitButton
                )

                playerLabels.add(firstPlacePlayer)
                playerLabels.add(secondPlacePlayer)
                playerLabels.add(thirdPlacePlayer)
                playerLabels.add(fourthPlacePlayer)


            }
        }

        // Display the results for every player
        for (i in orderedPlayers.indices) {
            playerLabels[i].text = orderedPlayers[i].toString()
        }


    }

}

