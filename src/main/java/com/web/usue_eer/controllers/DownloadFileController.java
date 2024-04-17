package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.security.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping("/portal/download-file")
public class DownloadFileController {
    private static final Logger logger = Logger.getLogger(DownloadFileController.class.getName());
    private final FilesTaskService filesTaskService;
    private final UserTaskFilesService userTaskFilesService;
    private final FileDisciplineService fileDisciplineService;
    private final FileUserService fileUserService;
    private final FileSharingService fileSharingService;

    @Autowired
    public DownloadFileController(FilesTaskService filesTaskService, UserTaskFilesService userTaskFilesService, FileDisciplineService fileDisciplineService, FileUserService fileUserService, FileSharingService fileSharingService) {
        this.filesTaskService = filesTaskService;
        this.userTaskFilesService = userTaskFilesService;
        this.fileDisciplineService = fileDisciplineService;
        this.fileUserService = fileUserService;
        this.fileSharingService = fileSharingService;
    }

    @GetMapping("/file-sharing/{fileId}")
    public void downloadFileSharing(HttpServletResponse response, @PathVariable Long fileId) {
        Optional<FileSharing> fileOptional = fileSharingService.findFileSharingById(fileId);
        if (fileOptional.isPresent()) {
            FileSharing file = fileOptional.get();
            download(response, file.getFileName(), file.getFileData(), file.getFileSize());
        }
        else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @GetMapping("/discipline-file/{fileId}")
    public void downloadDisciplineFile(HttpServletResponse response, @PathVariable Long fileId) {
        Optional<FileDiscipline> fileOptional = fileDisciplineService.findFileDisciplineById(fileId);
        if (fileOptional.isPresent()) {
            FileDiscipline file = fileOptional.get();
            download(response, file.getFileName(), file.getFileData(), file.getFileSize());
        }
        else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @GetMapping("/user-file/{fileId}")
    public void downloadUserFile(HttpServletResponse response, @PathVariable Long fileId) {
        Optional<FileUser> fileOptional = fileUserService.findFileUserById(fileId);
        if (fileOptional.isPresent()) {
            FileUser file = fileOptional.get();
            download(response, file.getFileName(), file.getFileData(), file.getFileSize());
        }
        else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @GetMapping("/task-file/{fileId}")
    public void downloadTask(HttpServletResponse response, @PathVariable Long fileId) {
        Optional<FilesTask> fileOptional = filesTaskService.findFilesTaskById(fileId);
        if (fileOptional.isPresent()) {
            FilesTask file = fileOptional.get();
            download(response, file.getFileName(), file.getFileData(), file.getFileSize());
        }
        else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @GetMapping("/user-task-file/{fileId}")
    public void downloadUserTask(HttpServletResponse response, @PathVariable Long fileId) {
        Optional<UserTaskFiles> fileOptional = userTaskFilesService.findUserTaskFilesById(fileId);
        if (fileOptional.isPresent()) {
            UserTaskFiles file = fileOptional.get();
            download(response, file.getFileName(), file.getFileData(), file.getFileSize());
        }
        else response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    public void download(HttpServletResponse response, String fileName, byte[] fileData, long fileSize) {
        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"; charset=UTF-8", new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLengthLong(fileSize);

            try (OutputStream out = response.getOutputStream()) {
                out.write(fileData);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при отправке файла", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}