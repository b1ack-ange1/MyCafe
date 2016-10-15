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
public class User_tb implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String Password;
    private String email;
    private String firstName;
    private String lastName;
    private double hourlyPayment;
    private String userStatus;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private UserGroup_tb userGroup;

    public User_tb() {
    }

    public User_tb(Long userId, String Password, String email, String firstName, String lastName, double hourlyPayment, String userStatus, UserGroup_tb userGroup) {
        this.userId = userId;
        this.Password = Password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hourlyPayment = hourlyPayment;
        this.userStatus = userStatus;
        this.userGroup = userGroup;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getHourlyPayment() {
        return hourlyPayment;
    }

    public void setHourlyPayment(double hourlyPayment) {
        this.hourlyPayment = hourlyPayment;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public UserGroup_tb getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup_tb userGroup) {
        this.userGroup = userGroup;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
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
        final User_tb other = (User_tb) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "Entity.User_tb[ userId=" + userId + " ]";
    }
    
}
