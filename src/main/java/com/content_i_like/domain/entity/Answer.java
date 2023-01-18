package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Answer {
    @Id
    private Long answerNo;
    private String answerContent;

    @OneToOne
    @JoinColumn(name = "inquiry_no")
    private Inquiry inquiry;
}
