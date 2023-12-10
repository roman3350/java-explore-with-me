package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    /**
     * Создание категории
     *
     * @param newCategoryDto Данные новой категории
     * @return Созданная категория
     */
    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    /**
     * Удаление категории
     *
     * @param catId ID категории на удаление
     */
    void deleteCategoryById(long catId);

    /**
     * Обновление категории
     *
     * @param catId          ID категории
     * @param newCategoryDto Данные на обновление
     * @return Обновленная категория
     */
    CategoryDto patchCategoryById(long catId, NewCategoryDto newCategoryDto);

    /**
     * Вывод всех категорий
     *
     * @param from начало списка
     * @param size количество категорий на странице
     * @return Категории
     */
    List<CategoryDto> getCategory(int from, int size);

    /**
     * Вывод категории по ID
     *
     * @param catId ID категории
     * @return Категория
     */
    CategoryDto getCategoryById(long catId);
}
