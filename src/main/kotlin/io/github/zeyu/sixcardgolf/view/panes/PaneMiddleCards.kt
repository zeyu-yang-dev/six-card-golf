package io.github.zeyu.sixcardgolf.view.panes

import io.github.zeyu.sixcardgolf.entity.*
import io.github.zeyu.sixcardgolf.service.*
import io.github.zeyu.sixcardgolf.view.*

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class PaneMiddleCards(
    private val rootService: RootService,
    private val gameScene: GameScene
) : Pane<ComponentView>(
    PMC_POS_X,
    PMC_POS_Y,
    PMC_WIDTH,
    PMC_HEIGHT,
    visual = PLAYER_PANE_BG_VISUAL
), Refreshable {





}