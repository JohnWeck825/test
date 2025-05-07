package com.myshop.adminpage.controller.admin;

import com.myshop.adminpage.dto.UserDto;
import com.myshop.adminpage.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/check-unique-add-form")
    public ResponseEntity<String> checkUniqueAddForm( @RequestParam(value = "id", required = false) Integer id,@RequestBody UserDto userDto) {
        String result = userService.handleUserException(id, userDto.getUserName(), userDto.getEmail());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/editUser{id}/check-unique-edit-form")
    public ResponseEntity<String> checkUniqueEditForm(@PathVariable Integer id, @RequestBody UserDto userDto) {
        String result = userService.handleUserException(id, userDto.getUserName(), userDto.getEmail());
        return ResponseEntity.ok().body(result);
    }
}
