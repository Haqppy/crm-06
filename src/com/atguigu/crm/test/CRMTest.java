package com.atguigu.crm.test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.atguigu.crm.dao.SalesChanceMapper;
import com.atguigu.crm.entity.SalesChance;

public class CRMTest {

	private ApplicationContext ctx = null;
	private SalesChanceMapper salesChanceMapper;
	
	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
	}
	
	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}

	@Test
	public void testSalesChanceMapper() {
		salesChanceMapper = ctx.getBean(SalesChanceMapper.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromIndex", 1);
		map.put("endIndex", 5);
		List<SalesChance> content = salesChanceMapper.getContent(map);
		
		for (SalesChance salesChance : content) {
			System.out.println(salesChance);
		}
	}
	
	public static void main(String[] args) throws SchedulerException {
//		实现 Job 接口，可使 Java 类变为可调度的任务
		
//		创建描述 Job 的 JobDetail 对象
		JobDetailImpl jobDetailImpl = new JobDetailImpl();
		
		jobDetailImpl.setName("myJob2");
		jobDetailImpl.setGroup("tem");
		jobDetailImpl.setJobClass(MyJob2.class);
		
		jobDetailImpl.setName("myJob");
		jobDetailImpl.setGroup("Two");
		jobDetailImpl.setJobClass(MyJob.class);
		
//		创建 SimpleTrigger 对象
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setName("myTrigger");
		trigger.setGroup("Two");
		
//		设置触发 Job 执行的时间规则
		trigger.setStartTime(new Date());
		trigger.setRepeatCount(10);
		trigger.setRepeatInterval(1000 * 3);
		
//		通过 SchedulerFactory 获取 Scheduler 对象
		StdSchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();
//		向 SchedulerFactory 中注册 JobDetail 和 Trigger
		scheduler.scheduleJob(jobDetailImpl, trigger);
		
//		启动调度任务
		scheduler.start();
	
		
	}
}
