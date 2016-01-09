package com.atguigu.crm.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.atguigu.crm.entity.Customer;

@Repository
public class ReportRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public Page<Object[]> getCustomerService(int pageNo,int pageSize,Date minDate,Date maxDate) {
		String hql = "select count(cs.serviceType) from CustomerService cs "
				 + "  where cs.customer is not null And cs.serviceType is not null "
				 + "  and cs.createDate between ? and ?";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter(1, minDate);
		query.setParameter(2, maxDate);
		long total = (long) query.getSingleResult();
		
		hql = "select cs.serviceType , count(cs.serviceType) from CustomerService cs "
				+ "where cs.customer is not null and cs.serviceType is not null and cs.createDate between ? and ? "
				+ " group by cs.serviceType";
		
		query = entityManager.createQuery(hql);
		query.setParameter(1, minDate);
		query.setParameter(2, maxDate);
		
		query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize);
		List<Object[]> content = query.getResultList();
		
		PageRequest pageable = new PageRequest(pageNo-1, pageSize);
		
		PageImpl<Object[]> page = new PageImpl<Object[]>(content, pageable, total);
		
		return page;
	}
	
	public Page<Object[]> getCustomerConsist(String type,int pageNo,int pageSize) {
		
		String hql = "select count(id) from Customer c "
				   + "where c."+type+" is not null "
				   + "group by c."+type;
				
		Query query = entityManager.createQuery(hql);	
		int total = query.getResultList().size();
		
		hql = "select c."+type+" , count(id) from Customer c where c."+type+" is not null "
				+ "group by c."+type;
		query = entityManager.createQuery(hql);
		query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize);
		List content = query.getResultList();
		
		PageRequest pageable = new PageRequest(pageNo-1, pageSize);
		
		PageImpl<Object[]> page = new PageImpl<Object[]>(content, pageable, total);
		
		return page;
	}
	
	public Page<Customer> getCustomerPage(int pageNo,int pageSize,String customerName,Date minDate,Date maxDate) {
		String hql = "Select count(distinct c.id) from "
				   + " Customer c "
				   + " left outer join c.orders o "
				   + " where c.name like ? "
				   + " AND o.date >= ? "
				   + " AND o.date < ? ";
		Query query = entityManager.createQuery(hql);
		query.setParameter(1, "%"+customerName+"%");
		query.setParameter(2, minDate);
		query.setParameter(3, maxDate);
		
		long totalElements = (long) query.getSingleResult();
		
		hql = "SELECT distinct c "
				+ "FROM Customer c "
				+ "LEFT OUTER JOIN FETCH c.orders o "
				+ "LEFT OUTER JOIN FETCH o.items "
				+ "WHERE c.name LIKE ? "
				+ "AND o.date >= ? "
				+ "AND o.date <= ? ";
		query = entityManager.createQuery(hql);
		query.setParameter(1, "%" + customerName + "%");
		query.setParameter(2, minDate);
		query.setParameter(3, maxDate);
		int firstResult = (pageNo - 1) * pageSize;
		int maxResults = pageSize;
		query.setFirstResult(firstResult)
		     .setMaxResults(maxResults);
		List<Customer> content = query.getResultList();
		
		PageRequest pageable = new PageRequest(pageNo-1, pageSize);
		
		PageImpl<Customer> page = new PageImpl<Customer>(content, pageable, totalElements);
		
		return page;
	}
	
}
