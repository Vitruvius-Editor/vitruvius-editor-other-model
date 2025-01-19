package tools.vitruv.vitruvAdapter.vitruv.api

/**
 * A window with a name and content. This class is used to represent a window in the frontend, but
 * where the content is not serialized yet.
 *
 * @author uhsab
 */
class Window<E> (val name: String, val content: E)
