package com.twenty4.controller;

import com.twenty4.DTO.UserDTO;
import com.twenty4.common.ResponseObject;
import com.twenty4.entity.Role;
import com.twenty4.entity.User;
import com.twenty4.enums.Status;
import com.twenty4.repository.RoleRepository;
import com.twenty4.repository.UserRepository;
import com.twenty4.service.BaseService;
import com.twenty4.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("${base.url}/user")
public class UserController extends BaseService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @MutationMapping
    User register( @Argument UserDTO user){

        Role role =  roleRepository.findById(user.getRole()).orElseThrow(() -> new IllegalArgumentException("Author not found!!!"));

        User u =   new User(user.getEmail(), encryptPassword(user.getPassword()) , user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getAddress());
        u.setRole(role);
        u.setStatus(Status.ACTIVE);

        return userRepository.save(u);
    }

    @QueryMapping
    User findUser(@Argument Long id){
        return userRepository.findById(id).get();
    }

    @QueryMapping
    Iterable<User> findAll(){
        return userRepository.findAll();
    }

    @Operation(summary = "HI", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/hello")
    public String hello(){

        String methodName = "HI";

        return methodName;

    }

}
