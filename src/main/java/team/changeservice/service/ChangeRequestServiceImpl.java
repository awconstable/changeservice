package team.changeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.changeservice.hierarchy.repo.HierarchyClient;
import team.changeservice.model.ChangeFailureRate;
import team.changeservice.model.ChangeRequest;
import team.changeservice.model.DORALevel;
import team.changeservice.repo.ChangeRequestRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ChangeRequestServiceImpl implements ChangeRequestService
    {
    private static final Logger log = LoggerFactory.getLogger(ChangeRequestServiceImpl.class);

    private final ChangeRequestRepo changeRequestRepo;
    private final HierarchyClient hierarchyClient;

    @Autowired
    public ChangeRequestServiceImpl(ChangeRequestRepo changeRequestRepo, HierarchyClient hierarchyClient)
        {
        this.changeRequestRepo = changeRequestRepo;
        this.hierarchyClient = hierarchyClient;
        }

    private DORALevel findDORAPerfLevel(BigDecimal cfrPercent){
        if(cfrPercent.compareTo(new BigDecimal("0.15")) <= 0){
            return DORALevel.ELITE;
        } else {
            return DORALevel.LOW;
        }
    }
    
    @Override
    public ChangeRequest store(ChangeRequest changeRequest)
        {
        log.info("Storing change request with id {}", changeRequest.getChangeRequestId());
        return changeRequestRepo.save(changeRequest);
        }

    @Override
    public Optional<ChangeRequest> get(String id)
        {
        log.info("Getting change request with id {}", id);
        return changeRequestRepo.findById(id);
        }

    @Override
    public List<ChangeRequest> list()
        {
        log.info("List all change requests");
        return changeRequestRepo.findAll();
        }

    @Override
    public String delete(String id)
        {
        log.info("Deleting change request with id {}", id);
        Optional<ChangeRequest> changeRequest = get(id);
        changeRequest.ifPresent(changeRequestRepo::delete);
        return id;
        }

    @Override
    public List<ChangeRequest> listAllForApplication(String applicationId)
        {
        log.info("Listing all change requests for applicationId {}", applicationId);
        return changeRequestRepo.findByApplicationId(applicationId);
        }

    @Override
    public List<ChangeRequest> listAllForHierarchy(String applicationId)
        {
        log.info("Listing all change requests in the hierarchy starting at applicationId {}", applicationId);
        Collection<String> appIds = hierarchyClient.findChildIds(applicationId);
        return changeRequestRepo.findByApplicationIdInOrderByClosedDesc(appIds);
        }

    @Override
    public List<ChangeRequest> listAllForApplication(String applicationId, Date reportingDate)
        {
        log.info("Listing all change requests for applicationId {}, and reporting date {}", applicationId, reportingDate);
        return changeRequestRepo.findByApplicationIdAndClosedBetweenOrderByClosed(applicationId, getStartDate(reportingDate, 0), getEndDate(reportingDate));
        }

    @Override
    public ChangeFailureRate calculateChangeFailureRate(String applicationId, Date reportingDate)
        {
        log.info("Calculating change failure rate for applicationId {}, and reporting date {}", applicationId, reportingDate);
        Date startDate = getStartDate(reportingDate, 89);
        Date endDate = getEndDate(reportingDate);
        List<ChangeRequest> requests = changeRequestRepo
                .findByApplicationIdAndClosedBetweenOrderByClosed(applicationId, startDate, endDate);
        //No data, return unknown performance level
        if(requests.size() == 0){
            return new ChangeFailureRate(applicationId, reportingDate, 0.0,0, DORALevel.UNKNOWN);
        }
        long failCount = requests.stream().filter(ChangeRequest::getFailed).count();
        double rawCfr = (double) failCount  / requests.size();
        BigDecimal cfr = new BigDecimal(rawCfr).setScale(2, RoundingMode.HALF_UP);
        DORALevel doraLevel = findDORAPerfLevel(cfr);
        return new ChangeFailureRate(applicationId, reportingDate, cfr.doubleValue(), requests.size(), doraLevel);
        }

    private Date getStartDate(Date reportingDate, Integer minusDays)
        {
            ZonedDateTime startDate = ZonedDateTime.ofInstant(reportingDate.toInstant(), ZoneOffset.UTC).minusDays(minusDays);
            return Date.from(startDate.toInstant());
        }
    
    private Date getEndDate(Date reportingDate)
        {
            return Date.from(ZonedDateTime.ofInstant(reportingDate.toInstant(), ZoneOffset.UTC).plusDays(1).toInstant());
        }
    }
