package com.contactManager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.helper.Message;
import com.contactManager.modals.Contact;
import com.contactManager.modals.User;
import com.contactManager.repository.ContactRepository;
import com.contactManager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	//Method for adding common data
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		
		String userName = principal.getName();
		System.out.println("Username " + userName);
		
		//getting user using username
		User user = userRepository.getUserByUserName(userName);
		System.out.println("user " + user);
		m.addAttribute("user",user);
		
	}
	
	//dashboard home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
		return "normal/dashboard";
	}
	
	
	
	//Add contact form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title","add contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}
	
	//process contact handler
	
	@PostMapping("/process-contact")
	public String processContactForm(@ModelAttribute Contact contact, 
			@RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session ) {
		
		try {
		
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		//processing and uploading image
		if(file.isEmpty()) {
			//
			System.out.println("no image");
			contact.setImage("contact.jpg");
		}else {
			//upload image in folder
			
			contact.setImage(file.getOriginalFilename());
			File saveFile = new ClassPathResource("static/images").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is uploaded");
			
		}
		
		contact.setUser(user);
		user.getContacts().add(contact);
		
		userRepository.save(user);
		
		System.out.println("Data " + contact);
		
		//success message
		session.setAttribute("message", new Message("Your contact is added.. Add more","success"));
		
		
		
		}catch (Exception e) {
			e.printStackTrace();
			
			//error message
			session.setAttribute("message", new Message("Something went wrong try again","danger"));
			
		}
		
		return "normal/add_contact";
	}
	
	//view contact handler
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") int page, Model model, Principal principal) {
		
		model.addAttribute("title","show-contacts");
		String userName = principal.getName();
		
		User user = userRepository.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//showing particular contact details
	
	@GetMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") int cId, Model model, Principal principal) {
		System.out.println(cId);
		
		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);

		
		if(user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact",contact);
			model.addAttribute("title", contact.getName());
		}
		
		return "normal/contact_detail";
	}
	
	//Delete contact handler
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cId, HttpSession session, Principal principal) {
		
		Contact contact = contactRepository.findById(cId).get();
		
		
		//contactRepository.delete(contact);
		User user = userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		
		userRepository.save(user);
		
		session.setAttribute("message", new Message("Contact deleted successfully...","success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	//update form handler
	@PostMapping("/update-contact/{cid}")
	public String showUpdateForm(@PathVariable("cid") int cid, Model m) {
		
		m.addAttribute("title","update contact");
		
		Contact contact = contactRepository.findById(cid).get();
		
		m.addAttribute("contact",contact);
		
		return "normal/update_form";
	}
	
	//process update form handle
	@PostMapping("/process-update")
	public String processUpdateForm(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, Model m, HttpSession session, Principal principal) {
		
		try {
			
			Contact oldContactDetail = contactRepository.findById(contact.getcId()).get();
			
			if(!file.isEmpty()) {
				//file rewrite
				
				//delete old photo
				File deleteFile = new ClassPathResource("static/images").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();
				
				
				//update new photo
				File saveFile = new ClassPathResource("static/images").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
				
			}else {
				contact.setImage(oldContactDetail.getImage());
			}
			
			User user = userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			
			contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Your contact is updated..","success"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(contact.getName());
		System.out.println(contact.getEmail());
		return "redirect:/user/contact/"+contact.getcId();
	}
	
	//your profile handler
	
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		m.addAttribute("title","profile page");
		return "normal/profile";
	}

}
