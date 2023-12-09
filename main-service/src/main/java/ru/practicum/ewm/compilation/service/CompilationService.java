package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    /**
     * Получение подборки событий
     *
     * @param pinned Закрепленные или не закрепленные подборки
     * @param from   с какого элемента начать
     * @param size   количество на странице
     * @return Подборки событий
     */
    List<CompilationDto> getCompilation(Boolean pinned, int from, int size);

    /**
     * Получение подборки событий по ID
     *
     * @param id ID
     * @return Подборка событий
     */
    CompilationDto getCompilationById(long id);

    /**
     * Создание подборки событий
     *
     * @param newCompilationDto Данные подборки событий
     * @return Созданная подборка событий
     */
    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки событий по ID
     *
     * @param compId ID подборки событий
     */
    void deleteCompilationById(long compId);

    /**
     * Обновление подборки событий
     *
     * @param compId                   ID подборки событий
     * @param updateCompilationRequest Данные на обновление
     * @return Обновленная подборка событий
     */
    CompilationDto patchCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);
}
