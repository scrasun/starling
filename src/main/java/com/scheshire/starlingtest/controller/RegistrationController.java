package com.scheshire.starlingtest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.scheshire.starlingtest.models.User;
import com.scheshire.starlingtest.repo.UserRepo;

@Controller
public class RegistrationController {
	@Autowired
    private UserRepo userRepo;
	
	@GetMapping("")
	public String showIndex(Model model, Authentication authentication)
	{	
		model.addAttribute("loggedin", authentication != null);
		
		return "index";
	}
	
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		User userDto = new User();
	    model.addAttribute("user", userDto);
	    return "regform";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@Valid User userDto, BindingResult bindingResult) {
		System.out.println("ahh");
		if (userRepo.findByEmail(userDto.getEmail())!=null)
		{
			bindingResult.rejectValue("email", "user.error", "already there");
		}
		if (bindingResult.hasErrors())
		{
			System.out.println("err");
			return "regform";
		}
		userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		userRepo.save(userDto);
		return "registered";
		
	}
}
