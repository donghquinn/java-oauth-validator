package org.donghyuns.oauth.validator.common.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommonListResponse {
  int currentPage;
  int total;
  int currentCount;

  int totalPage;

  public void setTotalPage(int totalCount, int pageSize) {
    this.totalPage = 0;
    if (pageSize > 0) {
      this.totalPage = totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1);
    }
  }
}
