package com.content_i_like.repository;

import com.content_i_like.domain.entity.Answer;
import com.content_i_like.domain.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

  public Answer findByInquiry(Inquiry inquiry);
}
