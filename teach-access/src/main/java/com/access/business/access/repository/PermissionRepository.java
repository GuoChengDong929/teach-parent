package com.access.business.access.repository;

import com.teach.entity.access.Permission;
import com.teach.entity.access.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission,String>, JpaSpecificationExecutor<Permission> {


    List<Permission> findByPidAndType(String pid, Integer type);

    @Query(nativeQuery = true , value = "SELECT COUNT(r.id) FROM pe_role r,pe_permission p ,pe_role_permission rp WHERE p.id = rp.permission_id AND rp.role_id = r.id AND p.id = ?")
    int getRolesByPermissionId(String id);
}
