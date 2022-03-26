package com.edu.uptc.app.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.uptc.app.entity.User;
import com.edu.uptc.app.services.UserService;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST})

@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody User user){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
	}
	
	 @GetMapping("/hello")
	  public String hello() {
	    return "Hello word";
	  }
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> read(@PathVariable(value = "id") Long userId){
		Optional<User> oUser = userService.findById(userId);
		
		if (!oUser.isPresent()) {
			return ResponseEntity.notFound().build();
			}else {
			  return ResponseEntity.ok(oUser);
			}
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> uptade(@RequestBody User userDetails,@PathVariable(value = "id") long idUser){
		Optional<User> user= userService.findById(idUser);
		
		if (!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
//		BeanUtils.copyProperties(userDetails, user); copiar y pegar todo a la vez
		user.get().setName(userDetails.getName());
		user.get().setEmail(userDetails.getEmail());
		user.get().setEnabled(userDetails.isEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.get()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> uptade(@PathVariable(value ="id") long idUser){
		if (!userService.findById(idUser).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		userService.deleteById(idUser);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public List<User> fill(){
		List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false).collect(Collectors.toList());
		return users;
	}
}
