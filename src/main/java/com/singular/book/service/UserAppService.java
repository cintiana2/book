package com.singular.book.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.Role;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserStatus;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserStatusRepository;
import com.singular.book.vo.ChangePasswordVO;
import com.singular.book.vo.LoginVO;
import com.singular.book.vo.UserAppVO;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserAppService {

    private static final Long ACTIVE_STATUS_ID = 1L;
    private static final Long INACTIVE_STATUS_ID = 2L;
    private static Role  DEFAULT_ROLE = new Role(1L, "ROLE_USER");


    private final UserAppRepository userAppRepository;
    private final UserStatusRepository userStatusRepository;
   
    private final PasswordEncoder passwordEncoder;

    public UserAppService(UserAppRepository userAppRepository,
                          UserStatusRepository userStatusRepository,
                         
                          PasswordEncoder passwordEncoder) {
        this.userAppRepository = userAppRepository;
        this.userStatusRepository = userStatusRepository;
       
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAppVO create(UserAppVO userVo) {
        if (userAppRepository.existsByLogin(userVo.getLogin())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este login.");
        }

        if (userVo.getPassword() == null || userVo.getPassword().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória para o cadastro.");
        }

        Long targetStatusId = userVo.getStatusId() != null ? userVo.getStatusId() : ACTIVE_STATUS_ID;
        
        UserStatus status = userStatusRepository.findById(targetStatusId)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado para o ID informado."));

       

        UserApp user = new UserApp();
        user.setName(userVo.getName());
        user.setLogin(userVo.getLogin());
        user.setPassword(passwordEncoder.encode(userVo.getPassword()));
        user.setStatus(status);
        
        user.getRoles().add(DEFAULT_ROLE);

        UserApp savedUser = userAppRepository.save(user);
        return toVo(savedUser);
    }

    @Transactional
    public UserAppVO login(LoginVO loginVo) {
        UserApp user = userAppRepository.findByLogin(loginVo.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Login ou senha incorretos."));

        if (user.getStatus() != null && INACTIVE_STATUS_ID.equals(user.getStatus().getId())) {
            throw new IllegalStateException("Usuário inativo. Acesso negado.");
        }

        if (!passwordEncoder.matches(loginVo.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Login ou senha incorretos.");
        }

        // Atualiza a data do último login e consequentemente a data de atualização
        user.setLastLogin(LocalDateTime.now());
        UserApp updatedUser = userAppRepository.save(user);

        return toVo(updatedUser);
    }

    @Transactional
    public UserAppVO update(Long userId, UserAppVO userVo) {
        UserApp user = userAppRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!user.getLogin().equals(userVo.getLogin()) && userAppRepository.existsByLogin(userVo.getLogin())) {
            throw new IllegalArgumentException("O novo login informado já está em uso por outro usuário.");
        }

        user.setName(userVo.getName());
        user.setLogin(userVo.getLogin());

        if (userVo.getStatusId() != null) {
            UserStatus status = userStatusRepository.findById(userVo.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Status não encontrado para o ID informado."));
            user.setStatus(status);
        }

        UserApp updatedUser = userAppRepository.save(user);
        return toVo(updatedUser);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordVO changePasswordVo) {
        UserApp user = userAppRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(changePasswordVo.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        user.setPassword(passwordEncoder.encode(changePasswordVo.getNewPassword()));
        userAppRepository.save(user);
    }

    @Transactional
    public void softDelete(Long userId) {
        UserApp user = userAppRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        UserStatus inactiveStatus = userStatusRepository.findById(INACTIVE_STATUS_ID)
                .orElseThrow(() -> new EntityNotFoundException("Status INATIVO não cadastrado no sistema."));

        user.setStatus(inactiveStatus);
        userAppRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAppVO findById(Long userId) {
        UserApp user = userAppRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        return toVo(user);
    }

    @Transactional(readOnly = true)
    public UserAppVO findByLogin(String login) {
        UserApp user = userAppRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o login informado."));
        return toVo(user);
    }

    @Transactional(readOnly = true)
    public List<UserAppVO> findAll() {
        List<UserApp> userList = userAppRepository.findAll();
        List<UserAppVO> voList = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            voList.add(toVo(userList.get(i)));
        }

        return voList;
    }

    private UserAppVO toVo(UserApp user) {
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