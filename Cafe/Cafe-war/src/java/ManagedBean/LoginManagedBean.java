package ManagedBean;

import AccountManagement.AccountManagementRemote;
import Entity.User_tb;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@Named(value = "loginManagedBean")
@SessionScoped
public class LoginManagedBean implements Serializable {
    
    @EJB
    private AccountManagementRemote accountManagementRemote;
    
    private User_tb user;
    private String email;
    private String password;
    private ExternalContext ec;
    private boolean isloggedIn;
    
    @PostConstruct
    public void init() {
        isloggedIn = false;
    }
    
    public LoginManagedBean() {
        user = new User_tb();
        email = "";
        password = "";
    }
    
    public void userLogin() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            String outcome = accountManagementRemote.userLogin(email, password);
            if (outcome.equals("Success")) {
                if (accountManagementRemote.getUser().getUserStatus().equalsIgnoreCase("Pending")) {
                    ec.redirect("pending.xhtml?faces-redirect=true");
                } else if (accountManagementRemote.getUser().getUserStatus().equalsIgnoreCase("Approved")) {
                    user = accountManagementRemote.getUser();
                    isloggedIn = true;
                    if(user.getUserGroup().getUserGroupRole().equals("Admin")){
                        ec.redirect("dashboard_admin.xhtml?faces-redirect=true");
                    } else {
                        ec.redirect("dashboard_mine.xhtml?faces-redirect=true");
                    }
                    
                }
            } else if (outcome.equals("Password Not Match")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Not Match", null));
            } else if (outcome.equals("User Not Exist")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Not Exist", null));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void redirectProfile(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void logout() {
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            accountManagementRemote.logout();
            
            ((HttpSession) ec.getSession(true)).invalidate();
            ec.invalidateSession();
            ec.redirect("landing.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isLoggin(){
        return isloggedIn;
    }
    
    public AccountManagementRemote getAccountManagementRemote() {
        return accountManagementRemote;
    }
    
    public void setAccountManagementRemote(AccountManagementRemote accountManagementRemote) {
        this.accountManagementRemote = accountManagementRemote;
    }

    public boolean adminLogin(){
        if (user.getEmail() == null){
            return false;
        }
        return user.getUserGroup().getUserGroupRole().equals("Admin");
    }
    
    public User_tb getUser() {
        return user;
    }
    
    public void setUser(User_tb user) {
        this.user = user;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public ExternalContext getEc() {
        return ec;
    }
    
    public void setEc(ExternalContext ec) {
        this.ec = ec;
    }
    
}
