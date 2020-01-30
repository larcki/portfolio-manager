package com.nordcomet.portfolio.data.fee;

import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.asset.Asset;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@Entity
public class Fee {

    @Id
    @GeneratedValue
    private Integer id;

    private Money amount;

    @ManyToOne
    private Asset asset;

    private LocalDateTime timestamp;

}
