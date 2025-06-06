package com.bookexchange.repository;

import com.bookexchange.dto.response.ListedBooksResponse;
import com.bookexchange.entity.ListedBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListedBookRepository extends JpaRepository<ListedBook, Long> {
    List<ListedBook> findTop4ByStatusOrderByCreatedAtDesc(Integer status);
    
    // Truy vấn tùy chỉnh để lấy sách với các điều kiện lọc
    @Query("SELECT lb FROM ListedBook lb " +
           "LEFT JOIN lb.authors a " +
           "LEFT JOIN lb.categories c " +
           "WHERE lb.status = 1 " +
           "AND (:title IS NULL OR LOWER(lb.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%'))) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId) " +
           "AND (:minPrice IS NULL OR lb.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR lb.price <= :maxPrice) " +
           "AND (:condition IS NULL OR lb.conditionNumber = :condition) " +
           "AND (:schoolId IS NULL OR lb.school.id = :schoolId) " +
           "GROUP BY lb.id")
    Page<ListedBook> findBooksWithFilters(
            @Param("title") String title,
            @Param("author") String author,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("condition") Integer condition,
            @Param("schoolId") Long schoolId,
            Pageable pageable);
    
    // Truy vấn DTO projection cho hiệu suất tốt hơn
    @Query("SELECT new com.bookexchange.dto.response.ListedBooksResponse(" +
           "lb.id, lb.title, lb.priceNew, lb.price, lb.conditionNumber, " +
           "lb.description, lb.thumbnail, lb.publisher, " +
           "s.name, u.fullName, COALESCE((SELECT MIN(a.name) FROM lb.authors a), '')) " +
           "FROM ListedBook lb " +
           "JOIN lb.school s " +
           "JOIN lb.seller u " +
           "LEFT JOIN lb.authors a " +
           "LEFT JOIN lb.categories c " +
           "WHERE lb.status = 1 " +
           "AND (:title IS NULL OR LOWER(lb.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:author IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :author, '%'))) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId) " +
           "AND (:minPrice IS NULL OR lb.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR lb.price <= :maxPrice) " +
           "AND (:condition IS NULL OR lb.conditionNumber = :condition) " +
           "AND (:schoolId IS NULL OR lb.school.id = :schoolId) " +
           "GROUP BY lb.id, lb.title, lb.priceNew, lb.price, lb.conditionNumber, " +
           "lb.description, lb.thumbnail, lb.publisher, s.name, u.fullName")
    Page<ListedBooksResponse> findBooksWithFiltersProjection(
            @Param("title") String title,
            @Param("author") String author,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("condition") Integer condition,
            @Param("schoolId") Long schoolId,
            Pageable pageable);
}