package com.singular.book.controller;

import com.singular.book.service.UserAppService;
import com.singular.book.vo.ChangePasswordVO;
import com.singular.book.vo.LoginVO;
import com.singular.book.vo.UserAppVO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAppController {

    private final UserAppService userAppService;

    public UserAppController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @PostMapping
    public ResponseEntity<UserAppVO> create(@Valid @RequestBody UserAppVO userVo) {
        UserAppVO response = userAppService.create(userVo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAppVO> update(@PathVariable("id") Long userId,
                                            @Valid @RequestBody UserAppVO userVo) {
        UserAppVO response = userAppService.update(userId, userVo);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable("id") Long userId,
                                               @Valid @RequestBody ChangePasswordVO changePasswordVo) {
        userAppService.changePassword(userId, changePasswordVo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable("id") Long userId) {
        userAppService.softDelete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAppVO> findById(@PathVariable("id") Long userId) {
        UserAppVO response = userAppService.findById(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<UserAppVO> findByLogin(@RequestParam("login") String login) {
        UserAppVO response = userAppService.findByLogin(login);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserAppVO>> findAll() {
        List<UserAppVO> response = userAppService.findAll();
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserAppVO> login(@Valid @RequestBody LoginVO loginVo) {
        UserAppVO response = userAppService.login(loginVo);
        return ResponseEntity.ok(response);
    }
}