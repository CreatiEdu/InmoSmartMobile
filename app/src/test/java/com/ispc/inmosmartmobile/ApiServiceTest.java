package com.ispc.inmosmartmobile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

public class ApiServiceTest {

    private MockWebServer mockWebServer;
    private ApiService apiService;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    // AUT-NET-01: Probar respuesta exitosa 200 OK del backend
    @Test
    public void obtenerTurnos_exitoso() throws Exception {
        // Simulamos un JSON de respuesta de turnos
        String jsonResponse = "[{\"id_turno\": 1, \"fecha_hora\": \"2026-11-15 15:30\", \"estado\": \"Pendiente\"}]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse));

        // Ejecutamos la llamada síncrona con execute() para el test
        retrofit2.Response<List<TurnoModel>> response = apiService.obtenerTurnos("Bearer token_falso").execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());
        assertEquals(1, response.body().get(0).getIdTurno());
    }

    // AUT-NET-02: Probar error del servidor 500 Internal Server Error
    @Test
    public void obtenerTurnos_errorServidor() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Error interno"));

        retrofit2.Response<List<TurnoModel>> response = apiService.obtenerTurnos("Bearer token_falso").execute();

        assertFalse(response.isSuccessful());
        assertEquals(500, response.code());
    }
}