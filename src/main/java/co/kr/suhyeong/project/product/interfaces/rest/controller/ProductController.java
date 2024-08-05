package co.kr.suhyeong.project.product.interfaces.rest.controller;

import co.kr.suhyeong.project.product.application.internal.commandservice.ProductCommandService;
import co.kr.suhyeong.project.product.application.internal.commandservice.ProductImageCommandService;
import co.kr.suhyeong.project.product.application.internal.queryservice.ProductQueryService;
import co.kr.suhyeong.project.product.domain.command.GetProductImageCommand;
import co.kr.suhyeong.project.product.domain.command.GetProductListCommand;
import co.kr.suhyeong.project.product.domain.command.ModifyProductImageCommand;
import co.kr.suhyeong.project.product.domain.model.view.ProductImageView;
import co.kr.suhyeong.project.product.domain.model.view.ProductListView;
import co.kr.suhyeong.project.product.domain.model.view.ProductView;
import co.kr.suhyeong.project.product.interfaces.rest.dto.*;
import co.kr.suhyeong.project.product.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static co.kr.suhyeong.project.product.interfaces.rest.controller.ProductUrl.*;

@Tag(name = "Product API", description = "상품 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = PRODUCT_DEFAULT_URL)
public class ProductController extends BaseController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;
    private final ProductImageCommandService productImageCommandService;

    private final CreateProductCommandDTOAssembler createProductCommandDTOAssembler;
    private final GetProductImageCommandDTOAssembler getProductImageCommandDTOAssembler;
    private final ModifyProductCommandDTOAssembler modifyProductCommandDTOAssembler;
    private final GetProductListCommandDTOAssembler getProductListCommandDTOAssembler;
    private final GetProductCommandDTOAssembler getProductCommandDTOAssembler;
    private final ModifyProductImageCommandDTOAssembler modifyProductImageCommandDTOAssembler;

    @Operation(summary = "상품 생성 API", description = "새로운 상품을 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(description = "성공", responseCode = "200", headers = {
                    @Header(name = "resultCode", description = "응답 코드"),
                    @Header(name = "resultMessage", description = "응답 메세지")
            }),
            @ApiResponse(description = "요청값 Validation 실패", responseCode = "400")
    })
    @PostMapping(PRODUCTS)
    public ResponseEntity<Void> createProduct(@RequestBody @Validated CreateProductReqDto reqDto) {
        productCommandService.createProduct(createProductCommandDTOAssembler.toCommand(reqDto));
        return new ResponseEntity<>(null, getSuccessHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "상품 이미지 임시 저장 API", description = "상품 이미지를 단건으로 임시 저장한다.")
    @PostMapping(value = PRODUCT_TEMP_IMAGE, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadProductTempImages(@RequestParam MultipartFile file) {
        String fileName = productImageCommandService.uploadProductTempImage(file);
        UploadProductTempImageRspDto rspDto = new UploadProductTempImageRspDto(fileName);
        return ResponseEntity.ok().headers(getSuccessHeaders()).body(rspDto);
    }

    @Operation(summary = "상품 리스트 조회 API", description = "상품 리스트를 페이징 처리하여 조회한다.")
    @GetMapping(PRODUCTS)
    public ResponseEntity<Object> getProductList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                        @RequestParam(required = false, defaultValue = "1") Integer size,
                                                        @RequestParam(required = false) String productCode,
                                                        @RequestParam(required = false) String productName,
                                                        @RequestParam(required = false, defaultValue = "false") boolean isProductNameContains,
                                                        @RequestParam(required = false) String mainCategoryCode,
                                                        @RequestParam(required = false) String subCategoryCode) {
        GetProductListCommand command = getProductListCommandDTOAssembler.toCommand(page, size, productCode, productName, isProductNameContains, mainCategoryCode, subCategoryCode);
        ProductListView productListView = productQueryService.getProductList(command);
        GetProductListRspDto rspDto = getProductListCommandDTOAssembler.toRspDto(productListView);
        return ResponseEntity.ok()
                .headers(getSuccessHeaders())
                .body(rspDto);
    }

    @Operation(summary = "단건 상품 조회 API", description = "상품 코드로 상품 정보를 조회한다.")
    @GetMapping(PRODUCT)
    public ResponseEntity<Object> getProduct(@PathVariable String productId) {
        ProductView product = productQueryService.getProduct(productId);
        GetProductRspDto rspDto = getProductCommandDTOAssembler.toPrdRspDto(product);
        return new ResponseEntity<>(rspDto, getSuccessHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "상품 수정 API", description = "상품 코드로 기존 상품 정보를 수정한다.")
    @PutMapping(PRODUCT)
    public ResponseEntity<Void> modifyProduct(@PathVariable String productId, @RequestBody ModifyProductReqDto reqDto) {
        productCommandService.modifyProduct(modifyProductCommandDTOAssembler.toCommand(productId, reqDto));
        return ResponseEntity.ok()
                .headers(getSuccessHeaders())
                .build();
    }

    @Operation(summary = "상품 정보 삭제 API", description = "상품 코드로 상품 정보를 삭제한다..")
    @DeleteMapping(PRODUCT)
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productCommandService.deleteProduct(productId);
        return ResponseEntity.ok()
                .headers(getSuccessHeaders())
                .build();
    }

    @Operation(summary = "상품 이미지 정보 조회 API", description = "상품 코드로 상품 이미지 정보를 조회한다.")
    @GetMapping(PRODUCT_IMAGE)
    public ResponseEntity<GetProductImageRspDto> getProductImageByProductCode(@PathVariable String productId, @RequestParam String divCode) {
        GetProductImageCommand command = getProductImageCommandDTOAssembler.toCommand(productId, divCode);
        List<ProductImageView> list = productQueryService.getProductImages(command);
        GetProductImageRspDto rspDto = getProductImageCommandDTOAssembler.toRspDTO(list, productId);
        return ResponseEntity.ok()
                .headers(getSuccessHeaders())
                .body(rspDto);
    }

    @Operation(summary = "상품 이미지 정보 수정 API", description = "상품 코드의 상품 이미지 정보를 수정한다.")
    @PutMapping(PRODUCT_IMAGE)
    public ResponseEntity<Void> modifyProductImage(@PathVariable String productId, @RequestBody ModifyProductImageReqDto reqDto) {
        productImageCommandService.modifyProductImages(modifyProductImageCommandDTOAssembler.toCommand(productId, reqDto));
        return ResponseEntity.ok().headers(getSuccessHeaders()).build();
    }


}
