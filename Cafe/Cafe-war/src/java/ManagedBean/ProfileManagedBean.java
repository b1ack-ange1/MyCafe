package ManagedBean;

import Entity.User_tb;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;


@Named(value = "profileManagedBean")
@ViewScoped
public class ProfileManagedBean implements Serializable {
    
    @Inject
    private LoginManagedBean loginManagedBean;
    
    private User_tb user;
    private String editArea;
    
    
    public ProfileManagedBean() {
        user = new User_tb();
        editArea = "";
    }
    
    @PostConstruct
    public void init() {
        user = loginManagedBean.getAccountManagementRemote().getUser();
    }
    
    public boolean showEditArea(){
        return editArea.equals("name");
    }
    
    public void updateUser(){
        loginManagedBean.getAccountManagementRemote().updateUser(user);
        loginManagedBean.getAccountManagementRemote().setUser(user);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String showHourlyPayment(){
        return (user.getHourlyPayment()==0) ? "∞":"$"+user.getHourlyPayment();
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
    
    public String getEditArea() {
        return editArea;
    }
    
    public void setEditArea(String editArea) {
        this.editArea = editArea;
    }
    
}
