package com.JIMS.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyTaskScheduler {

	@Autowired
	private TaskService taskService;

	// Schedule to run every day at 12:00 AM
	@Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Kolkata") // Midnight IST
	public void executeDailyTask() {
		System.out.println("Starting daily task execution...");
		taskService.performDailyTask(); // Call service logic
		System.out.println("Daily task execution completed.");
	}

}
