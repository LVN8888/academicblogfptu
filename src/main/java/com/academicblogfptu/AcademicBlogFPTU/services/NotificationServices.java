package com.academicblogfptu.AcademicBlogFPTU.services;

import com.academicblogfptu.AcademicBlogFPTU.config.MyHandler;
import com.academicblogfptu.AcademicBlogFPTU.dtos.NotificationDtos.NotificationDto;
import com.academicblogfptu.AcademicBlogFPTU.entities.CommentEntity;
import com.academicblogfptu.AcademicBlogFPTU.entities.NotificationEntity;
import com.academicblogfptu.AcademicBlogFPTU.entities.PostEntity;
import com.academicblogfptu.AcademicBlogFPTU.exceptions.AppException;
import com.academicblogfptu.AcademicBlogFPTU.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServices {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private final MyHandler myHandler;



    @Autowired
    private PostRepository postRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public void sendNotification(NotificationDto notificationDto){
        NotificationEntity notification = new NotificationEntity();

        notification.setContent(notificationDto.getContent());
        notification.setRead(false);
        notification.setType(notificationDto.getType());
        notification.setNotifyAt(LocalDateTime.now());
        notification.setRelatedId(notificationDto.getRelatedId());
        notification.setTriggerUser(notificationDto.getTriggerUser());
        notification.setUser(userRepository.findById(notificationDto.getUserId()).orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND)));

        if(notificationDto.getType().equals("post")) {
            PostEntity post = postRepository.findById(notificationDto.getRelatedId()).orElseThrow(() -> new AppException("Unknown post", HttpStatus.NOT_FOUND));

            notification.setRelatedURL(post.getSlug());
        } else if (notificationDto.getType().equals("comment")) {

            CommentEntity comment = commentRepository.findById(notificationDto.getRelatedId()).orElseThrow(() -> new AppException("Unknown comment", HttpStatus.NOT_FOUND));
            notification.setRelatedURL(comment.getPost().getSlug());

        }
        notificationRepository.save(notification);

    }

    public NotificationEntity readNotification(int id){
        NotificationEntity notification = notificationRepository.findById(id).orElseThrow(() -> new AppException("Unknown notification", HttpStatus.UNAUTHORIZED));
        if(!notification.isRead()){
            notification.setRead(true);
            notificationRepository.save(notification);
        }
        return notification;
    }

    public List<NotificationDto> getAllNotification(int id){
        List<NotificationEntity> notifications = notificationRepository.findAllByUserId(id);
        List<NotificationDto> notificationList = new ArrayList<>();
        for (NotificationEntity notificationEntity: notifications) {
            notificationList.add(mapToDto(notificationEntity));
        }
        return notificationList;
    }

    public NotificationEntity getNotification(int id){
        return notificationRepository.findById(id).orElseThrow(() -> new AppException("Unknown notification",HttpStatus.UNAUTHORIZED));
    }

    private NotificationDto mapToDto(NotificationEntity notificationEntity){



        NotificationDto notification = new NotificationDto();
        notification.setNotificationId(notificationEntity.getId());
        notification.setContent(notificationEntity.getContent());
        notification.setRead(notificationEntity.isRead()); ;
        notification.setRelatedId(notificationEntity.getRelatedId());
        notification.setType(notificationEntity.getType());
        notification.setNotifyTime(notificationEntity.getNotifyAt().format(formatter));
        notification.setRelatedUrl(notificationEntity.getRelatedURL());
        notification.setUserId(notificationEntity.getUser().getId());
        notification.setTriggerUser(notificationEntity.getTriggerUser());
        notification.setFullNameOfTriggerUser(userDetailsRepository.findByUserId(notificationEntity.getTriggerUser()).getFullName());

        return notification;
    }

    private final ApplicationContext applicationContext;


    public void sendNotificationRealtime(Integer userId, String message) {
        myHandler.sendNotification(userId, message);
    }


}
