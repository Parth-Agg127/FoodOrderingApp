package com.project.dto.map;

import com.project.dto.CouponDTO;
import com.project.entity.Coupon;
import com.project.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);

    @Mapping(target = "customerIds", expression = "java(mapCustomerIds(coupon.getCustomers()))")
    CouponDTO toDTO(Coupon coupon);

    Coupon toEntity(CouponDTO dto);

    List<CouponDTO> toDtoList(List<Coupon> coupons);

    List<Coupon> toEntityList(List<CouponDTO> dtos);

    // Helper method to map customers -> IDs
    default List<Long> mapCustomerIds(List<Customer> customers) {
        if (customers == null) {
            return null;
        }
        return customers.stream()
                .map(Customer::getId)
                .toList();
    }
}

