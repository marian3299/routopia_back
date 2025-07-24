package com.back.routopia.specification;

import com.back.routopia.entity.Category;
import com.back.routopia.entity.Destino;
import org.springframework.data.jpa.domain.Specification;

public class DestinoSpecification {
    public static Specification<Destino> hasCategory(Category category){
        return ((root, query, criteriaBuilder) ->
                    category == null ? null : criteriaBuilder.equal(root.get("category"), category));

    }
}
