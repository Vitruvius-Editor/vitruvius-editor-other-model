package tools.vitruv.vitruvAdapter.vitruv.connection

import tools.vitruv.framework.remote.client.VitruvClient
import tools.vitruv.framework.views.ViewProvider
import tools.vitruv.framework.views.ViewTypeProvider
import tools.vitruv.vitruvAdapter.model.Project
import java.nio.file.Path

class ServerConnection(var project: Project) : tools.vitruv.vitruvAdapter.vitruv.connection.Connection {
    lateinit var vitruviusClient: VitruvClient
    init {
        // Implement server connection here.
        TODO("Not yet implemented")
    }
    override fun getViewProvider(): ViewProvider {
        TODO("Not yet implemented")
    }

    override fun getViewTypeProvider(): ViewTypeProvider {
        TODO("Not yet implemented")
    }

    override fun isConnected(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getIp(): String {
        TODO("Not yet implemented")
    }

    override fun getPort(): Int {
        TODO("Not yet implemented")
    }

    override fun getProtocol(): String {
        TODO("Not yet implemented")
    }

    override fun getTemporaryPath(): Path {
        TODO("Not yet implemented")
    }
}