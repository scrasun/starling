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

/**
 * Controller for user registration
 */
@Controller
public class RegistrationController {
	@Autowired
    private UserRepo userRepo;
	
	/**
	 * Get index page
	 * @param model Model for frontend
	 * @param authentication User auth
	 * @return index template
	 */
	@GetMapping("")
	public String showIndex(Model model, Authentication authentication)
	{	
		model.addAttribute("loggedin", authentication != null);
		
		return "index";
	}
	
	/**
	 * Get registration page
	 * @param model Model for frontend
	 * @return registration form page
	 */
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		User userDto = new User();
	    model.addAttribute("user", userDto);
	    return "regform";
	}

	/**
	 * Register new user
	 * @param user User
	 * @param bindingResult Result
	 * @return new page
	 */
	@PostMapping("/registration")
	public String registerUserAccount(@Valid User userDto, BindingResult bindingResult) {
		if (userRepo.findByEmail(userDto.getEmail())!=null)
		{
			bindingResult.rejectValue("email", "user.error", "already there");
		}
		if (bindingResult.hasErrors())
		{
			return "regform";
		}
		userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		userRepo.save(userDto);
		return "registered";
		
	}
}
