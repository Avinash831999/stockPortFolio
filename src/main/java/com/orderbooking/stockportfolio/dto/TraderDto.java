package com.orderbooking.stockportfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderbooking.stockportfolio.enums.TraderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraderDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    @JsonProperty("pan_number")
    private String panNumber;
    @NotBlank
    @JsonProperty("trader_status")
    private String traderStatus;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updated_at;
}
