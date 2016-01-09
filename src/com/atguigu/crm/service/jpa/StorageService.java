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

import com.atguigu.crm.entity.Storage;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.repository.StorageRepository;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class StorageService {

	@Autowired
	private StorageRepository storageRepository;
	
	public void delete(Long id) {
		storageRepository.delete(id);
	}
	
	public void update(Storage storage) {
		storageRepository.saveAndFlush(storage);
	}
	
	public void save(Storage storage) {
		storageRepository.saveAndFlush(storage);
	}
	
	public Storage getStorageById(Long id) {
		
		return storageRepository.findOne(id);
	}
	
	public Page<Storage> getPage(Map<String, Object>map,int pageNo,int pageSize) {
		
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		//解析查询条件
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(map);
		
		Specification<Storage> spec = parseFiltersToSpecification(filters);
		
		return storageRepository.findAll(spec, pageable);
	}

	private Specification<Storage> parseFiltersToSpecification(
			final List<PropertyFilter> filters) {
		
		Specification<Storage> specification = new Specification<Storage>() {
			@Override
			public Predicate toPredicate(Root<Storage> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				
				if(filters != null && filters.size() > 0){
					for(PropertyFilter filter: filters){
						String propertyName = filter.getPropertyName(); //name, manager.name
						Object propertyVal = filter.getPropertyValue();
						Class propertyType = filter.getPropertyType();
						propertyVal = ReflectionUtils.convertValue(propertyVal, propertyType);
						
						MatchType matchType = filter.getMatchType();
						
						String[] names = propertyName.split("\\.");
						Path path = root.get(names[0]);
						if(names.length > 1){
							for(int i = 1; i < names.length; i++){
								path = path.get(names[i]);
							}
						}
						
						Expression<Comparable> expression = path;
						Comparable val = (Comparable) propertyVal;
						
						Predicate predicate = null;
						switch(matchType){
						case EQ:
							predicate = builder.equal(path, propertyVal);
							break;
						case GE:
							predicate = builder.greaterThanOrEqualTo(expression, val);
							break;
						case GT:
							predicate = builder.greaterThan(expression, val);
							break;
						case LE:
							predicate = builder.lessThanOrEqualTo(expression, val);
							break;
						case LT:
							predicate = builder.lessThan(expression, val);
							break;
						case LIKE:
							predicate = builder.like(path, "%" + propertyVal + "%");
							break;
						case NOTEQ:
							predicate = builder.notEqual(expression, val);
							break;
						}
						
						if(predicate != null){
							predicates.add(predicate);
						}
					}
				}
				
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}; 
		
		return specification;
	}
}
