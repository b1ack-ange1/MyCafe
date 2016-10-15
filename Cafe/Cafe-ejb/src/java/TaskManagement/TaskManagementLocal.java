package TaskManagement;

import Entity.SwapPermission_tb;
import Entity.Task_tb;
import java.util.List;

public interface TaskManagementLocal {

    public Long createTask(Task_tb task);
    
    public boolean updateTask(Task_tb task);
    
    public boolean deleteTask(Long taskId);
    
    public Task_tb retrieveTaskByTaskId(Long taskId);
    
    public List<Task_tb> retrieveAllTasks();
    
    public List<Task_tb> retrieveTaskByUserId(Long userId);
    
    public boolean createSwapPermission(SwapPermission_tb swapPermission);
    
    public boolean updateSwapPermission(SwapPermission_tb swapPermission);

    public boolean deleteSwapPermission(Long swapPermissionId);
    
    public SwapPermission_tb retrieveSwapPermissionBySwapPermissionId(Long swapPermissionId);
    
    public List<SwapPermission_tb> retrieveAllSwapPermissions();
    
    public List<SwapPermission_tb> retrieveSwapPermissionByTaskId(Long taskId);
    
    public List<SwapPermission_tb> retrieveSwapPermissionByOwnerId(Long ownerId);
    
    public List<SwapPermission_tb> retrieveSwapPermissionByTakerId(Long takerId);
    
}
