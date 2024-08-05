package co.kr.suhyeong.project.product.interfaces.rest.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ModifyProductImageReqDto {
    private List<ImageReqDto> images;

    @Getter
    public static class ImageReqDto {
        private String divCode;
        private Integer sequence;
        private String fileName;
    }
}
