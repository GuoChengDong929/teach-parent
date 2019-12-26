package com.access.business.access.service;

import com.access.business.access.repository.PermissionRepository;
import com.teach.base.BaseService;
import com.teach.entity.access.Permission;

import com.teach.entity.access.Role;
import com.teach.error.CommonException;
import com.teach.response.Result;
import com.teach.response.ResultCode;
import com.teach.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService extends BaseService {


    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private IdWorker idWorker;

    /**
     * 4.查询全部
     * type      : 查询全部权限列表type：0：菜单 + 按钮（权限点） 1：菜单2：按钮（权限点）3：API接口
     * enVisible : 0：查询所有saas平台的最高权限，1：查询企业的权限
     * pid ：父id
     */
    public List<Permission> findAll(Map<String, Object> map) {
        //1.需要查询条件
        Specification<Permission> spec = new Specification<Permission>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据父id查询
                if(!StringUtils.isEmpty(map.get("pid"))) {
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据enVisible查询
                if(!StringUtils.isEmpty(map.get("enVisible"))) {
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                //根据类型 type
                if(!StringUtils.isEmpty(map.get("type"))) {
                    String ty = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals(ty)) {
                        in.value(1).value(2);
                    }else{
                        in.value(Integer.parseInt(ty));
                    }
                    list.add(in);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionRepository.findAll(spec);
    }

    public Result all() {
        List<Permission> permissions = this.getPermissions("0");
        return new Result(ResultCode.SUCCESS, permissions);
    }


    protected List<Permission> getPermissions(String pid) {
        List<Permission> list = new ArrayList<>();
        Specification<Permission> spec = new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();

                list.add(criteriaBuilder.equal(root.get("pid").as(String.class),pid));

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        List<Permission> permissions = permissionRepository.findAll(spec);

        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                //permission现在已经拿到了,我们就等同于拿到了id, 即下一层的pid
                permission.setChildren(this.getPermissions(permission.getId()));//递归
                list.add(permission);
            }
        }

        return list;
    }

    public Result savePermission(Permission permission) throws CommonException {
        //类型为3的只能有一个 所以需要进行判断
        String pid = permission.getPid();
        Integer type = 3;
        List<Permission> permissions = permissionRepository.findByPidAndType(pid,type);
        if(permissions != null && permissions.size() == 1) return Result.FAIL();
        permission.setId(idWorker.nextId() + "");
        permission.setCreateTime(new Date());

        permission.setModifyId(currentUser().getId());
        permission.setModifyUser(currentUser().getNickName());
        permission.setModifyTime(new Date());
        Permission target = permissionRepository.save(permission);
        if(target != null) return Result.SUCCESS();
        else return Result.FAIL();
    }

    public Result updatePermission(Permission permission) {
        Permission target = permissionRepository.findById(permission.getId()).get();
        BeanUtils.copyProperties(permission,target);

        target.setModifyId(currentUser().getId());
        target.setModifyUser(currentUser().getNickName());
        target.setModifyTime(new Date());
        permissionRepository.save(target);
        return Result.SUCCESS();
    }

    public Result removePermission(String id) {
        //start 写了角色之后的补充: 查看是否有相应的角色绑定了该权限
        int count = permissionRepository.getRolesByPermissionId(id);

        if(count != 0){
            return new Result(ResultCode.PERMISSION_IN_ROLE_HAS_DATA);
        }

        //end

        permissionRepository.deleteById(id);
        return Result.SUCCESS();
    }
}
