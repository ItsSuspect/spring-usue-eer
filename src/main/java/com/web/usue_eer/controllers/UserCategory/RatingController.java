package com.web.usue_eer.controllers.UserCategory;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.entities.Task;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.entities.UserTask;
import com.web.usue_eer.payload.response.RatingResponse;
import com.web.usue_eer.payload.response.RatingTasksResponse;
import com.web.usue_eer.payload.response.TaskResponse;
import com.web.usue_eer.security.services.DisciplineService;
import com.web.usue_eer.security.services.TaskService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import com.web.usue_eer.security.services.UserTaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/portal")
public class RatingController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private UserTaskService userTaskService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/rating")
    @Transactional
    public String getUserResources(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<Discipline> disciplines = user.getDisciplines();
        List<RatingResponse> responseList = new ArrayList<>();
        for (Discipline discipline : disciplines) {
            List<Task> tasks = taskService.findTasksByDisciplineId(discipline.getId());
            List<UserTask> userTasks = userTaskService.findAllByDisciplineIdAndUserId(discipline.getId(), user.getId());

            List<RatingTasksResponse> tasksResponses = new ArrayList<>();
            for (UserTask userTask : userTasks) {
                tasksResponses.add(new RatingTasksResponse(
                        userTask.getTask().getId(),
                        userTask.getTask().getName(),
                        userTask.getResultScore(),
                        userTask.getTask().getMaxScore()
                    )
                );
            }

            Set<Long> existingTaskIds = new HashSet<>();
            for (RatingTasksResponse response : tasksResponses) {
                existingTaskIds.add(response.getTaskId());
            }

            for (Task task : tasks) {
                if (!existingTaskIds.contains(task.getId())) {
                    tasksResponses.add(new RatingTasksResponse(
                                    task.getId(),
                                    task.getName(),
                                    0,
                                    task.getMaxScore()
                            )
                    );
                }
            }
            tasksResponses.sort(Comparator.comparing(RatingTasksResponse::getTaskId));

            int totalScoreStudent = 0;
            int totalScoreMax = 0;
            for (RatingTasksResponse task : tasksResponses) {
                totalScoreStudent += task.getResultScore() != null ? task.getResultScore() : 0;
                totalScoreMax += task.getMaxScore() != null ? task.getMaxScore() : 0;
            }

            int rating = 0;
            if (totalScoreMax > 0) {
                double ratingDouble = (double) totalScoreStudent / totalScoreMax;
                rating = (int) Math.round(ratingDouble * 100);
            }

            responseList.add(new RatingResponse(
                    discipline.getId(),
                    discipline.getName(),
                    tasksResponses,
                    rating
            ));
        }

        responseList.sort(Comparator.comparing(RatingResponse::getDisciplineId));
        model.addAttribute("ratings", responseList);
        model.addAttribute("content", "top-navigation/user-rating/user-rating");
        return "index";
    }
}
