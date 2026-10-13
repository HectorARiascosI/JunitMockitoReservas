package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.mapper.ClienteMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Nested
    @DisplayName("Operaciones de Creación")
    class CreacionTests {

        // PRUEBA 1: Crear un cliente exitosamente cuando los datos son válidos
        @Test
        @DisplayName("Debe crear un cliente exitosamente cuando los datos son válidos")
        void debeCrearClienteExitosamente() {
            // Arrange
            CrearClienteRequest request = new CrearClienteRequest("Juan Perez", "juan@correo.com");
            Cliente clienteGuardado = new Cliente("Juan Perez", "juan@correo.com");
            ClienteResponse responseEsperado = new ClienteResponse(
                    clienteGuardado.getId(),
                    "Juan Perez",
                    "juan@correo.com",
                    true,
                    0);

            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
            when(clienteMapper.toResponse(any(Cliente.class))).thenReturn(responseEsperado);

            // Act
            ClienteResponse resultado = clienteService.crear(request);

            // Assert
            assertNotNull(resultado);
            assertEquals("Juan Perez", resultado.nombre());
            assertEquals("juan@correo.com", resultado.email());
            assertTrue(resultado.activo());

            verify(clienteRepository, times(1)).save(any(Cliente.class));
            verify(clienteMapper, times(1)).toResponse(any(Cliente.class));
        }

        // PRUEBA 2: Lanzar excepción cuando el nombre del cliente está vacío
        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre del cliente está vacío")
        void debeLanzarExcepcionCuandoNombreEstaVacio() {
            // Arrange - Act - Assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Cliente("", "juan@correo.com");
            });

            verifyNoInteractions(clienteRepository);
        }
    }

    @Nested
    @DisplayName("Operaciones de Consulta")
    class ConsultaTests {

        // PRUEBA 3: Retornar el cliente cuando se busca por un ID existente
        @Test
        @DisplayName("Debe retornar el cliente cuando se busca por un ID existente")
        void debeRetornarClientePorIdExistente() {
            // Arrange
            UUID clienteId = UUID.randomUUID();
            Cliente clienteExistente = new Cliente("Maria Lopez", "maria@correo.com");
            ClienteResponse responseEsperado = new ClienteResponse(
                    clienteId,
                    "Maria Lopez",
                    "maria@correo.com",
                    true,
                    0);

            when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
            when(clienteMapper.toResponse(clienteExistente)).thenReturn(responseEsperado);

            // Act
            ClienteResponse resultado = clienteService.obtenerPorId(clienteId);

            // Assert
            assertNotNull(resultado);
            assertEquals("Maria Lopez", resultado.nombre());
            assertEquals("maria@correo.com", resultado.email());

            verify(clienteRepository, times(1)).findById(clienteId);
            verify(clienteMapper, times(1)).toResponse(clienteExistente);
        }

        // PRUEBA 4: Lanzar excepción cuando el cliente no existe
        @Test
        @DisplayName("Debe lanzar excepción cuando el cliente no existe")
        void debeLanzarExcepcionCuandoClienteNoExiste() {
            // Arrange
            UUID clienteId = UUID.randomUUID();

            when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

            // Act - Assert
            assertThrows(IllegalArgumentException.class, () -> {
                clienteService.obtenerPorId(clienteId);
            });

            verify(clienteRepository, times(1)).findById(clienteId);
        }

        // PRUEBA 5: Retornar la lista completa de clientes
        @Test
        @DisplayName("Debe retornar la lista completa de clientes")
        void debeRetornarListaDeClientes() {
            // Arrange
            Cliente cliente1 = new Cliente("Carlos Ruiz", "carlos@correo.com");
            Cliente cliente2 = new Cliente("Ana Torres", "ana@correo.com");
            List<Cliente> clientes = List.of(cliente1, cliente2);

            ClienteResponse response1 = new ClienteResponse(cliente1.getId(), "Carlos Ruiz", "carlos@correo.com", true, 0);
            ClienteResponse response2 = new ClienteResponse(cliente2.getId(), "Ana Torres", "ana@correo.com", true, 0);
            List<ClienteResponse> responsesEsperados = List.of(response1, response2);

            when(clienteRepository.findAll()).thenReturn(clientes);
            when(clienteMapper.toResponseList(clientes)).thenReturn(responsesEsperados);

            // Act
            List<ClienteResponse> resultado = clienteService.listarTodos();

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            assertEquals("Carlos Ruiz", resultado.get(0).nombre());
            assertEquals("Ana Torres", resultado.get(1).nombre());

            verify(clienteRepository, times(1)).findAll();
            verify(clienteMapper, times(1)).toResponseList(clientes);
        }
    }
}
