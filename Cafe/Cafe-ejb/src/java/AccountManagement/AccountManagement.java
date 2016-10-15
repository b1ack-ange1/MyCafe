package AccountManagement;


import Entity.UserGroup_tb;
import Entity.UserSummary_tb;
import Entity.User_tb;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateful
@Local(AccountManagementLocal.class)
@Remote(AccountManagementRemote.class)
public class AccountManagement implements AccountManagementRemote, AccountManagementLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    private User_tb user;
    
    public AccountManagement(){
        user = new User_tb();
    }
    
    @Override
    public String userLogin(String email, String password) {
        user = retrieveUserByEmail(email);
        
        if (user.getPassword() != null) {
            if (user.getPassword().equals(password)) {
                return "Success";
            } else {
                return "Password Not Match";
            }
        } else {
            return "User Not Exist";
        }
    }
    
    @Override
    public Boolean logout() {
        user = null;
        return true;
    }
    
    @Override
    public boolean createUser(User_tb user) {
        boolean sameEmail = false;
        List<User_tb> list = new ArrayList<>();
        try {
            String jpql = "SELECT u FROM User_tb u";
            Query query = em.createQuery(jpql);
            list = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmail().equals(user.getEmail())) {
                sameEmail = true;
            }
        }
        if (!sameEmail) {
            em.persist(user);
            em.flush();
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean updateUser(User_tb user){
        try {
            em.merge(user);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(Long userId){
        try {
            User_tb deleteUser = em.find(User_tb.class, userId);
            em.remove(deleteUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public User_tb retrieveUserByUserId(Long userId){
        try {
            String jpql = "SELECT u FROM User_tb u WHERE u.userId = " + "'" + userId + "'";
            Query query = em.createQuery(jpql);
            return (User_tb) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new User_tb();
        }
    }
    
    @Override
    public User_tb retrieveUserByEmail(String email){
        try {
            String jpql = "SELECT u FROM User_tb u WHERE u.email = " + "'" + email + "'";
            Query query = em.createQuery(jpql);
            return (User_tb) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new User_tb();
        }
    }
    
    @Override
    public List<User_tb> retrieveAllUsers(){
        List<User_tb> users = null;
        try {
            String jpql = "SELECT u FROM User_tb u";
            Query query = em.createQuery(jpql);
            users = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return users;
    }
    
    @Override
    public List<User_tb> retrieveAllActiveUsers(){
        List<User_tb> users = null;
        try {
            String jpql = "SELECT u FROM User_tb u WHERE u.userStatus = 'Approved'";
            Query query = em.createQuery(jpql);
            users = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return users;
    }
    
    @Override
    public List<User_tb> retrieveUserByUserGroupId(Long userGroupId){
        List<User_tb> users = null;
        try {
            String jpql = "SELECT u FROM User_tb u WHERE u.userGroup.userGroupId = " + userGroupId;
            Query query = em.createQuery(jpql);
            users = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return users;
    }
    
    @Override
    public boolean createUserGroup(UserGroup_tb userGroup) {
        try{
            em.persist(userGroup);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    
    @Override
    public boolean updateUserGroup(UserGroup_tb userGroup){
        try {
            em.merge(userGroup);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteUserGroup(Long userGroupId){
        try {
            UserGroup_tb userGroup = em.find(UserGroup_tb.class, userGroupId);
            em.remove(userGroup);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public UserGroup_tb retrieveUserGroupByUserGroupId(Long userGroupId){
        try {
            String jpql = "SELECT ug FROM UserGroup_tb ug WHERE ug.userGroupId = " + "'" + userGroupId + "'";
            Query query = em.createQuery(jpql);
            return (UserGroup_tb) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new UserGroup_tb();
        }
    }
    
    @Override
    public List<UserGroup_tb> retrieveAllUserGroups(){
        List<UserGroup_tb> userGroups = null;
        try {
            String jpql = "SELECT ug FROM UserGroup_tb ug";
            Query query = em.createQuery(jpql);
            userGroups = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return userGroups;
    }
      
    @Override
    public boolean createUserSummary(UserSummary_tb userSummary) {
        try {
            em.persist(userSummary);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateUserSummary(UserSummary_tb userSummary){
   
        try {
            em.merge(userSummary);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteUserSummary(Long userSummaryId){
        try {
            UserSummary_tb deleteUserSummary = em.find(UserSummary_tb.class, userSummaryId);
            em.remove(deleteUserSummary);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public UserSummary_tb retrieveUserSummaryByUserSummaryId(Long userSummaryId){
        try {
            String jpql = "SELECT us FROM UserSummary_tb us WHERE us.userSummaryId = " + "'" + userSummaryId + "'";
            Query query = em.createQuery(jpql);
            return (UserSummary_tb) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new UserSummary_tb();
        }
    }
    
    @Override
    public List<UserSummary_tb> retrieveAllUserSummarys(){
        List<UserSummary_tb> userSummarys = null;
        try {
            String jpql = "SELECT us FROM UserSummary_tb us";
            Query query = em.createQuery(jpql);
            userSummarys = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return userSummarys;
    }
    
    @Override
    public List<UserSummary_tb> retrieveUserSummaryByUserId(Long userId){
        List<UserSummary_tb> userSummarys = null;
        try {
            String jpql = "SELECT us FROM UserSummary_tb us WHERE us.user.userId = " + userId;
            Query query = em.createQuery(jpql);
            userSummarys = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return userSummarys;
    }
    
    
    public EntityManager getEm() {
        return em;
    }
    
    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public User_tb getUser() {
        return user;
    }
    
    @Override
    public void setUser(User_tb user) {
        this.user = user;
    }
    
}
