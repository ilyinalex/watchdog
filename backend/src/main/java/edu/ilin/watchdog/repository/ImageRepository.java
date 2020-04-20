package edu.ilin.watchdog.repository;

import edu.ilin.watchdog.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Page<Image> findByUserID(Long userId);
}
