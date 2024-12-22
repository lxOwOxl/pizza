package com.example.pizza.service;

import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.entity.ProductPrice;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;
import com.example.pizza.model.PizzaVariant;
import com.example.pizza.model.ProductDTO;
import com.example.pizza.enums.ProductType;
import com.example.pizza.repository.CrustPriceRepository;
import com.example.pizza.repository.ProductPriceRepository;
import com.example.pizza.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private CrustPriceRepository crustPriceRepository;

    public ProductDTO getProductDTO(int productId, Integer crustId, int quantity) {
        Product product = getProductById(productId);
        CrustPrice crustPrice = new CrustPrice();
        BigDecimal price = BigDecimal.ZERO;
        if (product.getType() == ProductType.PIZZA) {
            crustPrice = getCrustPriceById(crustId);
            price = productPriceRepository.findPriceByProductIdAndSize(productId, crustPrice.getSize())
                    .add(crustPrice.getAdditionalPrice()).multiply(BigDecimal.valueOf(quantity));
        } else {
            price = productPriceRepository.findPriceByProductIdAndSize(productId, null)
                    .multiply(BigDecimal.valueOf(quantity));
        }
        return new ProductDTO(product.getId(), product.getName(), product.getType(), product.getImage(),
                crustPrice.getSize(), crustPrice.getCrust(), quantity, price);
    }

    public List<CrustPrice> getAllCrustPrices() {
        return crustPriceRepository.findAll();
    }

    public Map<Size, List<CrustPrice>> getAllCrustPriceMapBySize() {
        Map<Size, List<CrustPrice>> crustPrices = new HashMap<>();

        for (Size size : Size.values()) {
            List<CrustPrice> crustPriceList = crustPriceRepository.findBySize(size);

            crustPrices.put(size, crustPriceList);
        }

        return crustPrices;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public CrustPrice getCrustPriceById(int CrustId) {
        return crustPriceRepository.findById(CrustId).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductPrice> getPriceListByProduct(int productId) {
        return productPriceRepository.findByProductId(productId);
    }

    public List<CrustPrice> getCrustPriceListBySize(Size size) {
        return crustPriceRepository.findBySize(size);
    }

    public List<Crust> getCrustBySize(Size size) {
        return crustPriceRepository.findCrustsBySize(size);
    }

    // Lấy sản phẩm theo id
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductsByType(ProductType type) {
        return productRepository.findByType(type);
    }

    // Thêm sản phẩm mới
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Cập nhật sản phẩm
    public Product update(Product product) {
        if (productRepository.existsById(product.getId())) {
            return productRepository.save(product);
        }
        return null;
    }

    // Xóa sản phẩm
    public void delete(int id) {
        productRepository.deleteById(id);
    }

    public CrustPrice getCrustPriceBySizeAndCrust(Size size, Crust crust) {
        return crustPriceRepository.findBySizeAndCrust(size, crust);
    }
}