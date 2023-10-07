package healthcare.api.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SimpleService<T> {
    Page<T> getPaginated(Pageable pageable);

    Page<T> getPaginated(Specification<T> specification, Pageable pageable);

    List<T> getAll();

    List<T> getAll(Specification<T> specification);


    T getById(Long id);

    T reqToEntity(Object req,T entity) throws Exception;

    void deleteById(Long id);

    T save(T entity);
    List<T> saveAll(List<T> tList);
}
