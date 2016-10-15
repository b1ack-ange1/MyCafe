/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author XUAN
 */
@Entity
public class UserGroup_tb implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupId;
    private String userGroupRole;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userGroup",cascade = CascadeType.PERSIST)
    private List<User_tb> users;

    public UserGroup_tb() {
    }

    public UserGroup_tb(Long userGroupId, String userGroupRole, List<User_tb> users) {
        this.userGroupId = userGroupId;
        this.userGroupRole = userGroupRole;
        this.users = users;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupRole() {
        return userGroupRole;
    }

    public void setUserGroupRole(String userGroupRole) {
        this.userGroupRole = userGroupRole;
    }

    public List<User_tb> getUsers() {
        return users;
    }

    public void setUsers(List<User_tb> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final UserGroup_tb other = (UserGroup_tb) obj;
        if (!Objects.equals(this.userGroupId, other.userGroupId)) {
            return false;
        }
        if (!Objects.equals(this.userGroupRole, other.userGroupRole)) {
            return false;
        }
        if (!Objects.equals(this.users, other.users)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserGroup_tb{" + "userGroupId=" + userGroupId + '}';
    }

}
