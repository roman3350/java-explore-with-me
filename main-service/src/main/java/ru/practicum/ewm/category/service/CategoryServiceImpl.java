package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.exception.CategoryNotEmptyException;
import ru.practicum.ewm.category.exception.CategoryNotFoundException;
import ru.practicum.ewm.category.exception.DuplicateCategoryNameException;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    /**
     * Создание категории
     *
     * @param newCategoryDto Данные новой категории
     * @return Созданная категория
     */
    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.findFirstByName(newCategoryDto.getName()) != null) {
            throw new DuplicateCategoryNameException();
        }
        return CategoryMapper.toCategoryDto(categoryRepository
                .save(CategoryMapper.newCategoryDtoToCategory(newCategoryDto)));
    }

    /**
     * Удаление категории
     *
     * @param catId ID категории на удаление
     */
    @Override
    public void deleteCategoryById(long catId) {
        categoryRepository.findById(catId).orElseThrow(() ->
                new CategoryNotFoundException(catId));
        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new CategoryNotEmptyException();
        }
        categoryRepository.deleteById(catId);
    }

    /**
     * Обновление категории
     *
     * @param catId          ID категории
     * @param newCategoryDto Данные на обновление
     * @return Обновленная категория
     */
    @Override
    public CategoryDto patchCategoryById(long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new CategoryNotFoundException(catId));
        if (categoryRepository.findFirstByName(newCategoryDto.getName()) != null &&
                !category.getName().equals(newCategoryDto.getName())) {
            throw new DuplicateCategoryNameException();
        }
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    /**
     * Вывод всех категорий
     *
     * @param from начало списка
     * @param size количество категорий на странице
     * @return Категории
     */
    @Override
    public List<CategoryDto> getCategory(int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return CategoryMapper.toCategoryDto(categoryRepository.findAll(page).getContent());
    }

    /**
     * Вывод категории по ID
     *
     * @param catId ID категории
     * @return Категория
     */
    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new CategoryNotFoundException(catId));
        return CategoryMapper.toCategoryDto(category);
    }
}
