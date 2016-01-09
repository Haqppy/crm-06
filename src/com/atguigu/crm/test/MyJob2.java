package com.atguigu.crm.test;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob2 implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		System.out.println("MyJob2 --->执行了...." + new Date());
	}

}
