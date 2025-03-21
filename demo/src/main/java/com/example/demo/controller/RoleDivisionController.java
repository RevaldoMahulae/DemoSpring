package com.example.demo.controller;

import java.text.ParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Division;
import com.example.demo.model.Role;
import com.example.demo.service.UserService;
import com.example.demo.util.Constants;


@RestController
@RequestMapping("")
@CrossOrigin(origins = "*")
public class RoleDivisionController {
	
    @Autowired
    private UserService userService;
	
    @GetMapping(Constants.API_ROLE)
    public ResponseEntity<List<Role>> getAllRoles(
            @RequestParam(defaultValue = Constants.PARAM_ID) String sortBy,
            @RequestParam(defaultValue = Constants.DEFAULT_DIRECTION) String order) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<Role> roles = userService.getAllRoles(sortBy, direction);
        return ResponseEntity.ok(roles);
    }

    @GetMapping(Constants.API_DIVISION)
    public ResponseEntity<List<Division>> getAllDivision(
            @RequestParam(defaultValue = Constants.PARAM_ID) String sortBy,
            @RequestParam(defaultValue = Constants.DEFAULT_DIRECTION) String order) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<Division> divisions = userService.getAllDivision(sortBy, direction);
        return ResponseEntity.ok(divisions);
    }
	
}
