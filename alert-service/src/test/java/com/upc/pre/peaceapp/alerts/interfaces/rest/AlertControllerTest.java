package com.upc.pre.peaceapp.alerts.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import com.upc.pre.peaceapp.alerts.domain.model.commands.CreateAlertCommand;
import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByReportIdCommand;
import com.upc.pre.peaceapp.alerts.domain.model.commands.DeleteAllAlertsByUserIdCommand;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertByIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAllAlertsQuery;
import com.upc.pre.peaceapp.alerts.domain.model.queries.GetAlertsByUserIdQuery;
import com.upc.pre.peaceapp.alerts.domain.model.valueobjects.AlertType;
import com.upc.pre.peaceapp.alerts.domain.services.AlertCommandService;
import com.upc.pre.peaceapp.alerts.domain.services.AlertQueryService;
import com.upc.pre.peaceapp.alerts.interfaces.rest.resources.AlertResource;
import com.upc.pre.peaceapp.alerts.interfaces.rest.resources.CreateAlertResource;
import com.upc.pre.peaceapp.alerts.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.upc.pre.peaceapp.alerts.interfaces.rest.transform.CreateAlertCommandFromResourceAssembler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
// @AutoConfigureMockMvc(addFilters = false) // descomenta si filtros de seguridad bloquean
class AlertControllerTest {

    private static final String BASE = "/api/v1/alerts";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private AlertCommandService alertCommandService;
    @MockBean private AlertQueryService alertQueryService;
    @MockBean private CreateAlertCommandFromResourceAssembler createAssembler; // es bean (no estático)

    // --------------------- POST /alerts ---------------------
    @Test
    @DisplayName("POST /alerts -> 201 Created + Location + AlertResource")
    void createAlert_created() throws Exception {
        var req = new CreateAlertResource(
                "Av. Primavera 123", AlertType.ROBBERY, "Robo reportado", 101L, "https://img", 45L
        );

        var cmd = new CreateAlertCommand(
                req.location(), req.type(), req.description(), req.userId(), req.imageUrl(), req.reportId()
        );

        // entidad y resource de salida
        Alert entity = Mockito.mock(Alert.class);
        when(entity.getId()).thenReturn(10L);
        var resource = new AlertResource(10L, req.location(), req.type(), req.description(),
                req.userId(), req.imageUrl(), req.reportId());

        when(createAssembler.toCommand(any(CreateAlertResource.class))).thenReturn(cmd);
        when(alertCommandService.handle(any(CreateAlertCommand.class))).thenReturn(Optional.of(entity));

        try (MockedStatic<AlertResourceFromEntityAssembler> m =
                     Mockito.mockStatic(AlertResourceFromEntityAssembler.class)) {
            m.when(() -> AlertResourceFromEntityAssembler.toResourceFromEntity(any(Alert.class)))
                    .thenReturn(resource);

            mockMvc.perform(post(BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", BASE + "/10"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(10)))
                    .andExpect(jsonPath("$.type", is("ROBBERY")))
                    .andExpect(jsonPath("$.userId", is(101)));
        }
    }

    @Test
    @DisplayName("POST /alerts -> 400 Bad Request cuando service no crea")
    void createAlert_badRequest() throws Exception {
        var req = new CreateAlertResource("loc", AlertType.ROBBERY, "desc", 1L, null, null);

        when(createAssembler.toCommand(any(CreateAlertResource.class)))
                .thenReturn(Mockito.mock(CreateAlertCommand.class));
        when(alertCommandService.handle(any(CreateAlertCommand.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // --------------------- GET /alerts/{id} ---------------------
    @Test
    @DisplayName("GET /alerts/{id} -> 200 OK con AlertResource")
    void getAlertById_ok() throws Exception {
        Alert entity = Mockito.mock(Alert.class);
        when(alertQueryService.handle(any(GetAlertByIdQuery.class))).thenReturn(Optional.of(entity));

        var res = new AlertResource(5L, "loc", AlertType.ROBBERY, "desc", 101L, null, 45L);

        try (MockedStatic<AlertResourceFromEntityAssembler> m =
                     Mockito.mockStatic(AlertResourceFromEntityAssembler.class)) {
            m.when(() -> AlertResourceFromEntityAssembler.toResourceFromEntity(any(Alert.class)))
                    .thenReturn(res);

            mockMvc.perform(get(BASE + "/{id}", 5))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(5)))
                    .andExpect(jsonPath("$.type", is("ROBBERY")));
        }
    }

    @Test
    @DisplayName("GET /alerts/{id} -> 404 Not Found cuando no existe")
    void getAlertById_notFound() throws Exception {
        when(alertQueryService.handle(any(GetAlertByIdQuery.class))).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // --------------------- GET /alerts/user/{userId} ---------------------
    @Test
    @DisplayName("GET /alerts/user/{userId} -> 200 OK con lista")
    void getAlertsByUserId_ok() throws Exception {
        Alert e1 = Mockito.mock(Alert.class);
        Alert e2 = Mockito.mock(Alert.class);

        when(alertQueryService.handle(any(GetAlertsByUserIdQuery.class)))
                .thenReturn(List.of(e1, e2));

        var r1 = new AlertResource(1L, "l1", AlertType.ROBBERY, "d1", 101L, null, 11L);
        var r2 = new AlertResource(2L, "l2", AlertType.ROBBERY, "d2", 101L, null, 12L);

        try (MockedStatic<AlertResourceFromEntityAssembler> m =
                     Mockito.mockStatic(AlertResourceFromEntityAssembler.class)) {
            m.when(() -> AlertResourceFromEntityAssembler.toResourceFromEntity(any(Alert.class)))
                    .thenReturn(r1, r2);

            mockMvc.perform(get(BASE + "/user/{userId}", 101))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[1].id", is(2)));
        }
    }

    @Test
    @DisplayName("GET /alerts/user/{userId} -> 404 Not Found cuando vacío")
    void getAlertsByUserId_notFound() throws Exception {
        when(alertQueryService.handle(any(GetAlertsByUserIdQuery.class))).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/user/{userId}", 101))
                .andExpect(status().isNotFound());
    }

    // --------------------- GET /alerts ---------------------
    @Test
    @DisplayName("GET /alerts -> 200 OK con lista")
    void getAllAlerts_ok() throws Exception {
        when(alertQueryService.handle(any(GetAllAlertsQuery.class)))
                .thenReturn(List.of(Mockito.mock(Alert.class)));

        var r = new AlertResource(3L, "l3", AlertType.ROBBERY, "d3", 102L, null, 33L);

        try (MockedStatic<AlertResourceFromEntityAssembler> m =
                     Mockito.mockStatic(AlertResourceFromEntityAssembler.class)) {
            m.when(() -> AlertResourceFromEntityAssembler.toResourceFromEntity(any(Alert.class)))
                    .thenReturn(r);

            mockMvc.perform(get(BASE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(3)));
        }
    }

    @Test
    @DisplayName("GET /alerts -> 204 No Content cuando no hay registros")
    void getAllAlerts_noContent() throws Exception {
        when(alertQueryService.handle(any(GetAllAlertsQuery.class))).thenReturn(List.of());

        mockMvc.perform(get(BASE))
                .andExpect(status().isNoContent());
    }

    // --------------------- DELETE /alerts/user/{userId} ---------------------
    @Test
    @DisplayName("DELETE /alerts/user/{userId} -> 200 OK con mensaje")
    void deleteAllByUser_ok() throws Exception {
        Mockito.doNothing().when(alertCommandService)
                .handle(any(DeleteAllAlertsByUserIdCommand.class));

        mockMvc.perform(delete(BASE + "/user/{userId}", 101))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
    }

    @Test
    @DisplayName("DELETE /alerts/user/{userId} -> 400 Bad Request cuando IllegalArgumentException")
    void deleteAllByUser_badRequest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("invalid user"))
                .when(alertCommandService).handle(any(DeleteAllAlertsByUserIdCommand.class));

        mockMvc.perform(delete(BASE + "/user/{userId}", 101))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("invalid user")));
    }

    @Test
    @DisplayName("DELETE /alerts/user/{userId} -> 500 Internal Server Error en error inesperado")
    void deleteAllByUser_internalError() throws Exception {
        Mockito.doThrow(new RuntimeException("boom"))
                .when(alertCommandService).handle(any(DeleteAllAlertsByUserIdCommand.class));

        mockMvc.perform(delete(BASE + "/user/{userId}", 101))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("boom")));
    }

    // --------------------- DELETE /alerts/report/{reportId} ---------------------
    @Test
    @DisplayName("DELETE /alerts/report/{reportId} -> 200 OK con mensaje")
    void deleteAllByReport_ok() throws Exception {
        Mockito.doNothing().when(alertCommandService)
                .handle(any(DeleteAllAlertsByReportIdCommand.class));

        mockMvc.perform(delete(BASE + "/report/{reportId}", 45))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
    }

    @Test
    @DisplayName("DELETE /alerts/report/{reportId} -> 400 Bad Request cuando IllegalArgumentException")
    void deleteAllByReport_badRequest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("invalid report"))
                .when(alertCommandService).handle(any(DeleteAllAlertsByReportIdCommand.class));

        mockMvc.perform(delete(BASE + "/report/{reportId}", 45))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("invalid report")));
    }

    @Test
    @DisplayName("DELETE /alerts/report/{reportId} -> 500 Internal Server Error en error inesperado")
    void deleteAllByReport_internalError() throws Exception {
        Mockito.doThrow(new RuntimeException("boom"))
                .when(alertCommandService).handle(any(DeleteAllAlertsByReportIdCommand.class));

        mockMvc.perform(delete(BASE + "/report/{reportId}", 45))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("boom")));
    }
}
