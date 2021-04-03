package team.changeservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import team.changeservice.hierarchy.repo.HierarchyClient;
import team.changeservice.model.ChangeFailureRate;
import team.changeservice.model.ChangeRequest;
import team.changeservice.model.DORALevel;
import team.changeservice.repo.ChangeRequestRepo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ChangeRequestServiceImplTest
    {
    @Autowired
    private ChangeRequestService changeRequestService;
    @Autowired
    private ChangeRequestRepo mockChangeRequestRepo;
    @Autowired
    private HierarchyClient mockHierarchyClient;

    @TestConfiguration
    static class ChangeRequestServiceImplTestContextConfiguration
        {
        @MockBean
        private ChangeRequestRepo mockChangeRequestRepo;
        @MockBean
        private HierarchyClient mockHierarchyClient;
        @Bean
        public ChangeRequestService changeRequestService()
            {
            return new ChangeRequestServiceImpl(mockChangeRequestRepo, mockHierarchyClient);
            }
        }
    
    private ChangeRequest setupChangeRequest(int startYear, int endYear, int startMonth, int endMonth, int startDay, int endDay, int startHour, int endHour, int startMin, int endMin, boolean failed)
        {
        return new ChangeRequest(
            "cr1",
            "change request 1",
            "a1", 
                dateOf(startYear, startMonth, startDay, startHour, startMin, 0),
                dateOf(startYear, startMonth, startDay, startHour, startMin, 0),
                dateOf(endYear, endMonth, endDay, endHour, endMin, 0),
                dateOf(endYear, endMonth, endDay, endHour, endMin, 0),
                failed,
            "test");
        }
    
    static Date dateOf(int year,  int month, int dayOfMonth, int hour, int minute, int second){
        return Date.from(
            LocalDateTime.of(
                year, 
                month, 
                dayOfMonth, 
                hour, 
                minute, 
                second)
                .toInstant(ZoneOffset.UTC));
    }

    private List<ChangeRequest> setupChangeRequests(boolean f1, boolean f2, boolean f3, boolean f4, boolean f5){
        ChangeRequest cr1 = setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, f1);
        ChangeRequest cr2 = setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, f2);
        ChangeRequest cr3 = setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, f3);
        ChangeRequest cr4 = setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, f4);
        ChangeRequest cr5 = setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, f5);
        return Arrays.asList(cr1, cr2, cr3, cr4, cr5);
    }

    @Test
    void checkCfrCalc()
        {
        List<ChangeRequest> crs = setupChangeRequests(true, true, true, true, true);
        when(mockChangeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class))).thenReturn(crs);

        ChangeFailureRate cfr = changeRequestService.calculateChangeFailureRate("a1", new Date());

        verify(mockChangeRequestRepo, times(1)).findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class));
        assertThat(cfr.getChangeFailureRatePercent(), is(equalTo(1.0)));
        assertThat(cfr.getDoraLevel(), is(equalTo(DORALevel.LOW)));
        }

    @Test
    void checkUnknownCfrLevel()
        {
        when(mockChangeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class))).thenReturn(Collections.emptyList());

        ChangeFailureRate cfr = changeRequestService.calculateChangeFailureRate("a1", new Date());

        verify(mockChangeRequestRepo, times(1)).findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class));
        assertThat(cfr.getChangeFailureRatePercent(), is(equalTo(0.00)));
        assertThat(cfr.getDoraLevel(), is(equalTo(DORALevel.UNKNOWN)));
        }

    @Test
    void checkLowCfrLevel()
        {
        List<ChangeRequest> crs = setupChangeRequests(true, true, true, true, true);
        when(mockChangeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class))).thenReturn(crs);

        ChangeFailureRate cfr = changeRequestService.calculateChangeFailureRate("a1", new Date());

        verify(mockChangeRequestRepo, times(1)).findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class));
        assertThat(cfr.getChangeFailureRatePercent(), is(equalTo(1.00)));
        assertThat(cfr.getDoraLevel(), is(equalTo(DORALevel.LOW)));
        }

    @Test
    void checkEliteCfrLevel()
        {
        List<ChangeRequest> crs = setupChangeRequests(false, false, false, false, false);
        when(mockChangeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class))).thenReturn(crs);

        ChangeFailureRate cfr = changeRequestService.calculateChangeFailureRate("a1", new Date());

        verify(mockChangeRequestRepo, times(1)).findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class));
        assertThat(cfr.getChangeFailureRatePercent(), is(equalTo(0.00)));
        assertThat(cfr.getDoraLevel(), is(equalTo(DORALevel.ELITE)));
        }

    @Test
    void checkExactEliteCfrLevel()
        {
        List<ChangeRequest> crs = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            if(i < 3) {
                crs.addAll(setupChangeRequests(true, true, true, true, true));
            } else {
                crs.addAll(setupChangeRequests(false, false, false, false, false));
            }
        }
        assertThat(crs.size(), is(equalTo(100)));
        when(mockChangeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class))).thenReturn(crs);

        ChangeFailureRate cfr = changeRequestService.calculateChangeFailureRate("a1", new Date());

        verify(mockChangeRequestRepo, times(1)).findByApplicationIdAndClosedBetweenOrderByClosed(eq("a1"), any(Date.class), any(Date.class));
        assertThat(cfr.getChangeFailureRatePercent(), is(equalTo(0.15)));
        assertThat(cfr.getDoraLevel(), is(equalTo(DORALevel.ELITE)));
        }

    @Test
    void checkListAll()
        {
        ChangeRequest cr1 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        ChangeRequest cr2 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        List<ChangeRequest> changeRequests = new ArrayList<>();
        changeRequests.add(cr1);
        changeRequests.add(cr2);
        String appId = "app1";
        when(mockChangeRequestRepo.findByApplicationId
            (appId))
            .thenReturn(changeRequests);
        
        List<ChangeRequest> requests = changeRequestService.listAllForApplication(appId);
        
        assertThat(requests.size(), equalTo(2));
        }

    @Test
    void checkListHierarchy()
        {
        ChangeRequest cr1 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        ChangeRequest cr2 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        List<ChangeRequest> changeRequests = new ArrayList<>();
        changeRequests.add(cr1);
        changeRequests.add(cr2);
        String appId = "app1";
        when(mockHierarchyClient.findChildIds("a1")).thenReturn(Arrays.asList("a1", "a2"));
        when(mockChangeRequestRepo.findByApplicationIdInOrderByClosedDesc(anyCollection()))
            .thenReturn(changeRequests);

        List<ChangeRequest> crList = changeRequestService.listAllForHierarchy(appId);
        
        verify(mockHierarchyClient, times(1)).findChildIds("app1");
        verify(mockChangeRequestRepo, times(1)).findByApplicationIdInOrderByClosedDesc(anyCollection());
        assertThat(crList.size(), equalTo(2));
        }

    @Test
    void checkListAllWithDate()
        {
        ChangeRequest d1 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        ChangeRequest d2 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        List<ChangeRequest> changeRequests = new ArrayList<>();
        changeRequests.add(d1);
        changeRequests.add(d2);
        String appId = "app1";
        when(mockChangeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed
            (appId,
                dateOf(2020, 3, 10, 0, 0, 0),
                dateOf(2020, 3, 11, 0, 0, 0)))
            .thenReturn(changeRequests);

        List<ChangeRequest> crList = changeRequestService.listAllForApplication(appId, dateOf(2020, 3, 10, 0, 0, 0));

        assertThat(crList.size(), equalTo(2));
        }

    @Test
    void checkDelete()
        {
        ChangeRequest cr1 =  setupChangeRequest(2021, 2021, 2, 2, 4, 4, 8, 10, 0, 0, true);
        when(mockChangeRequestRepo.findById("id123"))
            .thenReturn(Optional.of(cr1));
        String id = changeRequestService.delete("id123");
        assertThat(id, is(equalTo("id123")));
        verify(mockChangeRequestRepo, times(1)).findById("id123");
        verify(mockChangeRequestRepo, times(1)).delete(cr1);
        }
    }
