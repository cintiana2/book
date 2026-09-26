package com.singular.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.singular.book.entity.UserApp;
import com.singular.book.entity.UserStatus;
import com.singular.book.repository.UserAppRepository;
import com.singular.book.repository.UserStatusRepository;
import com.singular.book.vo.ChangePasswordVO;
import com.singular.book.vo.UserAppVO;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserAppServiceIntegrationTest {

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private UserAppRepository userAppRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserStatus activeStatus;
    private UserStatus inactiveStatus;

    @BeforeEach
    void setUp() {
        // Assegura a existência dos status base de dados no ambiente de testes
        activeStatus = userStatusRepository.findById(1L).orElseGet(() -> {
            UserStatus status = new UserStatus();
            status.setId(1L);
            status.setName("ATIVO");
            return userStatusRepository.save(status);
        });

        inactiveStatus = userStatusRepository.findById(2L).orElseGet(() -> {
            UserStatus status = new UserStatus();
            status.setId(2L);
            status.setName("INATIVO");
            return userStatusRepository.save(status);
        });
    }

    @Test
    @DisplayName("Deve salvar utilizador na base de dados e criptografar a senha")
    void shouldCreateUserAndPersistInDatabase() {
        UserAppVO vo = new UserAppVO();
        vo.setName("Utilizador Teste");
        vo.setLogin("utilizador.teste");
        vo.setPassword("senha123");
        vo.setStatusId(activeStatus.getId());

        UserAppVO createdVo = userAppService.create(vo);

        assertNotNull(createdVo.getId());
        assertEquals("Utilizador Teste", createdVo.getName());
        assertEquals("utilizador.teste", createdVo.getLogin());
        assertEquals("ATIVO", createdVo.getStatusDescription());

        // Validação direta no repositório JPA
        UserApp persistedEntity = userAppRepository.findById(createdVo.getId()).orElse(null);
        assertNotNull(persistedEntity);
        assertTrue(passwordEncoder.matches("senha123", persistedEntity.getPassword()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar login duplicado")
    void shouldThrowExceptionWhenCreatingDuplicateLogin() {
        UserApp existingUser = new UserApp();
        existingUser.setName("Existente");
        existingUser.setLogin("login.duplicado");
        existingUser.setPassword(passwordEncoder.encode("123456"));
        existingUser.setStatus(activeStatus);
        userAppRepository.save(existingUser);

        UserAppVO newVo = new UserAppVO();
        newVo.setName("Novo Utilizador");
        newVo.setLogin("login.duplicado");
        newVo.setPassword("654321");
        newVo.setStatusId(activeStatus.getId());

        assertThrows(IllegalArgumentException.class, () -> userAppService.create(newVo));
    }

    @Test
    @DisplayName("Deve atualizar dados do utilizador na base de dados")
    void shouldUpdateUserInDatabase() {
        UserApp user = new UserApp();
        user.setName("Nome Antigo");
        user.setLogin("login.antigo");
        user.setPassword(passwordEncoder.encode("senha123"));
        user.setStatus(activeStatus);
        UserApp savedUser = userAppRepository.save(user);

        UserAppVO updateVo = new UserAppVO();
        updateVo.setName("Nome Atualizado");
        updateVo.setLogin("login.atualizado");
        updateVo.setStatusId(activeStatus.getId());

        UserAppVO updatedResult = userAppService.update(savedUser.getId(), updateVo);

        assertEquals("Nome Atualizado", updatedResult.getName());
        assertEquals("login.atualizado", updatedResult.getLogin());

        UserApp databaseUser = userAppRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals("Nome Atualizado", databaseUser.getName());
        assertEquals("login.atualizado", databaseUser.getLogin());
    }

    @Test
    @DisplayName("Deve alterar senha e validar o novo hash no banco")
    void shouldChangePasswordSuccessfully() {
        UserApp user = new UserApp();
        user.setName("Utilizador Senha");
        user.setLogin("user.senha");
        user.setPassword(passwordEncoder.encode("senhaAtual123"));
        user.setStatus(activeStatus);
        UserApp savedUser = userAppRepository.save(user);

        ChangePasswordVO changePasswordVO = new ChangePasswordVO();
        changePasswordVO.setCurrentPassword("senhaAtual123");
        changePasswordVO.setNewPassword("novaSenha456");

        userAppService.changePassword(savedUser.getId(), changePasswordVO);

        UserApp updatedUser = userAppRepository.findById(savedUser.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches("novaSenha456", updatedUser.getPassword()));
        assertFalse(passwordEncoder.matches("senhaAtual123", updatedUser.getPassword()));
    }

    @Test
    @DisplayName("Deve realizar a exclusão lógica alterando o status para INATIVO")
    void shouldSoftDeleteUserByChangingStatusToInactive() {
        UserApp user = new UserApp();
        user.setName("Utilizador Deletar");
        user.setLogin("user.deletar");
        user.setPassword(passwordEncoder.encode("senha123"));
        user.setStatus(activeStatus);
        UserApp savedUser = userAppRepository.save(user);

        userAppService.softDelete(savedUser.getId());

        UserApp inactivatedUser = userAppRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals(inactiveStatus.getId(), inactivatedUser.getStatus().getId());
        assertEquals("INATIVO", inactivatedUser.getStatus().getName());
    }

    @Test
    @DisplayName("Deve buscar utilizador por login cadastrado")
    void shouldFindUserByLogin() {
        UserApp user = new UserApp();
        user.setName("Utilizador Busca");
        user.setLogin("busca.login");
        user.setPassword(passwordEncoder.encode("senha123"));
        user.setStatus(activeStatus);
        userAppRepository.save(user);

        UserAppVO foundVo = userAppService.findByLogin("busca.login");

        assertNotNull(foundVo);
        assertEquals("Utilizador Busca", foundVo.getName());
        assertEquals("busca.login", foundVo.getLogin());
    }

    @Test
    @DisplayName("Deve listar todos os utilizadores salvos")
    void shouldFindAllUsers() {
        UserApp user1 = new UserApp();
        user1.setName("User Um");
        user1.setLogin("user.um");
        user1.setPassword("123456");
        user1.setStatus(activeStatus);

        UserApp user2 = new UserApp();
        user2.setName("User Dois");
        user2.setLogin("user.dois");
        user2.setPassword("123456");
        user2.setStatus(activeStatus);

        userAppRepository.saveAll(List.of(user1, user2));

        List<UserAppVO> usersList = userAppService.findAll();

        assertTrue(usersList.size() >= 2);
    }
}