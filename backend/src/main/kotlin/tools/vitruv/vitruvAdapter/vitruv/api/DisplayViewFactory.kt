package tools.vitruv.vitruvAdapter.vitruv.api

import tools.vitruv.vitruvAdapter.vitruv.impl.DisplayViewRepository

/**
 * Factory for creating a [DisplayViewRepository].
 *
 */
abstract class DisplayViewRepositoryFactory {
    abstract fun createDisplayViewRepository(): DisplayViewRepository
}