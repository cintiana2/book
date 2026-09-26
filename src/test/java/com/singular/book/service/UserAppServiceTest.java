package com.singular.book.service;

import com.singular.book.entity.Role;
import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserStatus;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserStatusRepository;
import com.singular.book.vo.ChangePasswordVO;
import com.singular.book.vo.LoginVO;
import com.singular.book.vo.UserAppVO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAppServiceTest {

    @Mock
    private UserAppRepository userAppRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAppService userAppService;

    private UserApp userApp;
    private UserStatus activeStatus;
    private UserStatus inactiveStatus;
    private Role defaultRole;
    private UserAppVO userAppVO;

    @BeforeEach
    void setUp() {
        activeStatus = new UserStatus();
        activeStatus.setId(1L);
        activeStatus.setName("ATIVO");

        inactiveStatus = new UserStatus();
        inactiveStatus.setId(2L);
        inactiveStatus.setName("INATIVO");

        defaultRole = new Role();
        defaultRole.setId(1L);
        defaultRole.setName("ROLE_USER");

        userApp = new UserApp();
        userApp.setId(1L);
        userApp.setName("Cíntia Araújo");
        userApp.setLogin("cintia.araujo");
        userApp.setPassword("encodedPassword123");
        userApp.setStatus(activeStatus);
        userApp.getRoles().add(defaultRole);

        userAppVO = new UserAppVO();
        userAppVO.setName("Cíntia Araújo");
        userAppVO.setLogin("cintia.araujo");
        userAppVO.setPassword("rawPassword123");
        userAppVO.setStatusId(1L);
    }

    @Nested
    @DisplayName("Testes do Método Create")
    class CreateTests {

        @Test
        @DisplayName("Deve criar utilizador com sucesso e atribuir ROLE_USER")
        void shouldCreateUserSuccessfully() {
            when(userAppRepository.existsByLogin(userAppVO.getLogin())).thenReturn(false);
            when(userStatusRepository.findById(1L)).thenReturn(Optional.of(activeStatus));
            when(passwordEncoder.encode(userAppVO.getPassword())).thenReturn("encodedPassword123");
            when(userAppRepository.save(any(UserApp.class))).thenReturn(userApp);

            UserAppVO result = userAppService.create(userAppVO);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Cíntia Araújo", result.getName());
            assertEquals("cintia.araujo", result.getLogin());
            assertEquals("ATIVO", result.getStatusDescription());
            assertTrue(result.getRoles().contains("ROLE_USER"));

            verify(userAppRepository).existsByLogin(userAppVO.getLogin());
            verify(passwordEncoder).encode("rawPassword123");
            verify(userAppRepository).save(any(UserApp.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o login já existir")
        void shouldThrowExceptionWhenLoginAlreadyExists() {
            when(userAppRepository.existsByLogin(userAppVO.getLogin())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userAppService.create(userAppVO)
            );

            assertEquals("Já existe um usuário cadastrado com este login.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando a senha for nula ou vazia")
        void shouldThrowExceptionWhenPasswordIsBlank() {
            userAppVO.setPassword("");
            when(userAppRepository.existsByLogin(userAppVO.getLogin())).thenReturn(false);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userAppService.create(userAppVO)
            );

            assertEquals("A senha é obrigatória para o cadastro.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o status informado não for encontrado")
        void shouldThrowExceptionWhenStatusNotFoundOnCreate() {
            when(userAppRepository.existsByLogin(userAppVO.getLogin())).thenReturn(false);
            when(userStatusRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> userAppService.create(userAppVO)
            );

            assertEquals("Status não encontrado para o ID informado.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }

    }

    @Nested
    @DisplayName("Testes do Método Login")
    class LoginTests {

        @Test
        @DisplayName("Deve realizar login com sucesso e atualizar lastLogin")
        void shouldLoginSuccessfully() {
            LoginVO loginVO = new LoginVO();
            loginVO.setLogin("cintia.araujo");
            loginVO.setPassword("rawPassword123");

            when(userAppRepository.findByLogin("cintia.araujo")).thenReturn(Optional.of(userApp));
            when(passwordEncoder.matches("rawPassword123", "encodedPassword123")).thenReturn(true);
            when(userAppRepository.save(any(UserApp.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UserAppVO result = userAppService.login(loginVO);

            assertNotNull(result);
            assertEquals("cintia.araujo", result.getLogin());
            assertNotNull(result.getLastLogin());

            verify(userAppRepository).save(userApp);
        }

        @Test
        @DisplayName("Deve lançar exceção no login se o usuário estiver inativo")
        void shouldThrowExceptionWhenUserIsInactiveOnLogin() {
            LoginVO loginVO = new LoginVO();
            loginVO.setLogin("cintia.araujo");
            loginVO.setPassword("rawPassword123");

            userApp.setStatus(inactiveStatus);
            when(userAppRepository.findByLogin("cintia.araujo")).thenReturn(Optional.of(userApp));

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> userAppService.login(loginVO)
            );

            assertEquals("Usuário inativo. Acesso negado.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção no login se a senha estiver incorreta")
        void shouldThrowExceptionWhenPasswordIsIncorrectOnLogin() {
            LoginVO loginVO = new LoginVO();
            loginVO.setLogin("cintia.araujo");
            loginVO.setPassword("wrongPassword");

            when(userAppRepository.findByLogin("cintia.araujo")).thenReturn(Optional.of(userApp));
            when(passwordEncoder.matches("wrongPassword", "encodedPassword123")).thenReturn(false);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userAppService.login(loginVO)
            );

            assertEquals("Login ou senha incorretos.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção no login se o login não for encontrado")
        void shouldThrowExceptionWhenUserNotFoundOnLogin() {
            LoginVO loginVO = new LoginVO();
            loginVO.setLogin("inexistente");
            loginVO.setPassword("rawPassword123");

            when(userAppRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userAppService.login(loginVO)
            );

            assertEquals("Login ou senha incorretos.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do Método Update")
    class UpdateTests {

        @Test
        @DisplayName("Deve atualizar utilizador com sucesso")
        void shouldUpdateUserSuccessfully() {
            UserAppVO updateVO = new UserAppVO();
            updateVO.setName("Cíntia Nogueira");
            updateVO.setLogin("cintia.nogueira");
            updateVO.setStatusId(1L);

            when(userAppRepository.findById(1L)).thenReturn(Optional.of(userApp));
            when(userAppRepository.existsByLogin("cintia.nogueira")).thenReturn(false);
            when(userStatusRepository.findById(1L)).thenReturn(Optional.of(activeStatus));

            UserApp updatedEntity = new UserApp();
            updatedEntity.setId(1L);
            updatedEntity.setName("Cíntia Nogueira");
            updatedEntity.setLogin("cintia.nogueira");
            updatedEntity.setStatus(activeStatus);

            when(userAppRepository.save(any(UserApp.class))).thenReturn(updatedEntity);

            UserAppVO result = userAppService.update(1L, updateVO);

            assertNotNull(result);
            assertEquals("Cíntia Nogueira", result.getName());
            assertEquals("cintia.nogueira", result.getLogin());

            verify(userAppRepository).save(userApp);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar se utilizador não for encontrado")
        void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
            when(userAppRepository.findById(99L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> userAppService.update(99L, userAppVO)
            );

            assertEquals("Usuário não encontrado.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção se novo login já estiver em uso por outro utilizador")
        void shouldThrowExceptionWhenNewLoginIsAlreadyTaken() {
            UserAppVO updateVO = new UserAppVO();
            updateVO.setLogin("login.existente");

            when(userAppRepository.findById(1L)).thenReturn(Optional.of(userApp));
            when(userAppRepository.existsByLogin("login.existente")).thenReturn(true);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userAppService.update(1L, updateVO)
            );

            assertEquals("O novo login informado já está em uso por outro usuário.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do Método ChangePassword")
    class ChangePasswordTests {

        @Test
        @DisplayName("Deve alterar senha com sucesso")
        void shouldChangePasswordSuccessfully() {
            ChangePasswordVO changePasswordVO = new ChangePasswordVO();
            changePasswordVO.setCurrentPassword("rawPassword123");
            changePasswordVO.setNewPassword("newPassword456");

            when(userAppRepository.findById(1L)).thenReturn(Optional.of(userApp));
            when(passwordEncoder.matches("rawPassword123", "encodedPassword123")).thenReturn(true);
            when(passwordEncoder.encode("newPassword456")).thenReturn("encodedNewPassword456");

            userAppService.changePassword(1L, changePasswordVO);

            verify(userAppRepository).save(userApp);
            assertEquals("encodedNewPassword456", userApp.getPassword());
        }

        @Test
        @DisplayName("Deve lançar exceção se a senha atual estiver incorreta")
        void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
            ChangePasswordVO changePasswordVO = new ChangePasswordVO();
            changePasswordVO.setCurrentPassword("wrongPassword");
            changePasswordVO.setNewPassword("newPassword456");

            when(userAppRepository.findById(1L)).thenReturn(Optional.of(userApp));
            when(passwordEncoder.matches("wrongPassword", "encodedPassword123")).thenReturn(false);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> userAppService.changePassword(1L, changePasswordVO)
            );

            assertEquals("A senha atual informada está incorreta.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do Método SoftDelete")
    class SoftDeleteTests {

        @Test
        @DisplayName("Deve inativar utilizador com sucesso")
        void shouldSoftDeleteUserSuccessfully() {
            when(userAppRepository.findById(1L)).thenReturn(Optional.of(userApp));
            when(userStatusRepository.findById(2L)).thenReturn(Optional.of(inactiveStatus));

            userAppService.softDelete(1L);

            assertEquals(inactiveStatus, userApp.getStatus());
            verify(userAppRepository).save(userApp);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar inativar utilizador inexistente")
        void shouldThrowExceptionWhenUserNotFoundOnSoftDelete() {
            when(userAppRepository.findById(99L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                    () -> userAppService.softDelete(99L)
            );

            assertEquals("Usuário não encontrado.", exception.getMessage());
            verify(userAppRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Consulta (Find)")
    class FindTests {

        @Test
        @DisplayName("Deve buscar utilizador por ID com sucesso")
        void shouldFindUserById() {
            when(userAppRepository.findById(1L)).thenReturn(Optional.of(userApp));

            UserAppVO result = userAppService.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("cintia.araujo", result.getLogin());
        }

        @Test
        @DisplayName("Deve buscar utilizador por login com sucesso")
        void shouldFindUserByLogin() {
            when(userAppRepository.findByLogin("cintia.araujo")).thenReturn(Optional.of(userApp));

            UserAppVO result = userAppService.findByLogin("cintia.araujo");

            assertNotNull(result);
            assertEquals("cintia.araujo", result.getLogin());
        }

        @Test
        @DisplayName("Deve listar todos os utilizadores mapeando para VO")
        void shouldFindAllUsers() {
            UserApp user2 = new UserApp();
            user2.setId(2L);
            user2.setName("Outro Utilizador");
            user2.setLogin("outro.login");
            user2.setStatus(activeStatus);

            when(userAppRepository.findAll()).thenReturn(List.of(userApp, user2));

            List<UserAppVO> resultList = userAppService.findAll();

            assertNotNull(resultList);
            assertEquals(2, resultList.size());
            assertEquals("cintia.araujo", resultList.get(0).getLogin());
            assertEquals("outro.login", resultList.get(1).getLogin());
        }
    }
}