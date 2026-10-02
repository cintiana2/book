package com.singular.book.service;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.Role;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserRole;
import com.singular.book.entity.UserStatus;
import com.singular.book.exceptions.BusinessException;
import com.singular.book.mapper.UserAppMapper;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserRoleRepository;
import com.singular.book.repository.UserStatusRepository;
import com.singular.book.vo.ChangePasswordVO;
import com.singular.book.vo.LoginVO;
import com.singular.book.vo.UserAppVO;

@Service
public class UserAppService {

    private static final Long ACTIVE_STATUS_ID = 1L;
    private static final Long INACTIVE_STATUS_ID = 2L;
    private static final Role DEFAULT_ROLE = new Role(1L, "ROLE_USER");

    private final UserAppRepository userAppRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAppMapper userAppMapper;
    private final MessageSource messageSource;
    private final UserRoleRepository userRoleRepository;

    public UserAppService(UserAppRepository userAppRepository,
                          UserStatusRepository userStatusRepository,
                          PasswordEncoder passwordEncoder,
                          UserAppMapper userAppMapper,
                          MessageSource messageSource,
                          UserRoleRepository userRoleRepository) {
        this.userAppRepository = userAppRepository;
        this.userStatusRepository = userStatusRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAppMapper = userAppMapper;
        this.messageSource = messageSource;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public UserAppVO create(UserAppVO userVo) {
        validateDuplicatedLogin(userVo.getLogin());
        validatePasswordPresence(userVo.getPassword());

        Long targetStatusId =  ACTIVE_STATUS_ID;
        UserStatus status = findStatusOrThrow(targetStatusId);

        UserApp user = new UserApp();
        user.setName(userVo.getName());
        user.setLogin(userVo.getLogin());
        user.setPassword(passwordEncoder.encode(userVo.getPassword()));
        user.setStatus(status);
        
        UserRole userRole = new UserRole();
        userRole.setRole(DEFAULT_ROLE);
        userRole.setUser(user);

        UserApp savedUser = userAppRepository.save(user);
        UserRole userRoleSaved = userRoleRepository.save(userRole);
        savedUser.setUserRoles(new HashSet<UserRole>());
        savedUser.getUserRoles().add(userRoleSaved);
        
        return userAppMapper.mapToVO(savedUser);
    }

    @Transactional
    public UserAppVO login(LoginVO loginVo) {
        UserApp user = userAppRepository.findByLogin(loginVo.getLogin())
                .orElseThrow(() -> new BusinessException(getMessage("user.validation.invalid-credentials")));

        if (user.getStatus() != null && INACTIVE_STATUS_ID.equals(user.getStatus().getId())) {
            throw new BusinessException(getMessage("user.validation.inactive"));
        }

        if (!passwordEncoder.matches(loginVo.getPassword(), user.getPassword())) {
            throw new BusinessException(getMessage("user.validation.invalid-credentials"));
        }

        user.setLastLogin(LocalDateTime.now());
        UserApp updatedUser = userAppRepository.save(user);

        return userAppMapper.mapToVO(updatedUser);
    }

    @Transactional
    public UserAppVO update(Long userId, UserAppVO userVo) {
        UserApp user = findUserOrThrow(userId);
        validateLoginUpdate(userVo.getLogin(), user);

        user.setName(userVo.getName());
        user.setLogin(userVo.getLogin());

        if (userVo.getStatusId() != null) {
            UserStatus status = findStatusOrThrow(userVo.getStatusId());
            user.setStatus(status);
        }

        UserApp updatedUser = userAppRepository.save(user);
        return userAppMapper.mapToVO(updatedUser);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordVO changePasswordVo) {
        UserApp user = findUserOrThrow(userId);

        if (!passwordEncoder.matches(changePasswordVo.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(getMessage("user.validation.current-password.incorrect"));
        }

        user.setPassword(passwordEncoder.encode(changePasswordVo.getNewPassword()));
        userAppRepository.save(user);
    }

    @Transactional
    public void softDelete(Long userId) {
        UserApp user = findUserOrThrow(userId);
        UserStatus inactiveStatus = findStatusOrThrow(INACTIVE_STATUS_ID);

        user.setStatus(inactiveStatus);
        userAppRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAppVO findById(Long userId) {
        UserApp user = findUserOrThrow(userId);
        return userAppMapper.mapToVO(user);
    }

    @Transactional(readOnly = true)
    public UserAppVO findByLogin(String login) {
        UserApp user = userAppRepository.findByLogin(login)
                .orElseThrow(() -> new BusinessException(getMessage("user.validation.login.not-found", login)));
        return userAppMapper.mapToVO(user);
    }


    // --- Métodos de Validação e Suporte ---

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    private UserApp findUserOrThrow(Long userId) {
        return userAppRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(getMessage("user.validation.user.not-found", userId)));
    }

    private UserStatus findStatusOrThrow(Long statusId) {
        return userStatusRepository.findById(statusId)
                .orElseThrow(() -> new BusinessException(getMessage("user.validation.status.not-found", statusId)));
    }

    private void validateDuplicatedLogin(String login) {
        if (login != null && userAppRepository.existsByLogin(login)) {
            throw new BusinessException(getMessage("user.validation.login.exists"));
        }
    }

    private void validatePasswordPresence(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(getMessage("user.validation.password.required"));
        }
    }

    private void validateLoginUpdate(String newLogin, UserApp currentUser) {
        if (!currentUser.getLogin().equals(newLogin) && userAppRepository.existsByLogin(newLogin)) {
            throw new BusinessException(getMessage("user.validation.login.exists-other"));
        }
    }
}