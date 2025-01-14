package tools.vitruv.vitruvAdapter.vitruv.impl.exception

class DisplayViewException(message: String) : Exception(message) {
    constructor(message: String, cause: Throwable) : this(message) {
        initCause(cause)
    }
}