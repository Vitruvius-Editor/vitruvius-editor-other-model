package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.vitruvAdapter.vitruv.impl.DisplayViewRepository

/**
 * Factory for creating a [DisplayViewRepository].
 */
abstract class DisplayViewRepositoryFactory {

    /**
     * Creates a new [DisplayViewRepository] with the given [DisplayView]s.
     */
    abstract fun createDisplayViewRepository(): DisplayViewRepository
}