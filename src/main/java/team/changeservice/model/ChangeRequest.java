package team.changeservice.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

public class ChangeRequest
    {
    @Id
    @NotBlank(message = "Change Request: unique id is mandatory")
    private final String changeRequestId;
    private final String description;
    @NotBlank(message = "Change Request: applicationId is mandatory")
    private final String applicationId;
    @NotNull(message = "Change Request: created date is mandatory")
    private final Date created;
    @NotNull(message = "Change Request: started date is mandatory")
    private final Date started;
    @NotNull(message = "Change Request: finished date is mandatory")
    private final Date finished;
    @NotNull(message = "Change Request: closed date is mandatory")
    private final Date closed;
    @NotNull(message = "Change Request: failed flag is mandatory")
    private final Boolean failed;
    private final String source;

    public ChangeRequest(String changeRequestId, String description, String applicationId, Date created, Date started, Date finished, Date closed, Boolean failed, String source)
        {
        this.changeRequestId = changeRequestId;
        this.description = description;
        this.applicationId = applicationId;
        this.created = created;
        this.started = started;
        this.finished = finished;
        this.closed = closed;
        this.failed = failed;
        this.source = source;
        }

    public String getChangeRequestId()
        {
        return changeRequestId;
        }

    public String getDescription()
        {
        return description;
        }

    public String getApplicationId()
        {
        return applicationId;
        }

    public Date getCreated()
        {
        return created;
        }

    public Date getStarted()
        {
        return started;
        }

    public Date getFinished()
        {
        return finished;
        }

    public Date getClosed()
        {
        return closed;
        }

    public Boolean getFailed()
        {
        return failed;
        }

    public String getSource()
        {
        return source;
        }

    @Override
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangeRequest that = (ChangeRequest) o;
        return Objects.equals(changeRequestId, that.changeRequestId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(applicationId, that.applicationId) &&
                Objects.equals(created, that.created) &&
                Objects.equals(started, that.started) &&
                Objects.equals(finished, that.finished) &&
                Objects.equals(closed, that.closed) &&
                Objects.equals(failed, that.failed) &&
                Objects.equals(source, that.source);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(changeRequestId, description, applicationId, created, started, finished, closed, failed, source);
        }

    @Override
    public String toString()
        {
        return "ChangeRequest{" +
                "changeId='" + changeRequestId + '\'' +
                ", description='" + description + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", created=" + created +
                ", started=" + started +
                ", finished=" + finished +
                ", closed=" + closed +
                ", failed=" + failed +
                ", source='" + source + '\'' +
                '}';
        }
    }
