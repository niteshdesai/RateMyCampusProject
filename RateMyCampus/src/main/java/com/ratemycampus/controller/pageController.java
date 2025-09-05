package com.ratemycampus.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ratemycampus.entity.College;
import com.ratemycampus.repository.CollegeRepository;


@Controller
public class pageController {
     
	@Autowired
	private CollegeRepository collegeRepository;
	
	@GetMapping("/")
	public String gethomepage()
	{
		return "home";
	}
	@GetMapping("/addCollege")
	public String addCollege()
	{
		return "collegeAdd";
	}
	
//	@GetMapping("/getImage/{id}")
//    public String getimage(@PathVariable("id") Long id, Model model)
//    {
//		String imageName=collegeRepository.getById(id).getCimg(); 
//		model.addAttribute("imageName", imageName);
//    	return "showimage";
//    }
	
	@GetMapping("/college-detail/{id}")
	public String getCollegeDetail(@PathVariable("id") long id,Model model)
	{
		model.addAttribute("college",new College());
		model.addAttribute("Id", id);
		return "college-detail";
	}
	
	@GetMapping("/login")
	public String getLogin()
	{
		return "login";
	}
}
