package ManagedBean;

import Entity.Task_tb;
import Entity.UserGroup_tb;
import Entity.UserSummary_tb;
import Entity.User_tb;
import TaskManagement.TaskManagementRemote;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;


@Named(value = "dashboardManagedBean")
@ViewScoped
public class DashboardManagedBean implements Serializable {
    
    @EJB
    private TaskManagementRemote taskManagementRemote;
    @Inject
    private LoginManagedBean loginManagedBean;
    
    private User_tb user;
    private List<Task_tb> todayTaskList;
    private UserSummary_tb userSummary;
    private List<Task_tb> allCurrentUserTasks;
    private List<Task_tb> allTasks;
    private int weeklyPercent;
    private int monthlyPercent;
    private ScheduleModel scheduleModel;
    private ScheduleModel scheduleModelAdmin;
    private ScheduleEvent event;
    private Task_tb selectedTask;
    private String currentArrangement;
    private String shiftChoice;
    private String selectedUserEmail;
    private Date selectedDate;
    private Date selectedStart;
    private Date selectedEnd;
    private List<User_tb> myWorkers;
    private User_tb selectedUser;
    private Long selectedRole;
    private List<String> allUserStatus;
    private List<UserGroup_tb> allUserRole;
    
    public DashboardManagedBean() {
        user = new User_tb();
        userSummary = new UserSummary_tb();
        todayTaskList = new ArrayList<>();
        weeklyPercent = 0;
        monthlyPercent = 0;
        currentArrangement = "";
        scheduleModel = new DefaultScheduleModel();
        scheduleModelAdmin = new DefaultScheduleModel();
        event = new DefaultScheduleEvent();
        allCurrentUserTasks = new ArrayList<>();
        allTasks = new ArrayList<>();
        selectedTask = new Task_tb();
        shiftChoice = "";
        selectedUserEmail = "";
        selectedStart = new Date();
        selectedEnd = new Date();
        myWorkers = new ArrayList<>();
        selectedUser = new User_tb();
        selectedRole = new Long(0);
        allUserStatus = new ArrayList<>();
        allUserRole = new ArrayList<>();
    }
    
    @PostConstruct
    public void init() {
        user = loginManagedBean.getAccountManagementRemote().getUser();
        allCurrentUserTasks = taskManagementRemote.retrieveTaskByUserId(user.getUserId());
        allTasks = taskManagementRemote.retrieveAllTasks();
        myWorkers = retrieveAvailableUsers();
        getTodaySchedule();
        generateUserSummary();
        getCurrentUserSchedule();
        getAllSchedule();
        allUserStatus.add("Pending");
        allUserStatus.add("Approved");
        allUserStatus.add("Suspended");
        allUserStatus.add("Delete Account");
        checkAllUserRole();
    }
    
    public void getTodaySchedule(){
        List<Task_tb> allTasks = taskManagementRemote.retrieveAllTasks();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        Date todayStart =  c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE,59);
        Date todayEnd =  c.getTime();
        
        for (int i = 0; i < allTasks.size(); i ++){
            if (allTasks.get(i).getStartTime().after(todayStart)
                    && allTasks.get(i).getEndTime().before(todayEnd)){
                todayTaskList.add(allTasks.get(i));
            }
        }
    }
    
    public void generateUserSummary(){
        
        boolean hasCreated = false;
        Date generateDate = new Date();
        int tempMonth = generateDate.getMonth()+1;
        int tempYear = generateDate.getYear() -100;
        String monthYear = "" + tempMonth + tempYear;
        List<UserSummary_tb> userSummaryList = loginManagedBean.getAccountManagementRemote().retrieveUserSummaryByUserId(user.getUserId());
        for (int i = 0; i < userSummaryList.size(); i ++){
            if (userSummaryList.get(i).getMonthYear().equals(monthYear)){
                userSummary = userSummaryList.get(i);
                hasCreated = true;
            }
        }
        
        double weeklyHour = getWeeklyHour();
        double monthlyHour = getMonthlyHour();
        double totalHour = getTotalHour();
        double weeklyPayment = weeklyHour * user.getHourlyPayment();
        double monthlyPayment = monthlyHour * user.getHourlyPayment();
        double addOnMonthlyPayment = 0;
        for (int i = 0; i < userSummaryList.size(); i ++){
            if (!userSummaryList.get(i).getMonthYear().equals(monthYear)){
                addOnMonthlyPayment += userSummaryList.get(i).getMonthlyPayment();
            }
        }
        double totalPayment = monthlyPayment + addOnMonthlyPayment;
        userSummary.setGenerateDate(generateDate);
        userSummary.setMonthYear(monthYear);
        userSummary.setMonthlyPayment(monthlyPayment);
        userSummary.setMonthlyWorkingHour(monthlyHour);
        userSummary.setTotalPayment(totalPayment);
        userSummary.setTotalWorkingHour(totalHour);
        userSummary.setWeeklyPayment(weeklyPayment);
        userSummary.setWeeklyWorkingHour(weeklyHour);
        if (!hasCreated){
            userSummary.setUser(user);
            loginManagedBean.getAccountManagementRemote().createUserSummary(userSummary);
        } else {
            loginManagedBean.getAccountManagementRemote().updateUserSummary(userSummary);
        }
    }
    
    public double getWeeklyHour (){
        double weeklyHour = 0;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        Date monday =  c.getTime();
        
        c.add(Calendar.DATE, 7);
        Date monday2 = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        
        for (int i = 0; i < allCurrentUserTasks.size();i ++){
            if (allCurrentUserTasks.get(i).getStartTime().after(monday) && allCurrentUserTasks.get(i).getEndTime().before(monday2)){
                weeklyHour += allCurrentUserTasks.get(i).getDuration();
            }
        }
        return weeklyHour;
    }
    
    public List<String> retrieveShiftList(){
        List<String> shiftList = new ArrayList<>();
        shiftList.add("Full");
        shiftList.add("Morning");
        shiftList.add("Afternoon");
        return shiftList;
    }
    
    public void updateTask(Task_tb task){
        if (currentArrangement.equals("edit")){
            if (shiftChoice.equals("Full")){
                Date newS = task.getStartTime();
                newS.setHours(10);
                task.setStartTime(newS);
                Date newE = task.getEndTime();
                newE.setHours(22);
                task.setEndTime(newE);
            } else if (shiftChoice.equals("Morning")){
                Date newS = task.getStartTime();
                newS.setHours(10);
                task.setStartTime(newS);
                Date newE = task.getEndTime();
                newE.setHours(16);
                task.setEndTime(newE);
                System.out.println("newS = " + newS);
                System.out.println("newE = " + newE);
            } else if (shiftChoice.equals("Afternoon")){
                Date newS = task.getStartTime();
                newS.setHours(16);
                task.setStartTime(newS);
                Date newE = task.getEndTime();
                newE.setHours(22);
                task.setEndTime(newE);
            }
            task.setUser(loginManagedBean.getAccountManagementRemote().retrieveUserByEmail(selectedUserEmail));
            taskManagementRemote.updateTask(task);
        } else {
            taskManagementRemote.deleteTask(task.getTaskId());
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard_admin.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createNewTask(){
        Task_tb newTask = new Task_tb();
        selectedStart.setYear(new Date().getYear());
        newTask.setStartTime(selectedStart);
        selectedEnd.setYear(new Date().getYear());
        newTask.setEndTime(selectedEnd);
        double durationTime = selectedEnd.getHours() - selectedStart.getHours();
        System.out.println("durationTime = " + durationTime);
        newTask.setDuration(durationTime);
        String day = "";
        switch(selectedDate.getDay()){
            case 0:
                day = "Sun";
                break;
            case 1:
                day = "Mon";
                break;
            case 2:
                day = "Tue";
                break;
            case 3:
                day = "Wed";
                break;
            case 4:
                day = "Thu";
                break;
            case 5:
                day = "Fri";
                break;
            case 6:
                day = "Sat";
                break;
        }
        newTask.setTaskDay(day);
        Long taskId = taskManagementRemote.createTask(newTask);
        newTask = taskManagementRemote.retrieveTaskByTaskId(taskId);
        User_tb currentTaskTaker = loginManagedBean.getAccountManagementRemote().retrieveUserByEmail(selectedUserEmail);
        System.out.println("id = " + currentTaskTaker.getUserId());
        newTask.setUser(currentTaskTaker);
        taskManagementRemote.updateTask(newTask);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard_admin.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<User_tb> retrieveAvailableUsers(){
        return loginManagedBean.getAccountManagementRemote().retrieveAllActiveUsers();
    }
    
    public double getMonthlyHour(){
        double monthlyHour = 0;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        Date firstDay =  c.getTime();
        
        c.add(Calendar.MONTH, 1);
        Date nextFirstDay = c.getTime();
        for (int i = 0; i < allCurrentUserTasks.size();i ++){
            if (allCurrentUserTasks.get(i).getStartTime().after(firstDay) && allCurrentUserTasks.get(i).getEndTime().before(nextFirstDay)){
                monthlyHour += allCurrentUserTasks.get(i).getDuration();
            }
        }
        return monthlyHour;
    }
    
    public double getTotalHour(){
        double totalHour = 0;
        for (int i = 0; i < allCurrentUserTasks.size();i ++){
            totalHour += allCurrentUserTasks.get(i).getDuration();
        }
        return totalHour;
    }
    
    public int generateWeeklyPercent(){
        double weeklyHour = 0;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        c.add(Calendar.DATE, -7);
        Date monday =  c.getTime();
        
        c.add(Calendar.DATE, 7);
        Date monday2 = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        
        for (int i = 0; i < allCurrentUserTasks.size();i ++){
            if (allCurrentUserTasks.get(i).getStartTime().after(monday) && allCurrentUserTasks.get(i).getEndTime().before(monday2)){
                weeklyHour += allCurrentUserTasks.get(i).getDuration();
            }
        }
        
        Double tempPercent = userSummary.getWeeklyWorkingHour()/weeklyHour * 100;
        weeklyPercent = tempPercent.intValue();
        return weeklyPercent;
    }
    
    public int generateMonthlyPercent(){
        double monthlyHour = 0;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        c.add(Calendar.MONTH, -1);
        Date firstDay =  c.getTime();
        
        c.add(Calendar.MONTH, 1);
        Date nextFirstDay = c.getTime();
        for (int i = 0; i < allCurrentUserTasks.size();i ++){
            if (allCurrentUserTasks.get(i).getStartTime().after(firstDay) && allCurrentUserTasks.get(i).getEndTime().before(nextFirstDay)){
                monthlyHour += allCurrentUserTasks.get(i).getDuration();
            }
        }
        Double tempPercent = userSummary.getMonthlyWorkingHour()/monthlyHour * 100;
        monthlyPercent = tempPercent.intValue();
        return monthlyPercent;
    }
    
    public void getCurrentUserSchedule(){
        for (int i = 0; i < allCurrentUserTasks.size(); i ++){
            Task_tb task = allCurrentUserTasks.get(i);
            Date start = task.getStartTime();
            Date end = task.getEndTime();
            long difference = end.getTime() - start.getTime();
            String title = "";
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
            scheduleModel.addEvent(new DefaultScheduleEvent(title,start,end));
        }
    }
    public void getAllSchedule(){
        for (int i = 0; i < allTasks.size(); i ++){
            Task_tb task = allTasks.get(i);
            Date start = task.getStartTime();
            Date end = task.getEndTime();
            String title = task.getUser().getFirstName();
            
            scheduleModelAdmin.addEvent(new DefaultScheduleEvent(title,start,end,task));
        }
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        selectedTask = (Task_tb)event.getData();
    }
    
    public void onDateSelect(SelectEvent selectEvent) {
        selectedDate = (Date) selectEvent.getObject();
        selectedStart = (Date) selectEvent.getObject();
        selectedEnd = (Date) selectEvent.getObject();
    }
    
    public boolean retrieveCurrentArrangementType(){
        if (currentArrangement == null){
            return true;
        } else {
            return currentArrangement.equals("edit");
        }
    }
    
    public void updateSelectedUser(){
        selectedUser.setUserGroup(loginManagedBean.getAccountManagementRemote().retrieveUserGroupByUserGroupId(selectedRole));
        loginManagedBean.getAccountManagementRemote().updateUser(selectedUser);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard_admin.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(ScheduleManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printShift(){
        System.out.println("Shift = " + shiftChoice);
    }
    
    public void printSelectedUser(){
        System.out.println("printSelectedUser = " + selectedUserEmail);
    }
    
    public void onRowSelect(SelectEvent event) {
        System.out.println("selectedUser = " + (User_tb) event.getObject());
    }
    
    public void checkAllUserRole(){
        allUserRole = loginManagedBean.getAccountManagementRemote().retrieveAllUserGroups();
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
    
    public List<Task_tb> getTodayTaskList() {
        return todayTaskList;
    }
    
    public void setTodayTaskList(List<Task_tb> todayTaskList) {
        this.todayTaskList = todayTaskList;
    }
    
    public UserSummary_tb getUserSummary() {
        return userSummary;
    }
    
    public void setUserSummary(UserSummary_tb userSummary) {
        this.userSummary = userSummary;
    }
    
    public List<Task_tb> getAllCurrentUserTasks() {
        return allCurrentUserTasks;
    }
    
    public void setAllCurrentUserTasks(List<Task_tb> allCurrentUserTasks) {
        this.allCurrentUserTasks = allCurrentUserTasks;
    }
    
    public int getWeeklyPercent() {
        return weeklyPercent;
    }
    
    public void setWeeklyPercent(int weeklyPercent) {
        this.weeklyPercent = weeklyPercent;
    }
    
    public int getMonthlyPercent() {
        return monthlyPercent;
    }
    
    public void setMonthlyPercent(int monthlyPercent) {
        this.monthlyPercent = monthlyPercent;
    }
    
    public ScheduleModel getScheduleModel() {
        return scheduleModel;
    }
    
    public void setScheduleModel(ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
    }
    
    public ScheduleEvent getEvent() {
        return event;
    }
    
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
    
    public ScheduleModel getScheduleModelAdmin() {
        return scheduleModelAdmin;
    }
    
    public void setScheduleModelAdmin(ScheduleModel scheduleModelAdmin) {
        this.scheduleModelAdmin = scheduleModelAdmin;
    }
    
    public List<Task_tb> getAllTasks() {
        return allTasks;
    }
    
    public void setAllTasks(List<Task_tb> allTasks) {
        this.allTasks = allTasks;
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
    
    public String getShiftChoice() {
        return shiftChoice;
    }
    
    public void setShiftChoice(String shiftChoice) {
        this.shiftChoice = shiftChoice;
    }
    
    public String getSelectedUserEmail() {
        return selectedUserEmail;
    }
    
    public void setSelectedUserEmail(String selectedUserEmail) {
        this.selectedUserEmail = selectedUserEmail;
    }
    
    public Date getSelectedDate() {
        return selectedDate;
    }
    
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
    
    public Date getSelectedStart() {
        return selectedStart;
    }
    
    public void setSelectedStart(Date selectedStart) {
        this.selectedStart = selectedStart;
    }
    
    public Date getSelectedEnd() {
        return selectedEnd;
    }
    
    public void setSelectedEnd(Date selectedEnd) {
        this.selectedEnd = selectedEnd;
    }
    
    public List<User_tb> getMyWorkers() {
        return myWorkers;
    }
    
    public void setMyWorkers(List<User_tb> myWorkers) {
        this.myWorkers = myWorkers;
    }
    
    public User_tb getSelectedUser() {
        return selectedUser;
    }
    
    public void setSelectedUser(User_tb selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public Long getSelectedRole() {
        return selectedRole;
    }
    
    public void setSelectedRole(Long selectedRole) {
        this.selectedRole = selectedRole;
    }
    
    public List<String> getAllUserStatus() {
        return allUserStatus;
    }
    
    public void setAllUserStatus(List<String> allUserStatus) {
        this.allUserStatus = allUserStatus;
    }
    
    public List<UserGroup_tb> getAllUserRole() {
        return allUserRole;
    }
    
    public void setAllUserRole(List<UserGroup_tb> allUserRole) {
        this.allUserRole = allUserRole;
    }
}
