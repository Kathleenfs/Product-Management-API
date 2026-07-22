package io.github.kathleenfs.productmanagementapi.repository;

import io.github.kathleenfs.productmanagementapi.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
