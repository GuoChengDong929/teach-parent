package com.access.business.access.repository;

import com.teach.entity.access.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    @Query(nativeQuery = true, value = "select u.* from pe_user u ,pe_teacher t where  u.id = t.id AND t.department_id = ?3 limit ?1,?2")
    List<User> getTeacherList(Integer page, Integer size, String departmentId);

    @Query(nativeQuery = true, value = "select count(u.id) from pe_user u ,pe_teacher t where  u.id = t.id AND t.department_id = ?")
    long getTeacherCount(String departmentId);
}
