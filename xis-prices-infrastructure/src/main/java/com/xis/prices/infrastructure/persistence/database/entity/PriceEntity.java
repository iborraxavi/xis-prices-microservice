package com.xis.prices.infrastructure.persistence.database.entity;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("PRICES")
public class PriceEntity {

  @Id private Long id;

  @Column("BRAND_ID")
  private Integer brandId;

  @Column("START_DATE")
  private OffsetDateTime startDate;

  @Column("END_DATE")
  private OffsetDateTime endDate;

  @Column("PRICE_LIST")
  private Long priceList;

  @Column("PRODUCT_ID")
  private String productId;

  @Column("PRIORITY")
  private Integer priority;

  @Column("PRICE")
  private Double price;

  @Column("CURR")
  private String currency;
}
