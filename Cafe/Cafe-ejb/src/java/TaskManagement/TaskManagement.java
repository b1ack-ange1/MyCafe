package TaskManagement;

import Entity.SwapPermission_tb;
import Entity.Task_tb;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(TaskManagementLocal.class)
@Remote(TaskManagementRemote.class)
public class TaskManagement implements TaskManagementRemote, TaskManagementLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Long createTask(Task_tb task) {
        try {
            em.persist(task);
            em.flush();
            return task.getTaskId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean updateTask(Task_tb task){
        try {
            em.merge(task);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteTask(Long taskId){
        try {
            Task_tb deleteTask = em.find(Task_tb.class, taskId);
            em.remove(deleteTask);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Task_tb retrieveTaskByTaskId(Long taskId){
        try {
            String jpql = "SELECT t FROM Task_tb t WHERE t.taskId = " + "'" + taskId + "'";
            Query query = em.createQuery(jpql);
            return (Task_tb) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new Task_tb();
        }
    }
    
    @Override
    public List<Task_tb> retrieveAllTasks(){
        List<Task_tb> tasks = null;
        try {
            String jpql = "SELECT t FROM Task_tb t";
            Query query = em.createQuery(jpql);
            tasks = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return tasks;
    }
    
    @Override
    public List<Task_tb> retrieveTaskByUserId(Long userId){
        List<Task_tb> tasks = null;
        try {
            String jpql = "SELECT t FROM Task_tb t WHERE t.user.userId = " + userId;
            Query query = em.createQuery(jpql);
            tasks = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return tasks;
    }
  
    @Override
    public boolean createSwapPermission(SwapPermission_tb swapPermission) {
        try {
            em.persist(swapPermission);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateSwapPermission(SwapPermission_tb swapPermission){
        try {
            em.merge(swapPermission);
            em.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteSwapPermission(Long swapPermissionId){
        try {
            SwapPermission_tb deleteSwapPermission = em.find(SwapPermission_tb.class, swapPermissionId);
            em.remove(deleteSwapPermission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public SwapPermission_tb retrieveSwapPermissionBySwapPermissionId(Long swapPermissionId){
        try {
            String jpql = "SELECT sp FROM SwapPermission_tb sp WHERE sp.swapPermissionId = " + "'" + swapPermissionId + "'";
            Query query = em.createQuery(jpql);
            return (SwapPermission_tb) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            return new SwapPermission_tb();
        }
    }
    
    @Override
    public List<SwapPermission_tb> retrieveAllSwapPermissions(){
        List<SwapPermission_tb> swapPermissions = null;
        try {
            String jpql = "SELECT sp FROM SwapPermission_tb sp";
            Query query = em.createQuery(jpql);
            swapPermissions = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return swapPermissions;
    }
    
    @Override
    public List<SwapPermission_tb> retrieveSwapPermissionByTaskId(Long taskId){
        List<SwapPermission_tb> swapPermissions = null;
        try {
            String jpql = "SELECT sp FROM SwapPermission_tb sp WHERE sp.task.taskId = " + taskId;
            Query query = em.createQuery(jpql);
            swapPermissions = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return swapPermissions;
    }
    
    @Override
    public List<SwapPermission_tb> retrieveSwapPermissionByOwnerId(Long ownerId){
        List<SwapPermission_tb> swapPermissions = null;
        try {
            String jpql = "SELECT sp FROM SwapPermission_tb sp WHERE sp.owner.userId = " + ownerId;
            Query query = em.createQuery(jpql);
            swapPermissions = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return swapPermissions;
    }
    
    @Override
    public List<SwapPermission_tb> retrieveSwapPermissionByTakerId(Long takerId){
        List<SwapPermission_tb> swapPermissions = null;
        try {
            String jpql = "SELECT sp FROM SwapPermission_tb sp WHERE sp.taker.userId = " + takerId;
            Query query = em.createQuery(jpql);
            swapPermissions = query.getResultList();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return swapPermissions;
    }
}
