package com.orlando.parser.service;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.orlando.parser.bean.IpFound;
import com.orlando.parser.model.BloquedIps;
import com.orlando.parser.model.Log;
import com.orlando.parser.repository.BloquedIpsRepository;
import com.orlando.parser.repository.LogRepository;

@Service
public class LogService {
		
	@Autowired
	private LogRepository logRepository;
	
	@Autowired
	private BloquedIpsRepository bloquedIpsRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Value("classpath:access.log")
	Resource logFile;
	
	private static Logger LOG = LoggerFactory
			.getLogger(SpringBootApplication.class);
	
	public void save() {
		logRepository.deleteAll();
		LOG.info("All entries deleted...");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			LOG.info("Uploading log file to DB...");
			Scanner scanner = new Scanner(logFile.getInputStream());
			while (scanner.hasNextLine()) {
				Log log = new Log();
				String line = scanner.nextLine();
				String[] arr = line.split("\\|");
				Date date = dateFormat.parse(arr[0]);
				log.setDate(date);
				log.setIp(arr[1]);
				log.setRequest(arr[2]);
				log.setStatus(arr[3]);
				log.setUserAgent(arr[4]);
				
				System.out.println(log);
				logRepository.save(log);
			}
			LOG.info("Log file uploaded to DB.");
			scanner.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		} 
		
	}
	
	public void findIps(Date time1, Date time2, int threshold) {
		Query query =  entityManager.createNativeQuery("select Log_Ip, Log_Status, count(*)"
				+ " from lp_log where Log_Date >= ? AND Log_Date <= ? "
				+ "group by Log_Ip having count(Log_Ip) > ? ");//logRepository.findIpsByDateAndThreshold(time1, time2, threshold);
		query.setParameter(1, time1) ;
		query.setParameter(2, time2);
		query.setParameter(3, threshold);
		List<Object[]> results = query.getResultList();
		
		results.stream().forEach((record) -> {
			IpFound ip  = new IpFound();
			ip.setIp((String)record[0]);
			ip.setStatus((Integer)record[1]);
			ip.setCount(((BigInteger)record[2]).longValue()); 
			
			System.out.println(ip);
			bloquedIpsRepository.save(new BloquedIps(ip.getIp(), "Ip was bloqued with " + ip.getCount() + " requests and status " + ip.getStatus()));
		});
		LOG.info("Ips found:" + results.size());
	}
	
}
