package com.content_i_like.repository;

import com.content_i_like.domain.entity.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {

    Page<FAQ> findAllByFaqCategory(Pageable pageable, String faqCategory);

    Page<FAQ> findAllByFaqTitleContaining(Pageable pageable, String title);
}
