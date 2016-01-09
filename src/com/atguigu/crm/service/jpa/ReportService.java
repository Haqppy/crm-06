package com.atguigu.crm.service.jpa;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.digester.ObjectParamRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.repository.ReportRepository;

@Service
public class ReportService {

	@Autowired
	private ReportRepository reportRepository;
	
	private DateFormat dateFormat = null;
	
	{
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public Page<Object[]> getCustomerService(Map<String, Object> paramMap, int pageNo,int pageSize) throws ParseException {
		//<input type="text" name="search_date1" size="10" />
		//<input type="text" name="search_date2" size="10" />

		String minDateStr = (String) paramMap.get("date1");
		Date minDate = null;
		
		try {
			minDate = dateFormat.parse(minDateStr);
		} catch (Exception e) {
			minDate = dateFormat.parse("1970-1-1");
		}
		
		String maxDateStr = (String) paramMap.get("date2");
		Date maxDate = null;
		try {
			maxDate = dateFormat.parse(maxDateStr);
		} catch (Exception e) {
			maxDate = dateFormat.parse("2099-12-31");
		}
		
		return reportRepository.getCustomerService(pageNo, pageSize, minDate, maxDate);		
		
	}
	
	
	public Page<Object[]> getCustomerConsist(String type,int pageNo,int pageSize) {
		
		return reportRepository.getCustomerConsist(type, pageNo, pageSize);
	}
	
	public Page<Customer> getCustomerPage(int pageNo,int pageSize,Map<String, Object> paramMap) throws ParseException {
		//search_name
		//search_date1 search_date2
		
		String name = (String) paramMap.get("name");
		if (name == null) {
			name = "";
		}
		String minDateStr = (String) paramMap.get("date1");
		Date minDate = null;
		
		try {
			minDate = dateFormat.parse(minDateStr);
		} catch (Exception e) {
			minDate = dateFormat.parse("1970-1-1");
		}
		
		String maxDateStr = (String) paramMap.get("date2");
		Date maxDate = null;
		try {
			maxDate = dateFormat.parse(maxDateStr);
		} catch (Exception e) {
			maxDate = dateFormat.parse("2099-12-31");
		}
		
		return reportRepository.getCustomerPage(pageNo, pageSize, name, minDate, maxDate);
	}
}
