package io.github.kathleenfs.productmanagementapi.repository;

import io.github.kathleenfs.productmanagementapi.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);
    List<Product> findAllByActiveTrue();
}