package ecommerce.api.api;


import ecommerce.api.service.ProductService;
import ecommerce.api.entity.ProductCategory;
import ecommerce.api.entity.ProductInfo;
import ecommerce.api.service.CategoryService;
import ecommerce.api.vo.response.CategoryPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping("/category")
    public Page<ProductCategory> findAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "3") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return categoryService.findAllPageable(request);
    }


    @GetMapping("/category/{type}")
    public CategoryPage showOne(@PathVariable("type") Integer categoryType,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "3") Integer size) {

        ProductCategory cat = categoryService.findByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInCategory = productService.findAllInCategory(categoryType, request);
        var tmp = new CategoryPage("", productInCategory,cat.getCategoryId());
        tmp.setCategoryName(cat.getCategoryName());
        return tmp;
    }

    @PostMapping("/seller/category")
    public ResponseEntity<?> addCategory(@RequestBody ProductCategory productCategory){
        try {
            return ResponseEntity.ok(categoryService.addNewCategory(productCategory));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/seller/category")
    public ResponseEntity<?> updateCategory(@RequestBody ProductCategory productCategory){
        try {
            return ResponseEntity.ok(categoryService.save(productCategory));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/category/public")
    public ResponseEntity<?> getAll(){
        try {
            return ResponseEntity.ok(categoryService.findAll());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/seller/category/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.ok().build();
    }
}
