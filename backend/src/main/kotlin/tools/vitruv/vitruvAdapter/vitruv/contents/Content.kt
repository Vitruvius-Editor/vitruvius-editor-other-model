package tools.vitruv.vitruvAdapter.vitruv.contents

import tools.vitruv.vitruvAdapter.vitruv.displayViews.mapper.ViewMapper

/**
 * Interface for contents.
 * The Idea is a content is 1-1 related to a model in the Java-Uml-Pcm VSUMM.
 * The specific content classes will contain information about the model, e.g. the UML model.
 * The graphical editor interpret UML contents, Java contents and PCM contents. So the content class should contain information about their related model,
 * but already in a specific format that the graphical editor can interpret.
 * The Mapper will map views to contents and vice versa. These contents are serialized to json strings, which the graphical editor can interpret and display.
 * @see ViewMapper
 * But how this can be nicely implemented is still open. It is not even sure if a "Content" interface is the right way to go.
 *
 * @author uhsab
 */
interface Content {
    fun getContent(): String
}