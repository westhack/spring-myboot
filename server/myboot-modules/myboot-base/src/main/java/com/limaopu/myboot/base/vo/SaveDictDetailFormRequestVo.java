package com.limaopu.myboot.base.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mac
 */
@NoArgsConstructor
@Data
public class SaveDictDetailFormRequestVo {
    @JsonProperty("dictId")
    @NotNull(message="不能为空")
    private Integer dictId;

    @JsonProperty("dictDetails")
    private List<DictDetailsDTO> dictDetails;

    @NoArgsConstructor
    @Data
    public static class DictDetailsDTO {
        @JsonProperty("label")
        private String label;
        @JsonProperty("value")
        private String value;
        @JsonProperty("color")
        private String color;
    }
}
