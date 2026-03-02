
package com.shopfusion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    @GetMapping("/")
    public String home(Model model) {

        List<Product> products = Arrays.asList(
                new Product("Wireless Headphones", 2999),
                new Product("Smart Watch", 4999),
                new Product("Gaming Mouse", 1999),
                new Product("Laptop Backpack", 1499)
        );

        model.addAttribute("products", products);
        model.addAttribute("appName", "ShopFusion");
        return "index";
    }

    public record Product(String name, int price) {}
}
