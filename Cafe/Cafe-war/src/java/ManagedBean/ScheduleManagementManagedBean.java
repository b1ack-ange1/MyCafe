package ManagedBean;

import Entity.SwapPermission_tb;
import Entity.Task_tb;
import Entity.User_tb;
import TaskManagement.TaskManagementRemote;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;


@Named(value = "scheduleManagementManagedBean")
@ViewScoped
public class ScheduleManagementManagedBean implements Serializable {
    
    @EJB
    private TaskManagementRemote taskManagementRemote;
    @Inject
    private LoginManagedBean loginManagedBean;
    
    private User_tb user;
    private List<Task_tb> allCurrentUserTasks;
    private List<Task_tb> currentWeekTasks;
    private List<SwapPermission_tb> swapPermissions;
    private List<SwapPermission_tb> swapPermissionsFromOthers;
    private Task_tb selectedTask;
    private SwapPermission_tb selectedSwapPermission;
    private String currentArrangement;
    private String requestArrangement;
    private String requestFromOthersArrangement;
    private List<User_tb> allUsers;
    private Long myTaker;
    
    public ScheduleManagementManagedBean() {
        user = new User_tb();
        allCurrentUserTasks = new ArrayList<>();
        currentWeekTasks = new ArrayList<>();
        swapPermissions = new ArrayList<>();
        swapPermissionsFromOthers = new ArrayList<>();
        selectedTask = new Task_tb();
        currentArrangement = "";
        requestArrangement = "";
        allUsers = new ArrayList<>();
        myTaker = new Long(1);
        selectedSwapPermission = new SwapPermission_tb();
        requestFromOthersArrangement = "";
    }
    
    @PostConstruct
    public void init() {
        user = loginManagedBean.getAccountManagementRemote().getUser();
        allCurrentUserTasks = taskManagementRemote.retrieveTaskByUserId(user.getUserId());
        swapPermissions = getUnsolvedSwapPermissions();
        allUsers = retrieveAllPossibleTakers();
        swapPermissionsFromOthers = getUnsolvedSwapPermissionsFromOthers();
        getWeeklyTasks();
    }
    
    public void getWeeklyTasks(){
        for (int i = 0; i < allCurrentUserTasks.size(); i ++){
            if (allCurrentUserTasks.get(i).getStartTime().after(new Date())){
                currentWeekTasks.add(allCurrentUserTasks.get(i));
            }
        }
        List<Date> dates = new ArrayList<>();
        for (int i = 0; i < allCurrentUserTasks.size(); i ++){
            dates.add(allCurrentUserTasks.get(i).getStartTime());
        }
        Collections.sort(dates);
        List<Task_tb> myTasks = new ArrayList<>();
        for (int i = 0; i < dates.size(); i ++){
            for(int j = 0; j < currentWeekTasks.size(); j++){
                if (currentWeekTasks.get(j).getStartTime().equals(dates.get(i))){
                    myTasks.add(currentWeekTasks.get(j));
                    break;
                }
            }
        }
        currentWeekTasks = myTasks;
    }
    
    public String getCurrentUserSchedule(Task_tb task){
        String title = "";
        Date start = task.getStartTime();
        Date end = task.getEndTime();
        long difference = end.getTime() - start.getTime();
        
        // 21600000 is 6 hours
        if (difference <= 21600000){
            if (start.getHours()<13){
                title = "Morning";
            } else {
                title = "Afternoon";
            }
        } else {
            title = "Full";
        }
        return title;
    }
    
    public List<SwapPermission_tb> getUnsolvedSwapPermissions(){
        List<SwapPermission_tb> tempPermissions = taskManagementRemote.retrieveSwapPermissionByOwnerId(user.getUserId());
        List<SwapPermission_tb> returnPermissions = new ArrayList<>();
        for (int i = 0; i < tempPermissions.size(); i++ ){
            if(tempPermissions.get(i).getTask().getStartTime().after(new Date())){
                returnPermissions.add(tempPermissions.get(i));
            }
        }
        List<Date> dates = new ArrayList<>();
        for (int i = 0; i < returnPermissions.size(); i ++){
            dates.add(returnPermissions.get(i).getTask().getStartTime());
        }
        Collections.sort(dates);
        List<SwapPermission_tb> myTasks = new ArrayList<>();
        for (int i = 0; i < dates.size(); i ++){
            for(int j = 0; j < returnPermissions.size(); j++){
                if (returnPermissions.get(j).getTask().getStartTime().equals(dates.get(i))){
                    myTasks.add(returnPermissions.get(j));
                    break;
                }
            }
        }
        return myTasks;
    }
    
    public List<SwapPermission_tb> getUnsolvedSwapPermissionsFromOthers(){
        List<SwapPermission_tb> permissionsFromOthers = taskManagementRemote.retrieveAllSwapPermissions();
        List<SwapPermission_tb> finalPermissions = new ArrayList<>();
        for (int i = 0; i < permissionsFromOthers.size(); i++ ){
            if(permissionsFromOthers.get(i).getTask().getStartTime().after(new Date()) &&
                    !permissionsFromOthers.get(i).getTask().getUser().getEmail().equals(user.getEmail())){
                finalPermissions.add(permissionsFromOthers.get(i));
            }
        }
        return finalPermissions;
    }
    
    public String getTakerFirstName(SwapPermission_tb swapPermission){
        return (swapPermission.getTaker() == null) ? "Replacement" : swapPermission.getTaker().getFirstName();
    }
    
    public String getTakerFirstName2(SwapPermission_tb swapPermission){
        return (swapPermission.getTaker() == null) ? "Unknown" : swapPermission.getTaker().getFirstName();
    }
    
    public boolean retrieveCurrentArrangementType(){
        if (currentArrangement == null){
            return true;
        } else {
            return currentArrangement.equals("withTaker");
        }
    }
    
    public boolean retrieveRequestArrangementType(){
        
        if (requestArrangement == null){
            return true;
        } else {
            return requestArrangement.equals("edit");
        }
    }
    
    public void permissionManagement(SwapPermission_tb swapPermission){
        if (requestArrangement.equals("edit")){
            swapPermission.setTaker(loginManagedBean.getAccountManagementRemote().retrieveUserByUserId(myTaker));
            taskManagementRemote.updateSwapPermission(swapPermission);
        } else {
            taskManagementRemote.deleteSwapPermission(swapPermission.getSwapPermissionId());
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("schedule_management.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void permissionFromOthersManagemnet(SwapPermission_tb swapPermission){
        if (requestFromOthersArrangement.equals("accept")){
            swapPermission.setTaker(user);
            swapPermission.setTakerStatus("Accepted");
            if (swapPermission.getBossStatus().equals("Approved")){
                swapPermission.setPermissionStatus("Approved");
                Task_tb swapTask = swapPermission.getTask();
                swapTask.setUser(swapPermission.getTaker());
                taskManagementRemote.updateTask(swapTask);
            }
            taskManagementRemote.updateSwapPermission(swapPermission);
        } else {
            if (swapPermission.getTaker().getEmail().equals(user.getEmail()) && swapPermission.getTakerStatus().equals("Approved")){
                swapPermission.setTaker(null);
                swapPermission.setTakerStatus("Pending");
                taskManagementRemote.updateSwapPermission(swapPermission);
            }
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard_admin.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void permissionFromOthersManagemnetAdmin(SwapPermission_tb swapPermission){
        swapPermission.setBossStatus("Approved");
        if (swapPermission.getTakerStatus().equals("Accepted")){
            swapPermission.setPermissionStatus("Approved");
            Task_tb swapTask = swapPermission.getTask();
            swapTask.setUser(swapPermission.getTaker());
            taskManagementRemote.updateTask(swapTask);
        }
        taskManagementRemote.updateSwapPermission(swapPermission);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard_admin.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<User_tb> retrieveAllPossibleTakers(){
        List <User_tb> tempUserList = loginManagedBean.getAccountManagementRemote().retrieveUserByUserGroupId(new Long(2));
        for (int i = 0; i < tempUserList.size(); i ++){
            if (tempUserList.get(i).getEmail().equals(user.getEmail()) || tempUserList.get(i).getUserStatus().equals("Pending") || tempUserList.get(i).getUserStatus().equals("Delete")){
                tempUserList.remove(i);
            }
        }
        return tempUserList;
    }
    
    public void generateNewRequest(Task_tb task){
        SwapPermission_tb newPermission = new SwapPermission_tb();
        newPermission.setOwner(user);
        newPermission.setBossStatus("Pending");
        newPermission.setPermissionStatus("Pending");
        if (currentArrangement.equals("withTaker")){
            newPermission.setTaker(loginManagedBean.getAccountManagementRemote().retrieveUserByUserId(myTaker));
        } else {
            newPermission.setTaker(null);
        }
        newPermission.setTakerStatus("Pending");
        newPermission.setTask(task);
        taskManagementRemote.createSwapPermission(newPermission);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("schedule_management.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String takerNameWithStatus(SwapPermission_tb swapPermission){
        if (swapPermission.getTaker() == null){
            return "Unknown";
        } else {
            return swapPermission.getTaker().getFirstName() + " (" + swapPermission.getTakerStatus() + ")";
        }
    }
    public void printTaker(){
        System.out.println("requestTaker = " + myTaker);
    }
    
    public boolean hasRequests(){
        return swapPermissions.isEmpty();
    }
    
    public boolean hasRequestsFromOthers(){
        return swapPermissionsFromOthers.isEmpty();
    }
    
    public void getTheSelectedTask (Task_tb task){
        selectedTask = task;
    }
    
    public void getTheSelectedSwapPermission (SwapPermission_tb swapPermission){
        selectedSwapPermission = swapPermission;
    }
    
    public TaskManagementRemote getTaskManagementRemote() {
        return taskManagementRemote;
    }
    
    public void setTaskManagementRemote(TaskManagementRemote taskManagementRemote) {
        this.taskManagementRemote = taskManagementRemote;
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
    
    public List<Task_tb> getAllCurrentUserTasks() {
        return allCurrentUserTasks;
    }
    
    public void setAllCurrentUserTasks(List<Task_tb> allCurrentUserTasks) {
        this.allCurrentUserTasks = allCurrentUserTasks;
    }
    
    public List<Task_tb> getCurrentWeekTasks() {
        return currentWeekTasks;
    }
    
    public void setCurrentWeekTasks(List<Task_tb> currentWeekTasks) {
        this.currentWeekTasks = currentWeekTasks;
    }
    
    public List<SwapPermission_tb> getSwapPermissions() {
        return swapPermissions;
    }
    
    public void setSwapPermissions(List<SwapPermission_tb> swapPermissions) {
        this.swapPermissions = swapPermissions;
    }
    
    public Task_tb getSelectedTask() {
        return selectedTask;
    }
    
    public void setSelectedTask(Task_tb selectedTask) {
        this.selectedTask = selectedTask;
    }
    
    public String getCurrentArrangement() {
        return currentArrangement;
    }
    
    public void setCurrentArrangement(String currentArrangement) {
        this.currentArrangement = currentArrangement;
    }
    
    public List<User_tb> getAllUsers() {
        return allUsers;
    }
    
    public void setAllUsers(List<User_tb> allUsers) {
        this.allUsers = allUsers;
    }
    
    public Long getMyTaker() {
        return myTaker;
    }
    
    public void setMyTaker(Long myTaker) {
        this.myTaker = myTaker;
    }
    
    public List<SwapPermission_tb> getSwapPermissionsFromOthers() {
        return swapPermissionsFromOthers;
    }
    
    public void setSwapPermissionsFromOthers(List<SwapPermission_tb> swapPermissionsFromOthers) {
        this.swapPermissionsFromOthers = swapPermissionsFromOthers;
    }
    
    public SwapPermission_tb getSelectedSwapPermission() {
        return selectedSwapPermission;
    }
    
    public void setSelectedSwapPermission(SwapPermission_tb selectedSwapPermission) {
        this.selectedSwapPermission = selectedSwapPermission;
    }
    
    public String getRequestArrangement() {
        return requestArrangement;
    }
    
    public void setRequestArrangement(String requestArrangement) {
        this.requestArrangement = requestArrangement;
    }
    
    public String getRequestFromOthersArrangement() {
        return requestFromOthersArrangement;
    }
    
    public void setRequestFromOthersArrangement(String requestFromOthersArrangement) {
        this.requestFromOthersArrangement = requestFromOthersArrangement;
    }
}
