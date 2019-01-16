package com.tomaszekem.modelling.config.data;

import com.google.common.collect.Lists;
import com.tomaszekem.modelling.domain.File;
import com.tomaszekem.modelling.domain.User;
import com.tomaszekem.modelling.repository.FileRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UserFilesDataGenerator {

    private static final String VPP_FILE_PATH = "src/main/resources/class_diagram.vpp";
    private static final String USER_ID = "userId";
    private static final String FILE_NAME = "Class diagram file";
    private static final String FILE_EXTENSION = "vpp";
    public static final String CONTENT_CONTENT_TYPE = "document";
    private final FileRepository fileRepository;

    public UserFilesDataGenerator(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    void createUsersFiles(List<User> users) throws IOException {
        java.io.File fileFromResources = new java.io.File(VPP_FILE_PATH);
        List<File> usersFiles = Lists.newArrayList();
        for (User user : users) {
            File file = new File();
            file.setUser(user);
            file.setName(FILE_NAME);
            file.setContentContentType(CONTENT_CONTENT_TYPE);
            file.setFileExtension(FILE_EXTENSION);
            file.setContent(FileUtils.readFileToByteArray(fileFromResources));
            usersFiles.add(file);
        }
        fileRepository.saveAll(usersFiles);
    }

}
