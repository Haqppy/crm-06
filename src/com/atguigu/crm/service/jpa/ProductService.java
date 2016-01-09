package com.atguigu.crm.service.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.atguigu.crm.entity.Product;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.repository.ProductRepository;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public void delete(Long id) {
		productRepository.delete(id);
	}
	
	public void update(Product product){
		productRepository.saveAndFlush(product);
	}
	
	public void save(Product product) {
		productRepository.saveAndFlush(product);
	}
	
	public Product get(Long id) {
		return productRepository.findOne(id);
	}
	
	public List<Product> getAll(){
		return productRepository.findAll();
	}
	
	public Page<Product> getPage(Map<String, Object> parmetersMap,int pageNo,int pageSize) {
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(parmetersMap);
		
		Specification<Product> spec = parseFiltersToSpecification(filters);
		
		return productRepository.findAll(spec, pageable);
	}

	private Specification<Product> parseFiltersToSpecification(
			final List<PropertyFilter> filters) {
		
		Specification<Product> specification = new Specification<Product>() {
			
			@Override
			public Predicate toPredicate(Root<Product> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (filters != null && filters.size() > 0) {
					for (PropertyFilter filter : filters) {
						String propertyName = filter.getPropertyName();
						Object propertyValue = filter.getPropertyValue();
						Class propertyType = filter.getPropertyType();
						propertyValue = ReflectionUtils.convertValue(propertyValue, propertyType);
						
						MatchType matchType = filter.getMatchType();
						
						String[] names = propertyName.split("\\.");
						Path path = root.get(names[0]);
						if (names.length > 0) {
							for (int i = 1; i < names.length; i++) {
								path = path.get(names[i]);
							}
						}
						Expression<Comparable> expression = path;
						Comparable val = (Comparable) propertyValue;
						
						Predicate predicate = null;
						switch (matchType) {
						case EQ:
							predicate = cb.equal(expression, propertyValue);
							break;
						case GE:
							predicate = cb.greaterThanOrEqualTo(expression, val);
							break;
						case GT:
							predicate = cb.greaterThan(expression, val);
							break;
						case LE:
							predicate = cb.lessThanOrEqualTo(expression, val);
							break;
						case LT:
							predicate = cb.lessThan(expression, val);
							break;
						case LIKE:
							predicate = cb.like(path, "%" + propertyValue + "%");
							break;
						case NOTEQ:
							predicate = cb.notEqual(expression, val);
							break;	
						}
						
						if (predicate != null) {
							predicates.add(predicate);
						}
						
					}
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		return specification;
	}
}
