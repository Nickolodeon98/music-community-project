package com.content_i_like.repository;

import com.content_i_like.domain.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
