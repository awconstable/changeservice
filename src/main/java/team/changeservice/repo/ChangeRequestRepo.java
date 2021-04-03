package team.changeservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import team.changeservice.model.ChangeRequest;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface ChangeRequestRepo extends MongoRepository<ChangeRequest, String>
    {
        List<ChangeRequest> findByApplicationId(String applicationId);

        List<ChangeRequest> findByApplicationIdInOrderByClosedDesc(Collection<String> applicationIds);
        
        List<ChangeRequest> findByApplicationIdAndClosedBetweenOrderByClosed(String applicationId, Date start, Date end);
    }
