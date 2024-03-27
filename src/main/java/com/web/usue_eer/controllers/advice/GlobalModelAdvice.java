package com.web.usue_eer.controllers.advice;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.entities.UserNotification;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import com.web.usue_eer.security.services.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Comparator;
import java.util.List;

@ControllerAdvice
public class GlobalModelAdvice {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserNotificationService userNotificationService;

    @Autowired
    public GlobalModelAdvice(UserDetailsServiceImpl userDetailsService, UserNotificationService userNotificationService) {
        this.userDetailsService = userDetailsService;
        this.userNotificationService = userNotificationService;
    }

    @ModelAttribute("notifications")
    public List<UserNotification> addNotificationsToModel() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userDetailsService.findUserByUsername(username);

            return userNotificationService.findUserNotificationsByUserId(user.getId());
        } else return null;
    }

    @ModelAttribute("disciplines")
    public List<Discipline> addDisciplinesToModel() {
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userDetailsService.findUserByUsername(username);

            List<Discipline> disciplines = user.getDisciplines();
            Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);

            disciplines.sort(comparator);
            return disciplines;
        } else return null;
    }
}
