package me.dhlee.springbootquartz.job;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TestJob implements Job {

    @Autowired
    private InfluxDB influxDB;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int data = (int) dataMap.get("data");
        log.info("data : {}", data);
        log.info("TestJob start!!!");

        influxDB.enableGzip();
        influxDB.setDatabase("hhi");
        influxDB.setRetentionPolicy("autogen");
        influxDB.enableBatch(BatchOptions.DEFAULTS);

        influxDB.write(
                Point.measurement("test")
                .tag("test", "test" + data)
                .addField("val", data)
                .build());

        influxDB.close();

        log.info("TestJob stop!!!");
    }
}
