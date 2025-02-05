package tools.vitruv.vitruvAdapter.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import tools.vitruv.vitruvAdapter.dto.ConnectionCreationRequest
import tools.vitruv.vitruvAdapter.dto.ConnectionEditRequest
import tools.vitruv.vitruvAdapter.dto.ConnectionResponse
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import tools.vitruv.vitruvAdapter.services.ConnectionService
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ConnectionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper;

    @MockitoBean
    private lateinit var connectionService: ConnectionService

    @Test
    fun testGetConnections() {
        val connections = setOf(
            ConnectionDetails(UUID.randomUUID(), "Connection 1", "Description 1", "https://example.com/1", 8080),
            ConnectionDetails(UUID.randomUUID(), "Connection 2", "Description 2", "https://example.com/2", 8080)
        )

        whenever(connectionService.getConnections()).thenReturn(connections)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connections"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(connections.map { ConnectionResponse(it) }))
        )
    }

    @Test
    fun testGetConnection() {
        val connection = ConnectionDetails(UUID.randomUUID(), "Connection 1", "Description 1", "https://example.com/1", 8080)

        whenever(connectionService.getConnectionById(any<UUID>())).thenAnswer {
            if (it.arguments[0] == connection.uuid) connection else throw ConnectionNotFoundException()
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/${connection.uuid}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(ConnectionResponse(connection))))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/connection/${UUID.randomUUID()}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testCreateConnection() {
        val uuid = UUID.randomUUID()
        val connectionCreationRequest = ConnectionCreationRequest("Test Connection", "Description", "https://example.com", 8080)

        whenever(connectionService.importConnection(any<ConnectionCreationRequest>())).thenAnswer {
            val arg: ConnectionCreationRequest = it.arguments[0] as ConnectionCreationRequest
            ConnectionDetails(uuid, arg.name, arg.description, arg.url, arg.port)
        }

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/connection")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(connectionCreationRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(ConnectionDetails(
                uuid,
                connectionCreationRequest.name,
                connectionCreationRequest.description,
                connectionCreationRequest.url,
                connectionCreationRequest.port
            ))))
    }

    @Test
    fun testDeleteConnection() {
        val connection = ConnectionDetails(UUID.randomUUID(), "Connection 1", "Description 1", "https://example.com/1", 8080)

        whenever(connectionService.deleteConnection(any<UUID>())).thenAnswer {
            if (it.arguments[0] == connection.uuid) connection else throw ConnectionNotFoundException()
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/connection/${connection.uuid}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("{}"))

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/connection/${UUID.randomUUID()}"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun testEditConnection() {
        val connection = ConnectionDetails(UUID.randomUUID(), "Connection 1", "Description 1", "https://example.com/1", 8080)
        val connectionEditRequest = ConnectionEditRequest("Connection 2", "Description 2", "https://example.com/2", 8080)

        whenever(connectionService.editConnection(any<UUID>(), any<ConnectionEditRequest>())).thenAnswer {
            val arg: ConnectionEditRequest = it.arguments[1] as ConnectionEditRequest
            if (it.arguments[0] == connection.uuid) {
                ConnectionDetails(connection.uuid, arg.name, arg.description, arg.url, arg.port)
            } else {
                throw ConnectionNotFoundException()
            }
        }

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/connection/${connection.uuid}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(connectionEditRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(ConnectionDetails(
                connection.uuid,
                connectionEditRequest.name,
                connectionEditRequest.description,
                connectionEditRequest.url,
                connectionEditRequest.port
            ))))

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/connection/${UUID.randomUUID()}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(connectionEditRequest)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}