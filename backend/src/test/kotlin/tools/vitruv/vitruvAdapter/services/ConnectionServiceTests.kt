package tools.vitruv.vitruvAdapter.services;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*;
import tools.vitruv.vitruvAdapter.dto.ConnectionCreationRequest
import tools.vitruv.vitruvAdapter.dto.ConnectionEditRequest
import tools.vitruv.vitruvAdapter.exception.ConnectionNotFoundException
import tools.vitruv.vitruvAdapter.model.ConnectionDetails
import tools.vitruv.vitruvAdapter.repository.ConnectionRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
class ConnectionServiceTest {

    @Mock
    private lateinit var connectionRepository: ConnectionRepository

    @InjectMocks
    private lateinit var connectionService: ConnectionService

    private lateinit var connection1: ConnectionDetails
    private lateinit var connection2: ConnectionDetails

    @BeforeEach
    fun setup() {
        connection1 = ConnectionDetails(UUID.randomUUID(), "Connection 1", "Description 1", "https://example.com/1")
        connection2 = ConnectionDetails(UUID.randomUUID(), "Connection 2", "Description 2", "https://example.com/2")
    }

    @Test
    fun testGetConnections() {
        val connections = listOf(connection1, connection2)
        whenever(connectionRepository.findAll()).thenReturn(connections)

        val result = connectionService.getConnections()

        assertEquals(2, result.size)
        assertTrue(result.contains(connection1))
        assertTrue(result.contains(connection2))
    }

    @Test
    fun testImportConnection() {
        val request = ConnectionCreationRequest("New Connection", "Description", "https://example.com/new")
        whenever(connectionRepository.save(any<ConnectionDetails>())).thenAnswer(AdditionalAnswers.returnsFirstArg<ConnectionDetails>())

        val result = connectionService.importConnection(request)

        assertEquals(request.name, result.name)
        assertEquals(request.description, result.description)
        assertEquals(request.url, result.url)
        verify(connectionRepository, times(1)).save(any<ConnectionDetails>())
    }

    @Test
    fun testDeleteConnection() {
        val connectionId = connection1.uuid
        whenever(connectionRepository.findByUuid(connectionId)).thenReturn(connection1)
        doNothing().whenever(connectionRepository).delete(connection1)

        connectionService.deleteConnection(connectionId)

        verify(connectionRepository, times(1)).findByUuid(connectionId)
        verify(connectionRepository, times(1)).delete(connection1)
    }

    @Test
    fun testEditConnection() {
        val connectionId = connection1.uuid
        val editRequest = ConnectionEditRequest("Edited Name", "Edited Description", "https://example.com/edited")

        whenever(connectionRepository.findByUuid(connectionId)).thenReturn(connection1)
        whenever(connectionRepository.save(any<ConnectionDetails>())).thenReturn(connection1)

        val result = connectionService.editConnection(connectionId, editRequest)

        assertEquals(editRequest.name, result.name)
        assertEquals(editRequest.description, result.description)
        assertEquals(editRequest.url, result.url)
        verify(connectionRepository, times(1)).findByUuid(connectionId)
        verify(connectionRepository, times(1)).save(any<ConnectionDetails>())
    }
    @Test
    fun testGetConnectionById() {
        val connectionId = connection1.uuid
        whenever(connectionRepository.findByUuid(connectionId)).thenReturn(connection1)

        val result = connectionService.getConnectionById(connectionId)

        assertEquals(connection1.uuid, result.uuid)
        assertEquals(connection1.name, result.name)
        assertEquals(connection1.description, result.description)
        assertEquals(connection1.url, result.url)
        verify(connectionRepository, times(1)).findByUuid(connectionId)
    }

    @Test
    fun testGetConnectionByIdWhenNotFound() {
        val connectionId = UUID.randomUUID()
        whenever(connectionRepository.findByUuid(connectionId)).thenReturn(null)

        assertThrows(ConnectionNotFoundException::class.java) {
            connectionService.getConnectionById(connectionId)
        }

        verify(connectionRepository, times(1)).findByUuid(connectionId)
    }
}