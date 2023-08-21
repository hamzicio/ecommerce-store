package ecommerce.api.vo.response;

import ecommerce.api.entity.ProductInfo;
import org.springframework.data.domain.Page;


public class CategoryPage {
    private String categoryName;

    private Integer categoryId;
    private Page<ProductInfo> page;

    public CategoryPage(String categoryName, Page<ProductInfo> page, Integer categoryId) {
        this.categoryName = categoryName;
        this.page = page;
        this.categoryId= categoryId;
    }


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Page<ProductInfo> getPage() {
        return page;
    }

    public void setPage(Page<ProductInfo> page) {
        this.page = page;
    }
}
