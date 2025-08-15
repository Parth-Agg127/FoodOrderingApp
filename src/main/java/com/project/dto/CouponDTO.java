package com.project.dto;

import com.project.enums.CouponType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponDTO {
    private Long id;
    private List<Long> customerIds;
    private String code;
    private CouponType type;
    private Double discountValue;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
}
