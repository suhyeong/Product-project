package co.kr.suhyeong.project.product.domain.model.aggregate;

import co.kr.suhyeong.project.product.domain.command.CreateProductCommand;
import co.kr.suhyeong.project.product.domain.constant.MainCategoryCode;
import co.kr.suhyeong.project.product.domain.constant.SubCategoryCode;
import co.kr.suhyeong.project.product.domain.model.converter.MainCategoryCodeConverter;
import co.kr.suhyeong.project.product.domain.model.converter.SubCategoryCodeConverter;
import co.kr.suhyeong.project.product.domain.model.converter.YOrNToBooleanConverter;
import co.kr.suhyeong.project.product.domain.model.entity.TimeEntity;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_master")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Product extends TimeEntity implements Serializable {

    @Id
    @Description("상품 코드")
    @Column(name = "product_code")
    private String productCode;

    @Description("상품 이름")
    @Column(name = "product_nm")
    private String productName;

    @Description("상품 메인 카테고리 코드")
    @Column(name = "main_cate_code")
    @Convert(converter = MainCategoryCodeConverter.class)
    private MainCategoryCode mainCategoryCode;

    @Description("상품 서브 카테고리 코드")
    @Column(name = "sub_cate_code")
    @Convert(converter = SubCategoryCodeConverter.class)
    private SubCategoryCode subCategoryCode;

    @Description("상품 원가")
    @Column(name = "cost")
    private int cost;

    @Description("할인율")
    @Column(name = "disc_rate")
    private Double discount_rate;

    @Description("판매 여부")
    @Convert(converter = YOrNToBooleanConverter.class)
    @Column(name = "sale_yn")
    private boolean saleYn;

    public Product(CreateProductCommand command, int sequence) {
        this.productCode = command.getMainCategoryCode().getCode() + command.getSubCategoryCode().getCode() + String.format("%05d", sequence);
        this.productName = command.getName();
        this.mainCategoryCode = command.getMainCategoryCode();
        this.subCategoryCode =  command.getSubCategoryCode();
        this.cost = command.getPrice();
        this.discount_rate = 0.0;
        this.saleYn = false;
    }
}
