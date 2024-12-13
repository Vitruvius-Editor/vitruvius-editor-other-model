package tools.vitruv.vitruvAdapter.exception

class VitruviusConnectFailedException(val errorMsg: String): Exception() {
    override val message = "Connection to Vitruvius server failed. $errorMsg"

}