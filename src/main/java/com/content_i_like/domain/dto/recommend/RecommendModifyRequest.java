package com.content_i_like.domain.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecommendModifyRequest {

  private String recommendTitle;
  private String recommendContent;
  private String recommendYoutubeUrl;
  private MultipartFile image;
  private String hashtag;
}
