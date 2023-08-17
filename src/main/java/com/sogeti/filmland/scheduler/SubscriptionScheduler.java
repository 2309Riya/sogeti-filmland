package com.sogeti.filmland.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.sogeti.filmland.service.SchedulerService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class SubscriptionScheduler {

	@Autowired
	private SchedulerService schedulerService;

	@Scheduled(cron = "*/10 * * * * ?") // Run on the 1st day of every month
	public void processMonthlyPayments() {
		log.info("Calling payment Scheduler");

		schedulerService.processMonthlyPayments();
	}

}
