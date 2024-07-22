package co.kr.suhyeong.project.product.domain.model.view;

import co.kr.suhyeong.project.product.domain.model.aggregate.Category;
import co.kr.suhyeong.project.product.domain.model.aggregate.Product;
import co.kr.suhyeong.project.util.DateTimeUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static co.kr.suhyeong.project.util.DateTimeUtils.DATE_FORMAT_YYYY_MM_DD_DOT;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductView {
    private String productCode;
    private String productName;
    private String createdAt;

    private String mainCategoryCode;
    private String mainCategoryName;
    private String subCategoryCode;
    private String subCategoryName;

    private int price;

    private List<ProductImageView> imageList;

    public ProductView(Product product) {
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.createdAt = DateTimeUtils.convertDateTimeToString(DATE_FORMAT_YYYY_MM_DD_DOT, product.getCreatedDate());
        this.mainCategoryCode = product.getMainCategoryCode();
        this.subCategoryCode = product.getSubCategoryCode();
        this.price = product.getCost();
        this.imageList = new ArrayList<>();
        product.getImages().forEach(image -> imageList.add(new ProductImageView(image.getImageDivCode(), image.getImgPath())));
    }

    @QueryProjection
    public ProductView(Product product, Category mainCategory, Category subCategory) {
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.createdAt = DateTimeUtils.convertDateTimeToString(DATE_FORMAT_YYYY_MM_DD_DOT, product.getCreatedDate());
        this.mainCategoryCode = product.getMainCategoryCode();
        this.mainCategoryCode = product.getMainCategoryCode();
        this.mainCategoryName = mainCategory.getName();
        this.subCategoryCode = product.getSubCategoryCode();
        this.subCategoryName = subCategory.getName();
        this.price = product.getCost();
    }
}
