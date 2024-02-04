package com.wittypuppy.backend.config.scheduler;

import com.wittypuppy.backend.mail.service.EmailService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class DynamicTaskScheduler {
    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate simp;
    private final EmailService service;

    public DynamicTaskScheduler(TaskScheduler taskScheduler, SimpMessagingTemplate simp, EmailService service) {
        this.taskScheduler = taskScheduler;
        this.simp = simp;
        this.service = service;
    }
    public void scheduleTask(LocalDateTime dateTime, Long emailCode) {
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        System.out.println("예약한 시간(Date 타입) : "+date);
        taskScheduler.schedule(() -> {
            System.out.println("스케줄된 작업 실행: " + new Date());
            simp.convertAndSend("/topic/mail/alert/"+1,service.sendReserveMail(emailCode));

        }, date);
    }



}
