package com.content_i_like.batchprocessing;

import com.content_i_like.domain.entity.Track;
import com.content_i_like.domain.entity.TrackData;
import org.springframework.batch.item.ItemProcessor;

public class TrackItemProcessor implements ItemProcessor<TrackData, Track> {

  @Override
  public Track process(TrackData item) throws Exception {
    return null;
  }
}
