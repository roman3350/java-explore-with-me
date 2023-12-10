package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Создание категории
     *
     * @param newCategoryDto Данные новой категории
     * @return Созданная категория
     */
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Создание категории {}", newCategoryDto);
        return categoryService.postCategory(newCategoryDto);
    }

    /**
     * Удаление категории
     *
     * @param catId ID категории на удаление
     */
    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable long catId) {
        log.info("Удаление категории с ID={}", catId);
        categoryService.deleteCategoryById(catId);
    }

    /**
     * Обновление категории
     *
     * @param catId          ID категории
     * @param newCategoryDto Данные на обновление
     * @return Обновленная категория
     */
    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto patchCategoryById(@PathVariable long catId,
                                         @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Обновление категории ID={}, Обновленные данные {}", catId, newCategoryDto);
        return categoryService.patchCategoryById(catId, newCategoryDto);
    }

    /**
     * Вывод всех категорий
     *
     * @param from начало списка
     * @param size количество категорий на странице
     * @return Категории
     */
    @GetMapping("/categories")
    public List<CategoryDto> getCategory(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Вывод всех категорий");
        return categoryService.getCategory(from, size);
    }

    /**
     * Вывод категории по ID
     *
     * @param catId ID категории
     * @return Категория
     */
    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("Вывод категории по ID={}", catId);
        return categoryService.getCategoryById(catId);
    }
}
