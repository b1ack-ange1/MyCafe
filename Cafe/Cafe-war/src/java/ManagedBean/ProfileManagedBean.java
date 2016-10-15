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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;


@Named(value = "profileManagedBean")
@ViewScoped
public class ProfileManagedBean implements Serializable {
    
    @Inject
    private LoginManagedBean loginManagedBean;
    
    private User_tb user;
    
    
    public ProfileManagedBean() {
        user = new User_tb();
    }
    
    @PostConstruct
    public void init() {
        user = loginManagedBean.getAccountManagementRemote().getUser();
    }

    public LoginManagedBean getLoginManagedBean() {
        return loginManagedBean;
    }

    public void setLoginManagedBean(LoginManagedBean loginManagedBean) {
        this.loginManagedBean = loginManagedBean;
    }

    public User_tb getUser() {
        return user;
    }

    public void setUser(User_tb user) {
        this.user = user;
    }
    
}
