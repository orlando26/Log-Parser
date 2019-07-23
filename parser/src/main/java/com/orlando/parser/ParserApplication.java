package com.orlando.parser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.orlando.parser.service.LogService;

@Parameters(separators = "=", commandDescription = "Parser arguments")
@SpringBootApplication
public class ParserApplication implements CommandLineRunner{

	@Parameter(names={"--startDate", "-sd"})
	private String startDate;
	
	@Parameter(names={"--duration", "-d"})
	private String duration;
	
	@Parameter(names={"--threshold", "-t"})
	private int threshold;
	
	@Parameter(names={"--uploadLog", "-ul"})
	private String uploadLog;

	private static Logger LOG = LoggerFactory
			.getLogger(SpringBootApplication.class);
	
	final JCommander commander = JCommander.newBuilder()
	.addObject(this)
	.build();
	
	@Autowired
	private LogService logService;
	

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(ParserApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		LOG.info("EXECUTING : command line runner");
		commander.parse(args);
		LOG.info("Initial values: startDate: " + startDate + ", duration: " + duration + ", threshold: " + threshold);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
		Date time1 = dateFormat.parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(time1);
		Date time2 = null;
		if (duration.equals("hourly")) {
			cal.add(Calendar.HOUR_OF_DAY, 1);
			time2 = cal.getTime();
		}else if (duration.equals("daily")){
			cal.add(Calendar.DAY_OF_YEAR, 1);
			time2 = cal.getTime();
		}else {
			LOG.error("duration should either be hourly or daily!");
			return;
		}
		
		if(uploadLog.equals("true")) {
			logService.save();
		}else if(uploadLog.equals("false")){
			LOG.info("Not uploading log file to DB");
		}else {
			LOG.error("uploadLog parameter should either be true or false!");
			return;
		}
		LOG.info("from " + time1 + "to " + time2);
		
		logService.findIps(time1, time2, threshold);
	}


}
