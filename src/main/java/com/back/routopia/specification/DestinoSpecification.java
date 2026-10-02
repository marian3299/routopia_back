package com.back.routopia.specification;

import com.back.routopia.entity.Booking;
import com.back.routopia.entity.Destino;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
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

    /**
     * Filtra destinos sin una reserva CONFIRMED en la fecha dada, es decir,
     * disponibles para esa fecha. {@code date} null: no se aplica filtro.
     */
    public static Specification<Destino> isAvailableOnDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = subquery.from(Booking.class);
            subquery.select(bookingRoot.get("id"))
                    .where(
                            criteriaBuilder.equal(bookingRoot.get("destino").get("id"), root.get("id")),
                            criteriaBuilder.equal(bookingRoot.get("bookingDate"), date),
                            criteriaBuilder.equal(bookingRoot.get("status"), "CONFIRMED")
                    );

            return criteriaBuilder.not(criteriaBuilder.exists(subquery));
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
