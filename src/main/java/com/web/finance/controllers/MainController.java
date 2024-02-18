package com.web.finance.controllers;

import com.web.finance.repository.DisciplineRepository;
import com.web.finance.security.services.DisciplineService;
import com.web.finance.security.services.GroupService;
import com.web.finance.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private UserDetailsServiceImpl userDetailsService;
    private GroupService groupService;
    private DisciplineService disciplineService;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @GetMapping("/portal")
    public String index(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(disciplineRepository.findDisciplinesByUsername(username));
        model.addAttribute("disciplines", disciplineRepository.findDisciplinesByUsername(username));
        return "index";
    }
}
