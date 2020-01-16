package me.dhlee.springbootquartz;

import lombok.RequiredArgsConstructor;
import me.dhlee.springbootquartz.job.TestJob;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner{

	private final Scheduler scheduler;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		scheduler.standby();

		for (int i = 0; i < 10; i++) {

			JobKey jobKeyA = new JobKey("TestJob-" + i);
			TriggerKey triggerKeyA = new TriggerKey("TestTrigger-" + i);

			JobDataMap jobDataA = new JobDataMap();
			jobDataA.put("data", i);

			scheduler.scheduleJob(JobBuilder.newJob(TestJob.class)
							.setJobData(jobDataA)
							.withIdentity(jobKeyA)
							.storeDurably()
							.build(),
					TriggerBuilder.newTrigger()
							.withIdentity(triggerKeyA)
							.startNow()
							.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())
							.build()
			);
		}

		scheduler.start();
	}

}
