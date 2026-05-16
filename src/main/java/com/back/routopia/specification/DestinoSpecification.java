package com.back.routopia.specification;

import com.back.routopia.entity.Destino;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class DestinoSpecification {

    /**
     * Filtro por categoría en el backend usando OR exclusivamente:
     * {@code destino.category IN (:categories)}, es decir, el destino coincide si
     * su categoría es cualquiera de las indicadas.
     * <p>
     * Lista vacía o null: no se aplica filtro por categoría (predicado siempre verdadero).
     */
    public static Specification<Destino> hasCategoryIn(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("category").get("id").in(categoryIds);
        };
    }

    public static Specification<Destino> searchByNameAndCity(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), likePattern)
            );
        };
    }
}
