/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author XUAN
 */
@Entity
public class UserSummary_tb implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSummeryId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date generateDate;
    private String monthYear;
    private double weeklyWorkingHour;
    private double monthlyWorkingHour;
    private double totalWorkingHour;
    private double weeklyPayment;
    private double monthlyPayment;
    private double totalPayment;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User_tb user;

    public UserSummary_tb() {
    }

    public UserSummary_tb(Long userSummeryId, Date generateDate, String monthYear, double weeklyWorkingHour, double monthlyWorkingHour, double totalWorkingHour, double weeklyPayment, double monthlyPayment, double totalPayment, User_tb user) {
        this.userSummeryId = userSummeryId;
        this.generateDate = generateDate;
        this.monthYear = monthYear;
        this.weeklyWorkingHour = weeklyWorkingHour;
        this.monthlyWorkingHour = monthlyWorkingHour;
        this.totalWorkingHour = totalWorkingHour;
        this.weeklyPayment = weeklyPayment;
        this.monthlyPayment = monthlyPayment;
        this.totalPayment = totalPayment;
        this.user = user;
    }

    public Long getUserSummeryId() {
        return userSummeryId;
    }

    public void setUserSummeryId(Long userSummeryId) {
        this.userSummeryId = userSummeryId;
    }

    public Date getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public double getWeeklyWorkingHour() {
        return weeklyWorkingHour;
    }

    public void setWeeklyWorkingHour(double weeklyWorkingHour) {
        this.weeklyWorkingHour = weeklyWorkingHour;
    }

    public double getMonthlyWorkingHour() {
        return monthlyWorkingHour;
    }

    public void setMonthlyWorkingHour(double monthlyWorkingHour) {
        this.monthlyWorkingHour = monthlyWorkingHour;
    }

    public double getTotalWorkingHour() {
        return totalWorkingHour;
    }

    public void setTotalWorkingHour(double totalWorkingHour) {
        this.totalWorkingHour = totalWorkingHour;
    }

    public double getWeeklyPayment() {
        return weeklyPayment;
    }

    public void setWeeklyPayment(double weeklyPayment) {
        this.weeklyPayment = weeklyPayment;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public User_tb getUser() {
        return user;
    }

    public void setUser(User_tb user) {
        this.user = user;
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
        final UserSummary_tb other = (UserSummary_tb) obj;
        if (!Objects.equals(this.userSummeryId, other.userSummeryId)) {
            return false;
        }
        if (!Objects.equals(this.generateDate, other.generateDate)) {
            return false;
        }
        if (!Objects.equals(this.monthYear, other.monthYear)) {
            return false;
        }
        if (Double.doubleToLongBits(this.weeklyWorkingHour) != Double.doubleToLongBits(other.weeklyWorkingHour)) {
            return false;
        }
        if (Double.doubleToLongBits(this.monthlyWorkingHour) != Double.doubleToLongBits(other.monthlyWorkingHour)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totalWorkingHour) != Double.doubleToLongBits(other.totalWorkingHour)) {
            return false;
        }
        if (Double.doubleToLongBits(this.weeklyPayment) != Double.doubleToLongBits(other.weeklyPayment)) {
            return false;
        }
        if (Double.doubleToLongBits(this.monthlyPayment) != Double.doubleToLongBits(other.monthlyPayment)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totalPayment) != Double.doubleToLongBits(other.totalPayment)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserSummary_tb{" + "userSummeryId=" + userSummeryId + '}';
    }
}
