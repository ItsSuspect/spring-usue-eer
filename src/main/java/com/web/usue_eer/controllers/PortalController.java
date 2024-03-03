package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.repository.DisciplineRepository;
import com.web.usue_eer.repository.UserRepository;
import com.web.usue_eer.security.services.DisciplineService;
import com.web.usue_eer.security.services.GroupService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller
public class PortalController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("/portal")
    public String index(Model model) {
        model.addAttribute("disciplines", getDisciplines());
        return "index";
    }

    @GetMapping("/portal/disciplines")
    public String disciplines(Model model) {
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/disciplines");
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/{category}")
    public String getDisciplineCategoryContent(@PathVariable("disciplineId") Long disciplineId,
                                               @PathVariable("category") String category,
                                               Model model) {

        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("category", category);
        model.addAttribute("disciplineId", disciplineId);

        return "index";
    }

    public List<Discipline> getDisciplines () {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<Discipline> disciplines = user.getDisciplines();
        Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);

        disciplines.sort(comparator);
        return disciplines;
    }
}