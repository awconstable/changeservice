package team.changeservice.controller.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.changeservice.model.ChangeFailureRate;
import team.changeservice.model.ChangeRequest;
import team.changeservice.model.DORALevel;
import team.changeservice.service.ChangeRequestService;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChangeRequestControllerV1.class)
class ChangeRequestControllerV1Test
    {
    
    @Autowired private MockMvc mockMvc;
    
    @MockBean private ChangeRequestService mockChangeRequestService;
    
    @Test
    void store() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
            .content(
                "[" +
                "{\n" +
                "    \"changeRequestId\": \"cr11\",\n" +
                "    \"description\": \"a description\",\n" +
                "    \"applicationId\": \"a1\",\n" +
                "    \"created\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"closed\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"started\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"finished\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"failed\": true,\n" +
                "    \"source\": \"test\"\n" +
                "}" +
                "]"
            ))
            .andExpect(status().isCreated());
        verify(mockChangeRequestService, times(1)).store(any(ChangeRequest.class));
        }
    
    @Test
    void storeCheckValidationEmptyList() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
            .content(
                "[]"
            ))
            .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }
    
    @Test
    void storeCheckValidationAppId() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
            .content(
                "[" +
                "{\n" +
                "    \"changeRequestId\": \"cr11\",\n" +
                "    \"description\": \"a description\",\n" +
                "    \"created\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"closed\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"started\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"finished\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"failed\": true,\n" +
                "    \"source\": \"test\"\n" +
                "}" +
                "]"
            ))
            .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }

    @Test
    void storeCheckValidationCreated() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
            .content(
                "[" +
                "{\n" +
                "    \"changeRequestId\": \"cr11\",\n" +
                "    \"description\": \"a description\",\n" +
                "    \"applicationId\": \"a1\",\n" +
                "    \"closed\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"started\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"finished\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"failed\": true,\n" +
                "    \"source\": \"test\"\n" +
                "}" +
                "]"
            ))
            .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }

    @Test
    void storeCheckValidationFinished() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
            .content(
                "[" +
                "{\n" +
                "    \"changeRequestId\": \"cr11\",\n" +
                "    \"description\": \"a description\",\n" +
                "    \"applicationId\": \"a1\",\n" +
                "    \"created\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"closed\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"started\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"failed\": true,\n" +
                "    \"source\": \"test\"\n" +
                "}" +
                "]"
            ))
            .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }

    @Test
    void storeCheckValidationStarted() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
            .content(
                "[" +
                "{\n" +
                "    \"changeRequestId\": \"cr11\",\n" +
                "    \"description\": \"a description\",\n" +
                "    \"applicationId\": \"a1\",\n" +
                "    \"created\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"closed\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"finished\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"failed\": true,\n" +
                "    \"source\": \"test\"\n" +
                "}" +
                "]"
                ))
                .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }

    @Test
    void storeCheckValidationClosed() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
                .content(
                "[" +
                "{\n" +
                "    \"changeRequestId\": \"cr11\",\n" +
                "    \"description\": \"a description\",\n" +
                "    \"applicationId\": \"a1\",\n" +
                "    \"created\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"started\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"finished\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                "    \"failed\": true,\n" +
                "    \"source\": \"test\"\n" +
                "}" +
                "]"
                ))
                .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }

    @Test
    void storeCheckValidationFailed() throws Exception
        {
        mockMvc.perform(post("/api/v1/changerequest").contentType(MediaType.APPLICATION_JSON)
                .content(
                    "[" +
                    "{\n" +
                    "    \"changeRequestId\": \"cr11\",\n" +
                    "    \"description\": \"a description\",\n" +
                    "    \"applicationId\": \"a1\",\n" +
                    "    \"created\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                    "    \"closed\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                    "    \"started\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                    "    \"finished\": \"2020-11-30T22:00:00.000+00:00\",\n" +
                    "    \"source\": \"test\"\n" +
                    "}" +
                    "]"
                ))
                .andExpect(status().is4xxClientError());
        verify(mockChangeRequestService, never()).store(any(ChangeRequest.class));
        }

    @Test
    void list() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        
        List<ChangeRequest> crs = new ArrayList<>();
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        crs.add(cr1);
        crs.add(cr2);
        when(mockChangeRequestService.list()).thenReturn(crs);
        MvcResult result = mockMvc.perform(get("/api/v1/changerequest")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content, is(equalTo("[{\"changeRequestId\":\"cr1\",\"description\":\"change request 1\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"},{\"changeRequestId\":\"cr2\",\"description\":\"change request 2\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"}]")));
        verify(mockChangeRequestService, times(1)).list();
        }

    @Test
    void show() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        
        String id = "cr1";
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");

        when(mockChangeRequestService.get(id)).thenReturn(Optional.of(cr1));
        MvcResult result = mockMvc.perform(get("/api/v1/changerequest/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content, is(equalTo("{\"changeRequestId\":\"cr1\",\"description\":\"change request 1\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"}")));
        verify(mockChangeRequestService, times(1)).get(id);
        }

    @Test
    void delete() throws Exception
        {
        when(mockChangeRequestService.delete("id123")).thenReturn("id123");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/changerequest/{id}", "id123")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content, is(equalTo("id123")));
        verify(mockChangeRequestService, times(1)).delete("id123");
        }

    @Test
    void listByApp() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        String appId = "id123";
        List<ChangeRequest> crs = new ArrayList<>();
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        crs.add(cr1);
        crs.add(cr2);

        when(mockChangeRequestService.listAllForApplication(appId)).thenReturn(crs);

        MvcResult result = mockMvc.perform(get("/api/v1/changerequest/application/" + appId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content, is(equalTo( "[{\"changeRequestId\":\"cr1\",\"description\":\"change request 1\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"},{\"changeRequestId\":\"cr2\",\"description\":\"change request 2\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"}]")));
        verify(mockChangeRequestService, times(1)).listAllForApplication(appId);
        }

    @Test
    void listForHierarchy() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        String appId = "id123";
        List<ChangeRequest> crs = new ArrayList<>();
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        crs.add(cr1);
        crs.add(cr2);

        when(mockChangeRequestService.listAllForHierarchy(appId)).thenReturn(crs);

        MvcResult result = mockMvc.perform(get("/api/v1/changerequest/hierarchy/" + appId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content, is(equalTo( "[{\"changeRequestId\":\"cr1\",\"description\":\"change request 1\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"},{\"changeRequestId\":\"cr2\",\"description\":\"change request 2\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"}]")));
        verify(mockChangeRequestService, times(1)).listAllForHierarchy(appId);
        }
    
    @Test
    void listByAppAndDate() throws Exception
        {
        ZonedDateTime reportingDate = LocalDate.of(2020, 10, 10).atStartOfDay(ZoneId.of("UTC"));
        String dateIn = DateTimeFormatter.ISO_LOCAL_DATE.format(reportingDate);
        String appId = "id123";
        List<ChangeRequest> crs = new ArrayList<>();
        ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "a1", Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()), Date.from(reportingDate.toInstant()),true, "test");
        crs.add(cr1);
        crs.add(cr2);
        
        when(mockChangeRequestService.listAllForApplication(appId, Date.from(reportingDate.toInstant()))).thenReturn(crs);
        
        MvcResult result = mockMvc.perform(get("/api/v1/changerequest/application/" + appId + "/date/" + dateIn)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        
        String content = result.getResponse().getContentAsString();
        verify(mockChangeRequestService, times(1)).listAllForApplication(appId, Date.from(reportingDate.toInstant()));
        assertThat(content, is(equalTo("[{\"changeRequestId\":\"cr1\",\"description\":\"change request 1\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"},{\"changeRequestId\":\"cr2\",\"description\":\"change request 2\",\"applicationId\":\"a1\",\"created\":\"2020-10-10T00:00:00.000+00:00\",\"started\":\"2020-10-10T00:00:00.000+00:00\",\"finished\":\"2020-10-10T00:00:00.000+00:00\",\"closed\":\"2020-10-10T00:00:00.000+00:00\",\"failed\":true,\"source\":\"test\"}]")));
        }
    
    @Test
    void calcCfr() throws Exception
        {
        LocalDateTime date = LocalDate.now().minusDays(1).atStartOfDay();
        Date reportingDate = Date.from(date.toInstant(ZoneOffset.UTC));
        String appId = "a1";
        ChangeFailureRate changeFailureRate = new ChangeFailureRate(appId, reportingDate, 0.00, 4, DORALevel.ELITE);
        when(mockChangeRequestService.calculateChangeFailureRate(appId, reportingDate)).thenReturn(changeFailureRate);
        
        MvcResult result = mockMvc.perform(get("/api/v1/changerequest/application/" + appId + "/cfr")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        
        String content = result.getResponse().getContentAsString();
        String dateOut = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        assertThat(content, is(equalTo("{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"" + dateOut + "\",\"changeFailureRatePercent\":0.0,\"changeRequestCount\":4,\"doraLevel\":\"ELITE\"}")));
        verify(mockChangeRequestService, times(1)).calculateChangeFailureRate(appId, reportingDate);
        }
    
    @Test
    void calcCfrByDate() throws Exception
        {
        LocalDateTime date = LocalDate.of(2020, Month.OCTOBER, 3).atStartOfDay();
        Date reportingDate = Date.from(date.toInstant(ZoneOffset.UTC));
        String appId = "a1";
        ChangeFailureRate changeFailureRate = new ChangeFailureRate(appId, reportingDate, 0.00, 4, DORALevel.ELITE);
        when(mockChangeRequestService.calculateChangeFailureRate(appId, reportingDate)).thenReturn(changeFailureRate);
        String dateOut = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        assertThat(dateOut, is(equalTo("2020-10-03")));

        MvcResult result = mockMvc.perform(get("/api/v1/changerequest/application/" + appId + "/cfr/" + dateOut)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content, is(equalTo("{\"applicationId\":\"" + appId + "\",\"reportingDate\":\"" + dateOut + "\",\"changeFailureRatePercent\":0.0,\"changeRequestCount\":4,\"doraLevel\":\"ELITE\"}")));
        verify(mockChangeRequestService, times(1)).calculateChangeFailureRate(appId, reportingDate);
        }
    
    }