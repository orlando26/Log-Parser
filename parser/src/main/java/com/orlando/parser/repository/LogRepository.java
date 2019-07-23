package com.orlando.parser.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.orlando.parser.model.Log;

public interface LogRepository extends JpaRepository<Log, Integer>{
	
	 ;	
			
			@Query(value = "select * from lp_log where Log_Date >= (?1) AND Log_Date <= (?2)"
							+ "group by Log_Ip having count(Log_Ip) > (?3) ",
					nativeQuery = true)
			public List<Log> findIpsByDateAndThreshold(Date time1, Date time2, int threshold);
			
}
