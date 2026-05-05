package com.libraryspring.turkcell.repository;

import com.libraryspring.turkcell.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * BookCopyRepository - BookCopy entity'si için veri erişim katmanı
 */
@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    
    // Belirli bir kitabın tüm kopyaları
    List<BookCopy> findByBook_BookId(Long bookId);
    
    // Duruma göre kopyalar
    List<BookCopy> findByStatus(String status);
    
    // Raf konumuna göre
    List<BookCopy> findByShelfLocation(String shelfLocation);
}
