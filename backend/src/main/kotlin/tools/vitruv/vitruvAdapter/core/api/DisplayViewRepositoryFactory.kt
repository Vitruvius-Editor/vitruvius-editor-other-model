package tools.vitruv.vitruvAdapter.core.api

import tools.vitruv.vitruvAdapter.core.impl.DisplayViewRepository

/**
 * Factory for creating a [DisplayViewRepository].
 */
abstract class DisplayViewRepositoryFactory {

    /**
     * Creates a new [DisplayViewRepository] with the given [DisplayView]s.
     */
    abstract fun createDisplayViewRepository(): DisplayViewRepository
}