package com.content_i_like.repository;

import com.content_i_like.domain.entity.Inquiry;
import com.content_i_like.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    Page<Inquiry> findAllByMemberAndCreatedAtAfter(Pageable pageable, Member member, LocalDateTime before_6_months);
}
