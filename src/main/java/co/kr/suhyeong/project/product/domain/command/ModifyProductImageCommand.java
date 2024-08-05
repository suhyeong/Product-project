package co.kr.suhyeong.project.product.domain.command;

import co.kr.suhyeong.project.product.domain.constant.ProductImageCode;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
public class ModifyProductImageCommand {
    private String productCode;
    private List<ProductImage> images;

    public boolean existModifyImage() {
        return Objects.nonNull(this.images) && !this.images.isEmpty();
    }

    /**
     * 수정이 필요하지 않은 (sequence 가 존재) 데이터의 시퀀스만 리턴
     * @return
     */
    public List<Integer> getNotModifyImageSequences() {
        return this.images.stream().map(ProductImage::getSequence)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public String getThumbnailImagePath() {
        Optional<ProductImage> image = this.images.stream().filter(ProductImage::isThumbnailImage).findFirst();
        return image.isPresent() ? image.get().getPath() : StringUtils.EMPTY;
    }

    public List<String> getDetailImagePath() {
        return this.images.stream().filter(ProductImage::isDetailImage).map(ProductImage::getPath).collect(Collectors.toList());
    }

    /**
     * sequence 가 Null 인 경우 : 새 이미지
     * sequence 가 있는 경우 : 기존 이미지 (수정 없음)
     */
    @Getter
    @Builder
    public static class ProductImage {
        private Integer sequence;
        private ProductImageCode imageDivCode;
        private String path;

        private boolean isThumbnailImage() {
            return this.imageDivCode.equals(ProductImageCode.THUMBNAIL_IMAGE);
        }

        private boolean isDetailImage() {
            return this.imageDivCode.equals(ProductImageCode.FULL_DETAIL_IMAGE);
        }
    }
}
