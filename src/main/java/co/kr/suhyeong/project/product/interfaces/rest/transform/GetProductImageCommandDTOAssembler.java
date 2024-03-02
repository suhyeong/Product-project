package co.kr.suhyeong.project.product.interfaces.rest.transform;

import co.kr.suhyeong.project.product.domain.command.GetProductImageCommand;
import co.kr.suhyeong.project.product.domain.constant.ProductImageCode;
import co.kr.suhyeong.project.product.domain.model.entity.ProductImage;
import co.kr.suhyeong.project.product.interfaces.rest.dto.GetProductImageDetailRspDto;
import co.kr.suhyeong.project.product.interfaces.rest.dto.GetProductImageRspDto;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class GetProductImageCommandDTOAssembler {

    @Mappings({
            @Mapping(target = "productCode", source = "productId"),
            @Mapping(target = "productImageCodeList", ignore = true)
    })
    public abstract GetProductImageCommand toCommand(String productId, String divCode);

    @AfterMapping
    protected void afterMappingToCommand(@MappingTarget GetProductImageCommand.GetProductImageCommandBuilder builder,
                                         String productId, String divCode) {
        String[] divCodes = divCode.split(",");
        List<ProductImageCode> codeList = new ArrayList<>();
        for(String code : divCodes) {
            codeList.add(ProductImageCode.find(code));
        }
        builder.productImageCodeList(codeList);
    }

    @Mappings({
            @Mapping(target = "productId", source = "productId"),
            @Mapping(target = "details", ignore = true)
    })
    public abstract GetProductImageRspDto toRspDTO(List<ProductImage> productImages, String productId);

    @AfterMapping
    protected void afterMappingToRspDTO(@MappingTarget GetProductImageRspDto.GetProductImageRspDtoBuilder builder,
                                        List<ProductImage> productImages, String productId) {
        List<GetProductImageDetailRspDto> list = new ArrayList<>();
        productImages.forEach(image -> list.add(toRspDetailDTO(image)));
        builder.details(list);
    }

    @Mappings({
            @Mapping(target = "divCode", source = "productImage.productImageId.divCode.code"),
            @Mapping(target = "path", source = "productImage.imgPath")
    })
    abstract GetProductImageDetailRspDto toRspDetailDTO(ProductImage productImage);
}
