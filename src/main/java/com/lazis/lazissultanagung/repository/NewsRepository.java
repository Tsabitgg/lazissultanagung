package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE n.approved = true ORDER BY n.date DESC")
    Page<News> findAllApprovedNews(Pageable pageable);
}
