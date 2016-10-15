/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author XUAN
 */
@Entity
public class Task_tb implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String taskDay;
    private double duration;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "task")
    private List<SwapPermission_tb> swapPermissions;
    @ManyToOne(fetch = FetchType.EAGER, optional = false,cascade = CascadeType.PERSIST)
    private User_tb user;

    public Task_tb() {
    }

    public Task_tb(Long taskId, String taskDay, double duration, Date startTime, Date endTime, List<SwapPermission_tb> swapPermissions, User_tb user) {
        this.taskId = taskId;
        this.taskDay = taskDay;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.swapPermissions = swapPermissions;
        this.user = user;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDay() {
        return taskDay;
    }

    public void setTaskDay(String taskDay) {
        this.taskDay = taskDay;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<SwapPermission_tb> getSwapPermissions() {
        return swapPermissions;
    }

    public void setSwapPermissions(List<SwapPermission_tb> swapPermissions) {
        this.swapPermissions = swapPermissions;
    }

    public User_tb getUser() {
        return user;
    }

    public void setUser(User_tb user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task_tb other = (Task_tb) obj;
        if (!Objects.equals(this.taskId, other.taskId)) {
            return false;
        }
        if (!Objects.equals(this.taskDay, other.taskDay)) {
            return false;
        }
        if (Double.doubleToLongBits(this.duration) != Double.doubleToLongBits(other.duration)) {
            return false;
        }
        if (!Objects.equals(this.startTime, other.startTime)) {
            return false;
        }
        if (!Objects.equals(this.endTime, other.endTime)) {
            return false;
        }
        if (!Objects.equals(this.swapPermissions, other.swapPermissions)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Task_tb{" + "taskId=" + taskId + '}';
    }
    
}
