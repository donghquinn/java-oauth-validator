package org.donghyuns.oauth.validator.common.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CommonListReq {
    @Min(10)
    @Max(100)
    int ListSize = 10;

    @Min(1)
    int pageNo = 1;
}
