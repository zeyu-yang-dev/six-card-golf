package io.github.zeyuyangdev.sixcardgolf.service

/**
 * Abstract service class that manages multiple [Refreshable] instances,
 * (usually UI elements, such as specialized [tools.aqua.bgw.core.BoardGameScene] classes/instances)
 * which are notified of changes to refresh via the [onAllRefreshables] method.
 */
abstract class AbstractRefreshingService {

    private val refreshables = mutableListOf<Refreshable>()

    /**
     * Adds a [Refreshable] to the list that gets called
     * whenever [onAllRefreshables] is used.
     */
    fun addRefreshable(newRefreshable : Refreshable) {
        refreshables += newRefreshable
    }

    /**
     * Executes the passed method (usually a lambda) on all
     * [Refreshable]s registered with the service class that
     * extends this [AbstractRefreshingService]
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }

}