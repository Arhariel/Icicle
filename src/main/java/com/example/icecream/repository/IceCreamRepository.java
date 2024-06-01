package com.example.icecream.repository;

import com.example.icecream.model.IceCream;
import com.example.icecream.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IceCreamRepository extends JpaRepository<IceCream, Long> {
    List<IceCream> findByCategory(Category category);
}
