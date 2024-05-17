package com.contactManager.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactManager.helper.Message;
import com.contactManager.modals.User;
import com.contactManager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String home(Model model) {
		
		model.addAttribute("title","Home - smart contact manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title","about - smart contact manager");
		return "about";
	}
	
	
	@RequestMapping("/signup")
	public String signUp(Model model) {
		
		model.addAttribute("title","register - smart contact manager");
		model.addAttribute("user", new User());

		  
		return "register";
	}
	
	
	//Registration Handler
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result, 
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, 
			Model model, HttpSession session ) {
		
		try {
			
			
			if(!agreement) {
				System.out.println("You have not accepted terms and conditions");
				throw new Exception("You have not accepted terms and conditions");
			}
			
			if(result.hasErrors()) {
				model.addAttribute("user",user);
				return "register";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			User saveUser =  userRepository.save(user);
			System.out.println(saveUser);
			
			model.addAttribute("user",new User());
			
			session.setAttribute("message", new Message("Successfully registered " ,"alert-success"));

			return "register";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong " + e.getMessage(),"alert-danger"));

			return "register";
		}
		
	}
	
	//handler for login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		
		model.addAttribute("title","Login-page");
		return "login";
	}
}
