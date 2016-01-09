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

import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.repository.DictRepository;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class DictService {

	@Autowired
	private DictRepository dictRepository;
	
	public Dict get(Long id){
		
		return dictRepository.findOne(id);
	}
	
	public void update(Dict dict){
		dictRepository.saveAndFlush(dict);
	}
	
	public void delete(Long id){
		dictRepository.delete(id);
	}
	
	public void save(Dict dict){
		dictRepository.saveAndFlush(dict);
	}
	
	public Page<Dict> getPage(Map<String, Object> paramMap,int pageNo,int pageSize) {
		
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(paramMap);
	
		Specification<Dict> spec = parseFiltersToSpecification(filters);
		
		return dictRepository.findAll(spec, pageable);
	}

	private Specification<Dict> parseFiltersToSpecification(
			final List<PropertyFilter> filters) {
		
		Specification<Dict> specification = new Specification<Dict>() {

			@Override
			public Predicate toPredicate(Root<Dict> root,
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
						if (names.length > 1) {
							for (int i = 1; i < names.length; i++) {
								path = path.get(names[i]);
							}
						}
						Expression<Comparable> expression = path; 
						Comparable val = (Comparable) propertyValue;
						Predicate predicate = null;
						switch (matchType) {
						case EQ:
							predicate = cb.equal(path, propertyValue);
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
							predicate = cb.like(path, "%"+propertyValue+"%");
							break;
						case NOTEQ:
							predicate = cb.notEqual(expression, propertyValue);
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
