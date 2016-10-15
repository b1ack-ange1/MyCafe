/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author XUAN
 */
@Entity
public class SwapPermission_tb implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swapPermissionId;
    private String permissionStatus;
    private String bossStatus;
    private String takerStatus;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User_tb taker;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User_tb owner;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Task_tb task;

    public SwapPermission_tb() {
    }

    public SwapPermission_tb(Long swapPermissionId, String permissionStatus, String bossStatus, String takerStatus, User_tb taker, User_tb owner, Task_tb task) {
        this.swapPermissionId = swapPermissionId;
        this.permissionStatus = permissionStatus;
        this.bossStatus = bossStatus;
        this.takerStatus = takerStatus;
        this.taker = taker;
        this.owner = owner;
        this.task = task;
    }

    public Long getSwapPermissionId() {
        return swapPermissionId;
    }

    public void setSwapPermissionId(Long swapPermissionId) {
        this.swapPermissionId = swapPermissionId;
    }

    public String getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(String permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public String getBossStatus() {
        return bossStatus;
    }

    public void setBossStatus(String bossStatus) {
        this.bossStatus = bossStatus;
    }

    public String getTakerStatus() {
        return takerStatus;
    }

    public void setTakerStatus(String takerStatus) {
        this.takerStatus = takerStatus;
    }

    public User_tb getTaker() {
        return taker;
    }

    public void setTaker(User_tb taker) {
        this.taker = taker;
    }

    public User_tb getOwner() {
        return owner;
    }

    public void setOwner(User_tb owner) {
        this.owner = owner;
    }

    public Task_tb getTask() {
        return task;
    }

    public void setTask(Task_tb task) {
        this.task = task;
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
        final SwapPermission_tb other = (SwapPermission_tb) obj;
        if (!Objects.equals(this.swapPermissionId, other.swapPermissionId)) {
            return false;
        }
        if (!Objects.equals(this.permissionStatus, other.permissionStatus)) {
            return false;
        }
        if (!Objects.equals(this.bossStatus, other.bossStatus)) {
            return false;
        }
        if (!Objects.equals(this.takerStatus, other.takerStatus)) {
            return false;
        }
        if (!Objects.equals(this.taker, other.taker)) {
            return false;
        }
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        if (!Objects.equals(this.task, other.task)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwapPermission_tb{" + "swapPermissionId=" + swapPermissionId + '}';
    }
    
}
