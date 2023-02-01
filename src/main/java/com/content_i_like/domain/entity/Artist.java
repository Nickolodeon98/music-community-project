package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Artist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long artistNo;
  @Column(unique = true)
  public String artistName;
}
