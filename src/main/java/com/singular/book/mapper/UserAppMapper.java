package com.singular.book.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.singular.book.entity.Role;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserStatus;
import com.singular.book.vo.UserAppVO;

@Component
public class UserAppMapper {

    public UserApp mapToEntity(UserAppVO vo, UserStatus status, Set<Role> roles) {
        UserApp user = new UserApp();
        user.setId(vo.getId());
        user.setName(vo.getName());
        user.setLogin(vo.getLogin());
        user.setStatus(status);
        if (roles != null) {
            user.setRoles(roles);
        }
        return user;
    }

    public UserAppVO mapToVO(UserApp user) {
        if (user == null) {
            return null;
        }

        UserAppVO vo = new UserAppVO();
        vo.setId(user.getId());
        vo.setName(user.getName());
        vo.setLogin(user.getLogin());

        if (user.getStatus() != null) {
            vo.setStatusId(user.getStatus().getId());
            vo.setStatusDescription(user.getStatus().getName());
        }

        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            vo.setRoles(roleNames);
        }

        vo.setLastLogin(user.getLastLogin());
        return vo;
    }
}