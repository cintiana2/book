package com.singular.book.service;

import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserStatus;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserStatusRepository;
import com.singular.book.vo.ChangePasswordVO;
import com.singular.book.vo.UserAppVO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAppService {

    private static final Long ACTIVE_STATUS_ID = 1L;
    private static final Long INACTIVE_STATUS_ID = 2L;

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
        
        Optional<UserStatus> statusOptional = userStatusRepository.findById(targetStatusId);
        if (statusOptional.isEmpty()) {
            throw new EntityNotFoundException("Status não encontrado para o ID informado.");
        }
        UserStatus status = statusOptional.get();

        UserApp user = new UserApp();
        user.setName(userVo.getName());
        user.setLogin(userVo.getLogin());
        // Criptografia da senha com o algoritmo BCrypt
        user.setPassword(passwordEncoder.encode(userVo.getPassword()));
        user.setStatus(status);

        UserApp savedUser = userAppRepository.save(user);
        return toVo(savedUser);
    }

    @Transactional
    public UserAppVO update(Long userId, UserAppVO userVo) {
        Optional<UserApp> userOptional = userAppRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        UserApp user = userOptional.get();

        // Valida se o login foi alterado e se já existe em outro registro
        if (!user.getLogin().equals(userVo.getLogin()) && userAppRepository.existsByLogin(userVo.getLogin())) {
            throw new IllegalArgumentException("O novo login informado já está em uso por outro usuário.");
        }

        user.setName(userVo.getName());
        user.setLogin(userVo.getLogin());

        if (userVo.getStatusId() != null) {
            Optional<UserStatus> statusOptional = userStatusRepository.findById(userVo.getStatusId());
            if (statusOptional.isEmpty()) {
                throw new EntityNotFoundException("Status não encontrado para o ID informado.");
            }
            user.setStatus(statusOptional.get());
        }

        UserApp updatedUser = userAppRepository.save(user);
        return toVo(updatedUser);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordVO changePasswordVo) {
        Optional<UserApp> userOptional = userAppRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        UserApp user = userOptional.get();

        // Valida se a senha atual fornecida bate com o hash salvo no banco
        if (!passwordEncoder.matches(changePasswordVo.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }

        // Criptografa e grava a nova senha
        user.setPassword(passwordEncoder.encode(changePasswordVo.getNewPassword()));
        userAppRepository.save(user);
    }

    @Transactional
    public void softDelete(Long userId) {
        Optional<UserApp> userOptional = userAppRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        UserApp user = userOptional.get();

        Optional<UserStatus> inactiveStatusOptional = userStatusRepository.findById(INACTIVE_STATUS_ID);
        if (inactiveStatusOptional.isEmpty()) {
            throw new EntityNotFoundException("Status INATIVO não cadastrado no sistema.");
        }

     
        user.setStatus(inactiveStatusOptional.get());
        userAppRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAppVO findById(Long userId) {
        Optional<UserApp> userOptional = userAppRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado.");
        }
        return toVo(userOptional.get());
    }

    @Transactional(readOnly = true)
    public UserAppVO findByLogin(String login) {
        Optional<UserApp> userOptional = userAppRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado para o login informado.");
        }
        return toVo(userOptional.get());
    }

    @Transactional(readOnly = true)
    public List<UserAppVO> findAll() {
        List<UserApp> userList = userAppRepository.findAll();
        List<UserAppVO> voList = new ArrayList<>();

        // Iteração imperativa usando for tradicional (sem Stream)
        for (int i = 0; i < userList.size(); i++) {
            UserApp user = userList.get(i);
            voList.add(toVo(user));
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
        return vo;
    }
}