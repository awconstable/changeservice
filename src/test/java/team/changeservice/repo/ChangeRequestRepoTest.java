package team.changeservice.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.changeservice.MongoDBContainerTest;
import team.changeservice.model.ChangeRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ChangeRequestRepoTest extends MongoDBContainerTest
    {
    
    @Autowired
    ChangeRequestRepo repo;
    
    @BeforeEach
    void setUp()
        {
            ChangeRequest cr1 = new ChangeRequest("cr1", "change request 1", "a1", Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()), true,"test");
            repo.save(cr1);
            ChangeRequest cr2 = new ChangeRequest("cr2", "change request 2", "a1", Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()),true,"test");
            repo.save(cr2);
            ChangeRequest cr3 = new ChangeRequest("cr3", "change request 3", "a2", Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()),true,"test");
            repo.save(cr3);
            ChangeRequest cr4 = new ChangeRequest("cr4", "change request 4", "a3", Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()), Date.from(Instant.now()),true,"test");
            repo.save(cr4);
        }

    @AfterEach
    void tearDown()
        {
        repo.deleteAll();
        }
    
    @Test
    public void getWithChangeRequestId()
        {
            Optional<ChangeRequest> cr1 = repo.findById("cr1");
            assert(cr1.isPresent());
            assertThat(cr1.get().getChangeRequestId(), is(equalTo("cr1")));
        }

    @Test
    public void getAllWithApplicationId()
        {
            List<ChangeRequest> crs = repo.findByApplicationId("a1");
            assertThat(crs.size(), is(equalTo(2)));
        }
    
    @Test
    public void getAllForDateRange()
        {
            LocalDateTime startDateTime = LocalDate.now().atStartOfDay();
            LocalDateTime endDateTime = LocalDate.now().plusDays(1).atStartOfDay();
            Date startDate = Date.from(startDateTime.toInstant(ZoneOffset.UTC));
            Date endDate = Date.from(endDateTime.toInstant(ZoneOffset.UTC));
            List<ChangeRequest> crs = repo.findByApplicationIdAndClosedBetweenOrderByClosed("a1", startDate, endDate);
            assertThat(crs.size(), is(equalTo(2)));
        }

    @Test
    public void getAllAppsUsingIn()
        {
            List<ChangeRequest> crs = repo.findByApplicationIdInOrderByClosedDesc(Arrays.asList("a1", "a2"));
            assertThat(crs.size(), is(equalTo(3)));
        }
    }