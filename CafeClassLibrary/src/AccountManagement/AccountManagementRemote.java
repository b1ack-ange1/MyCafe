package AccountManagement;

import Entity.UserGroup_tb;
import Entity.UserSummary_tb;
import Entity.User_tb;
import java.util.List;

public interface AccountManagementRemote {
    
    public String userLogin(String email, String password);
    
    public Boolean logout();
    
    public boolean createUser(User_tb user);
    
    public boolean updateUser(User_tb user);
    
    public boolean deleteUser(Long userId);
    
    public User_tb retrieveUserByUserId(Long userId);
    
    public User_tb retrieveUserByEmail(String email);
    
    public List<User_tb> retrieveAllActiveUsers();
    
    public List<User_tb> retrieveAllUsers();
    
    public List<User_tb> retrieveUserByUserGroupId(Long userGroupId);
    
    public boolean createUserGroup(UserGroup_tb userGroup);
    
    public boolean updateUserGroup(UserGroup_tb userGroup);
    
    public boolean deleteUserGroup(Long userGroupId);
    
    public UserGroup_tb retrieveUserGroupByUserGroupId(Long userGroupId);
    
    public List<UserGroup_tb> retrieveAllUserGroups();
    
    public boolean createUserSummary(UserSummary_tb userSummary);
    
    public boolean updateUserSummary(UserSummary_tb userSummary);
    
    public boolean deleteUserSummary(Long userSummaryId);
    
    public UserSummary_tb retrieveUserSummaryByUserSummaryId(Long userSummaryId);
    
    public List<UserSummary_tb> retrieveAllUserSummarys();
    
    public List<UserSummary_tb> retrieveUserSummaryByUserId(Long userId);
    
    public User_tb getUser();
    
    public void setUser(User_tb user);
    
}
