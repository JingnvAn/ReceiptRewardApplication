package com.jingnu.receipt.processor.responseModels;

import org.springframework.stereotype.Component;
@Component
public class GetPointsSuccessResponse extends GetPointsResponse {
    private Integer points;
    public Integer getPoints() { return points; }

    public void setPoints(Integer points) { this.points = points; }
}
